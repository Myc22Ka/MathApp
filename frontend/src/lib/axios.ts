import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
} from "axios";
import { toast } from "sonner";

export const BASE_URL =
  process.env.NEXT_PUBLIC_PLATFORM_URL && process.env.NEXT_PUBLIC_BACKEND_PORT
    ? `http://${process.env.NEXT_PUBLIC_PLATFORM_URL}:${process.env.NEXT_PUBLIC_BACKEND_PORT}`
    : "http://localhost:8080";

export const API_URL = `${BASE_URL}/api`;
export const AUTH_URL = `${BASE_URL}/api/auth`;

export interface ApiError {
  timestamp: string;
  message: string;
  status: number;
}

export interface RequestConfig {
  method: "GET" | "POST" | "PUT" | "DELETE" | "PATCH";
  url: string;
  data?: unknown;
  params?: unknown;
}

const apiClient: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
  paramsSerializer: { indexes: null },
});

apiClient.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiError>) => {
    return Promise.reject(error);
  }
);

export const request = <T>({
  method,
  url,
  data,
  params,
}: RequestConfig): Promise<AxiosResponse<T>> => {
  const config: AxiosRequestConfig = {
    method,
    url,
    withCredentials: true,
  };

  if (method === "GET" || method === "PUT") config.params = params;
  else config.data = data;

  return apiClient(config);
};

export const get = async <T>(url: string, params?: unknown): Promise<T> => {
  const response = await request<T>({ method: "GET", url, params });
  return response.data;
};

export const post = async <T>(url: string, data?: unknown): Promise<T> => {
  const response = await request<T>({ method: "POST", url, data });
  return response.data;
};

export const put = async <T>(url: string, params?: unknown): Promise<T> => {
  const response = await request<T>({ method: "PUT", url, params });
  return response.data;
};

export const del = async <T>(url: string, data?: unknown): Promise<T> => {
  const response = await request<T>({ method: "DELETE", url, data });
  return response.data;
};

export const patch = async <T>(url: string, data?: unknown): Promise<T> => {
  const response = await request<T>({ method: "PATCH", url, data });
  return response.data;
};

export function getApiErrorMessage(error: unknown): string {
  if (
    typeof error === "object" &&
    error !== null &&
    "isAxiosError" in error &&
    (error as AxiosError).isAxiosError === true
  ) {
    const apiError = (error as AxiosError<ApiError>).response?.data;
    return apiError?.message || "An unknown server error occurred.";
  }

  return "An unexpected error occurred.";
}

export function appendParamsToUrl(
  url: string,
  params?: Record<string, unknown>
): string {
  if (!params || Object.keys(params).length === 0) return url;

  const query = new URLSearchParams();

  for (const [key, value] of Object.entries(params)) {
    if (value !== undefined && value !== null) {
      query.append(key, String(value));
    }
  }

  const separator = url.includes("?") ? "&" : "?";
  return `${url}${separator}${query.toString()}`;
}

export default apiClient;
