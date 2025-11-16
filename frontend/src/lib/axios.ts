import axios, { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';

export const BASE_URL =
    process.env.NEXT_PUBLIC_PLATFORM_URL && process.env.NEXT_PUBLIC_BACKEND_PORT
        ? `http://${process.env.NEXT_PUBLIC_PLATFORM_URL}:${process.env.NEXT_PUBLIC_BACKEND_PORT}`
        : 'http://localhost:8080';

export const API_URL = `${BASE_URL}/api`;
export const AUTH_URL = `${BASE_URL}/api/auth`;

export interface ApiError {
    timestamp: string;
    message: string;
    status: number;
}

export interface ApiDefaultResponse {
    timestamp: string;
    message: string;
    status: number;
}

export interface RequestConfig {
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
    url: string;
    data?: unknown;
    params?: unknown;
}

const apiClient: AxiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true,
    paramsSerializer: { indexes: null },
});

apiClient.interceptors.request.use(
    config => config,
    error => Promise.reject(error)
);

apiClient.interceptors.response.use(
    response => {
        if (response.status === 202) {
            const error = new AxiosError('2FA Required');
            error.response = response;
            throw error;
        }
        return response;
    },
    (error: AxiosError<ApiError>) => {
        return Promise.reject(error);
    }
);

export interface RequestConfig {
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
    url: string;
    data?: unknown;
    params?: unknown;
    headers?: Record<string, string>;
    responseType?: AxiosRequestConfig['responseType'];
}

export const request = <T>({ method, url, data, params, headers, responseType }: RequestConfig): Promise<AxiosResponse<T>> => {
    const config: AxiosRequestConfig = {
        method,
        url,
        withCredentials: true,
        headers,
        responseType,
    };

    if (method === 'GET' || method === 'PUT') config.params = params;
    else config.data = data;

    return apiClient(config);
};

export const get = async <T>(
    url: string,
    params?: unknown,
    headers?: Record<string, string>,
    responseType?: AxiosRequestConfig['responseType']
): Promise<T> => {
    const response = await request<T>({ method: 'GET', url, params, headers, responseType });
    return response.data;
};

export const post = async <T>(url: string, data?: unknown, headers?: Record<string, string>, responseType?: AxiosRequestConfig['responseType']): Promise<T> => {
    const response = await request<T>({ method: 'POST', url, data, headers, responseType });
    return response.data;
};

export const put = async <T>(
    url: string,
    params?: unknown,
    headers?: Record<string, string>,
    responseType?: AxiosRequestConfig['responseType']
): Promise<T> => {
    const response = await request<T>({ method: 'PUT', url, params, headers, responseType });
    return response.data;
};

export const del = async <T>(url: string, data?: unknown, headers?: Record<string, string>, responseType?: AxiosRequestConfig['responseType']): Promise<T> => {
    const response = await request<T>({ method: 'DELETE', url, data, headers, responseType });
    return response.data;
};

export const patch = async <T>(
    url: string,
    data?: unknown,
    headers?: Record<string, string>,
    responseType?: AxiosRequestConfig['responseType']
): Promise<T> => {
    const response = await request<T>({ method: 'PATCH', url, data, headers, responseType });
    return response.data;
};

export function getApiErrorMessage(error: unknown): string {
    if (typeof error === 'object' && error !== null && 'isAxiosError' in error && (error as AxiosError).isAxiosError === true) {
        const apiError = (error as AxiosError<ApiError>).response?.data;
        return apiError?.message || 'An unknown server error occurred.';
    }

    return 'An unexpected error occurred.';
}

export function appendParamsToUrl(url: string, params?: Record<string, unknown>): string {
    if (!params || Object.keys(params).length === 0) return url;

    const query = new URLSearchParams();

    for (const [key, value] of Object.entries(params)) {
        if (value !== undefined && value !== null) {
            query.append(key, String(value));
        }
    }

    const separator = url.includes('?') ? '&' : '?';
    return `${url}${separator}${query.toString()}`;
}

export default apiClient;
