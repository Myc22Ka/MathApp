'use client';
import { useState, useRef, useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { getApiErrorMessage, post, appendParamsToUrl } from '@/lib/axios';
import { useAuth } from '@/components/providers/auth-provider';

export function useVerificationCode(email: string) {
    const [code, setCode] = useState(Array(6).fill(''));
    const [isResending, setIsResending] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const inputsRef = useRef<Array<HTMLInputElement | null>>([]);
    const router = useRouter();

    const { refreshUser } = useAuth();

    const submitCode = useCallback(
        async (code: string) => {
            setIsLoading(true);
            setError(null);
            setSuccess(null);
            try {
                await post(appendParamsToUrl('/auth/verify-code', { email, code }));
                setSuccess('Kod zweryfikowany pomyślnie!');
                await refreshUser();

                router.push('/dashboard');
            } catch (err) {
                setError(getApiErrorMessage(err));
            } finally {
                setIsLoading(false);
            }
        },
        [email, refreshUser, router]
    );

    const resendCode = async () => {
        setIsResending(true);
        setError(null);
        setCode(Array(6).fill(''));
        inputsRef.current[0]?.focus();
        try {
            await post(appendParamsToUrl('/auth/resend-code', { email }));
            setSuccess('Nowy kod został wysłany!');
        } catch (err) {
            setError(getApiErrorMessage(err));
        } finally {
            setIsResending(false);
        }
    };

    const handleFocus = (index: number) => {
        const firstEmptyIndex = code.findIndex(v => v === '');
        if (code[index] !== '') return;
        if (firstEmptyIndex === -1) {
            inputsRef.current.forEach(input => input?.blur());
            return;
        }
        if (index !== firstEmptyIndex) {
            inputsRef.current[firstEmptyIndex]?.focus();
        }
    };

    const handleChange = (value: string, index: number) => {
        if (!/^\d?$/.test(value)) return;
        const newCode = [...code];
        newCode[index] = value;
        setCode(newCode);

        if (value && index < newCode.length - 1) {
            setTimeout(() => {
                inputsRef.current[index + 1]?.focus();
            }, 0);
        }

        if (newCode.every(v => v !== '')) {
            setTimeout(() => {
                inputsRef.current.forEach(input => input?.blur());
                submitCode(newCode.join(''));
            }, 0);
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, index: number) => {
        if (e.key === 'Backspace') {
            e.preventDefault();
            const newCode = [...code];
            if (newCode[index] !== '') {
                newCode[index] = '';
                setCode(newCode);
            } else if (index > 0) {
                newCode[index - 1] = '';
                setCode(newCode);
                setTimeout(() => {
                    inputsRef.current[index - 1]?.focus();
                }, 0);
            }
        }
    };

    const handlePaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
        const pasted = e.clipboardData.getData('text').trim();
        if (/^\d{6}$/.test(pasted)) {
            const newCode = pasted.split('');
            setCode(newCode);
            submitCode(pasted);
            inputsRef.current.forEach(input => input?.blur());
        }
        e.preventDefault();
    };

    useEffect(() => {
        setCode(Array(6).fill(''));

        const handleClipboardOnReturn = async () => {
            if (document.visibilityState === 'visible') {
                try {
                    const text = await navigator.clipboard.readText();
                    if (/^\d{6}$/.test(text)) {
                        setCode(text.split(''));
                        submitCode(text);
                        inputsRef.current.forEach(input => input?.blur());
                    }
                } catch {}
            }
        };

        window.addEventListener('focus', handleClipboardOnReturn);
        document.addEventListener('visibilitychange', handleClipboardOnReturn);

        return () => {
            window.removeEventListener('focus', handleClipboardOnReturn);
            document.removeEventListener('visibilitychange', handleClipboardOnReturn);
        };
    }, [submitCode]);

    useEffect(() => {
        const firstEmptyIndex = code.findIndex(v => v === '');
        if (firstEmptyIndex !== -1) {
            setTimeout(() => inputsRef.current[firstEmptyIndex]?.focus(), 50);
        }
    }, [code]);

    return {
        code,
        setCode,
        inputsRef,
        handleFocus,
        handleChange,
        handleKeyDown,
        handlePaste,
        submitCode,
        resendCode,
        isLoading,
        isResending,
        error,
        success,
    };
}
