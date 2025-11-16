import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User } from '@/types/User';
import { useRouter } from 'next/navigation';
import { LoginRequest, ProfileFormData, RegisterRequest } from '@/lib/zod';
import { get, post, getApiErrorMessage, del, ApiDefaultResponse, patch } from '@/lib/axios';
import { toast } from 'sonner';

type AuthContextType = {
    user: User | null;
    pending2FA: { userId: number } | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    isSubmitted: boolean;
    login: (data: LoginRequest) => Promise<void>;
    register: (data: RegisterRequest) => Promise<void>;
    logout: () => Promise<void>;
    refreshUser: () => Promise<void>;
    forgotPassword: (email: string) => Promise<void>;
    resetPassword: (code: string, newPassword: string) => Promise<void>;
    changePassword: (currentPassword: string, newPassword: string) => Promise<void>;
    deleteAccount: (password: string) => Promise<void>;
    toggleTwoFactor: (enabled: boolean) => Promise<void>;
    updateProfile: (profileData: ProfileFormData) => Promise<void>;
    uploadProfileImage: (file: File) => Promise<void>;
    loadProfileImage: () => Promise<string | null>;
    deleteProfileImage: () => Promise<void>;
    downloadProfileImage: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null);
    const [pending2FA, setPending2FA] = useState<{ userId: number } | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const router = useRouter();

    const login = async (data: LoginRequest) => {
        try {
            setIsSubmitted(true);

            const response = await post<User>('/auth/sign-in', data);

            // 2FA required - zwrócono tylko id i email
            if (response.email && !response.login) {
                router.push(
                    `/verify?email=${encodeURIComponent(response.email)}`
                );
                return;
            }

            // Pełne logowanie
            setUser(response);
            toast.success('Zalogowano pomyślnie');
            router.push('/');
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    const register = async (data: RegisterRequest) => {
        try {
            setIsSubmitted(true);
            const registeredUser = await post<User>('/auth/register', data);
            setUser(registeredUser);

            const encodedEmail = encodeURIComponent(registeredUser.email);
            const uid = crypto.randomUUID();

            sessionStorage.setItem('verify_uid', uid);
            toast.success('Konto utworzone pomyślnie');
            router.push(`/verify?email=${encodedEmail}&uid=${uid}`);
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    const forgotPassword = async (email: string) => {
        try {
            setIsSubmitted(true);

            const response = await post<{ message: string }>('/auth/password/request', { email });
            toast.success(response.message);
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    const resetPassword = async (code: string, newPassword: string) => {
        try {
            setIsSubmitted(true);

            const response = await post<{ message: string }>('/auth/password/confirm', {
                code,
                newPassword,
            });

            toast.success(response.message);
            router.push('/auth/login');
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    const changePassword = async (oldPassword: string, newPassword: string) => {
        try {
            setIsSubmitted(true);

            const response = await post<{ message: string }>('/auth/password/change', {
                oldPassword,
                newPassword,
            });

            toast.success(response.message);
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    const deleteAccount = async (password: string) => {
        try {
            setIsSubmitted(true);

            await del('/auth/delete', { password });

            setUser(null);
            toast.success('Konto zostało usunięte');
            router.push('/');
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    const logout = async () => {
        try {
            await post('/auth/sign-out');
            toast.success('Wylogowano pomyślnie');
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setUser(null);
            setPending2FA(null);
            router.push('/auth/login');
        }
    };

    const refreshUser = async () => {
        try {
            const fetchedUser = await get<User>('/auth/me');
            setUser(fetchedUser);
        } catch {
            setUser(null);
        }
    };

    const updateProfile = async (profileData: ProfileFormData) => {
        try {
            setIsSubmitted(true);

            const response = await patch<User>('/auth/update', profileData);
            setUser(response);
            toast.success('Profil zaktualizowany pomyślnie');
        } catch (err) {
            toast.error(getApiErrorMessage(err));
            throw err;
        } finally {
            setIsSubmitted(false);
        }
    };

    const toggleTwoFactor = async (enabled: boolean) => {
        try {
            setIsSubmitted(true);
            const response = await post<ApiDefaultResponse>('/auth/2fa', { enabled });
            await refreshUser();
            toast.success(response.message);
        } catch (err) {
            toast.error(getApiErrorMessage(err));
            throw err;
        } finally {
            setIsSubmitted(false);
        }
    };

    const uploadProfileImage = async (file: File) => {
        try {
            setIsSubmitted(true);
            const formData = new FormData();
            formData.append('file', file);
            formData.append('type', 'PROFILE');

            const response = await post<ApiDefaultResponse>('/api/images/upload', formData, {
                'Content-Type': 'multipart/form-data',
            });

            toast.success(response.message);
            await refreshUser();
        } catch (err) {
            toast.error(getApiErrorMessage(err));
            throw err;
        } finally {
            setIsSubmitted(false);
        }
    };

    const loadProfileImage = async () => {
        try {
            setIsSubmitted(true);
            const blob = await get<Blob>('/api/images/download', { type: 'PROFILE' }, {}, 'blob');

            return URL.createObjectURL(blob);
        } catch (err) {
            toast.error(getApiErrorMessage(err));

            return null;
        } finally {
            setIsSubmitted(false);
        }
    };

    const deleteProfileImage = async () => {
        try {
            setIsSubmitted(true);

            const response = await del<ApiDefaultResponse>('/api/images/delete', { type: 'PROFILE' });

            toast.success(response.message);
            await refreshUser();

            if (user) {
                setUser({ ...user, profilePhotoUrl: null });
            }
        } catch (err) {
            toast.error(getApiErrorMessage(err));
            throw err;
        } finally {
            setIsSubmitted(false);
        }
    };

    const downloadProfileImage = async () => {
        if (!user) return;

        try {
            setIsSubmitted(true);

            const blob = await get<Blob>('/api/images/download', { type: 'PROFILE' }, {}, 'blob');

            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `profile-photo-${Date.now()}.jpg`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);

            toast.success('Zdjęcie pobrane');
        } catch (err) {
            toast.error(getApiErrorMessage(err));
        } finally {
            setIsSubmitted(false);
        }
    };

    useEffect(() => {
        const initializeUser = async () => {
            try {
                const fetchedUser = await get<User>('/auth/me');
                setUser(fetchedUser);
            } catch {
                setUser(null);
                setPending2FA(null);
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
                pending2FA,
                isAuthenticated: !!user,
                isLoading,
                isSubmitted,
                login,
                register,
                logout,
                refreshUser,
                forgotPassword,
                resetPassword,
                changePassword,
                deleteAccount,
                toggleTwoFactor,
                updateProfile,
                uploadProfileImage,
                loadProfileImage,
                deleteProfileImage,
                downloadProfileImage,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) throw new Error('useAuth must be used within an AuthProvider');
    return context;
}
