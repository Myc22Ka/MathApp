import {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";
import { User } from "@/types/User";
import { useRouter } from "next/navigation";
import { LoginRequest, RegisterRequest } from "@/lib/zod";
import { get, post, getApiErrorMessage } from "@/lib/axios";

type AuthContextType = {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isSubmitted: boolean;
  error: string | null;
  success: string | null;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => Promise<void>;
  refreshUser: () => Promise<void>;
  forgotPassword: (email: string) => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const router = useRouter();

  const login = async (data: LoginRequest) => {
    try {
      setIsSubmitted(true);
      setError(null);
      const loggedUser = await post<User>("/auth/sign-in", data);
      setUser(loggedUser);
      router.push(!loggedUser.twoFactorEnabled ? "/" : "/verify");
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setIsSubmitted(false);
    }
  };

  const register = async (data: RegisterRequest) => {
    try {
      setIsSubmitted(true);
      setError(null);
      const registeredUser = await post<User>("/auth/register", data);
      setUser(registeredUser);
      router.push("/verify");
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setIsSubmitted(false);
    }
  };

  const forgotPassword = async (email: string) => {
    try {
      setIsSubmitted(true);
      setError(null);
      setSuccess(null);

      const response = await post<{ message: string }>(
        "/auth/password/request",
        { email }
      );
      setSuccess(response.message);
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setIsSubmitted(false);
    }
  };

  const logout = async () => {
    try {
      await post("/auth/sign-out");
    } finally {
      setUser(null);
      router.push("/login");
    }
  };

  const refreshUser = async () => {
    try {
      const fetchedUser = await get<User>("/auth/me");
      setUser(fetchedUser);
    } catch {
      setUser(null);
    }
  };

  useEffect(() => {
    const initializeUser = async () => {
      try {
        const fetchedUser = await get<User>("/auth/me");
        setUser(fetchedUser);
      } catch {
        localStorage.removeItem("auth_token");
        setUser(null);
      } finally {
        setIsLoading(false);
      }
    };

    initializeUser();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        isSubmitted,
        error,
        success,
        login,
        register,
        logout,
        refreshUser,
        forgotPassword,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
}
