"use client";

import { useState, useCallback } from "react";
import { getApiErrorMessage, post, get } from "@/lib/axios";

interface UseApiOptions<T> {
  method?: "GET" | "POST";
  url: string;
  body?: unknown;
  immediate?: boolean;
  onSuccess?: (data: T) => void;
  onError?: (err: string) => void;
}

export function useApi<T = unknown>({
  method = "GET",
  url,
  body,
  onSuccess,
  onError,
}: UseApiOptions<T>) {
  const [data, setData] = useState<T | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(
    async (overrideBody?: unknown) => {
      setIsLoading(true);
      setError(null);

      try {
        let response: T;
        if (method === "POST") {
          response = await post<T>(url, overrideBody ?? body);
        } else {
          response = await get<T>(url);
        }

        setData(response);
        onSuccess?.(response);
      } catch (err) {
        const msg = getApiErrorMessage(err);
        setError(msg);
        onError?.(msg);
      } finally {
        setIsLoading(false);
      }
    },
    [method, url, body, onSuccess, onError]
  );

  return { data, isLoading, error, fetchData, setError, setData };
}
