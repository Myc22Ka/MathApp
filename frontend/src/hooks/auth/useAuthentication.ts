"use client";

import { useState } from "react";
import { post, getApiErrorMessage } from "@/lib/axios";
import { User } from "@/types/User";

type AuthHookReturn<T> = {
  authenticate: (data: T) => Promise<User | null>;
  isLoading: boolean;
  submitted: boolean;
  error: string | null;
  user: User | null;
};

export function useAuthentication<T>(endpoint: string): AuthHookReturn<T> {
  const [isLoading, setIsLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [user, setUser] = useState<User | null>(null);

  const authenticate = async (data: T): Promise<User | null> => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await post<User>(endpoint, data);
      setUser(response);
      setSubmitted(true);
      setTimeout(() => setSubmitted(false), 3000);
      return response;
    } catch (err) {
      setError(getApiErrorMessage(err));
      return null;
    } finally {
      setIsLoading(false);
    }
  };

  return { authenticate, isLoading, submitted, error, user };
}
