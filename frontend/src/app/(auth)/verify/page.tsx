'use client';

import { Button } from '@/components/ui/button';
import { MessageContainer } from '@/components/responses/MessageContainer';
import { useVerificationCode } from '@/hooks/auth/useVerificationCode';
import { Input } from '@/components/ui/input';
import { useRouter, useSearchParams } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function VerifyPage() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const email = searchParams.get('email');
    const [isAllowed, setIsAllowed] = useState(false);

    useEffect(() => {
        if (!email) {
            router.push('/auth/login');
            return;
        }
        setIsAllowed(true);
    }, [email, router]);

    const { code, inputsRef, handleChange, handleKeyDown, handlePaste, handleFocus, submitCode, resendCode, isLoading, isResending, error, success } =
        useVerificationCode(email || '');

    if (!isAllowed) return null;
    return (
        <div className="flex flex-col items-center gap-4">
            <div className="flex justify-center gap-3">
                {code.map((digit, i) => (
                    <Input
                        key={i}
                        id={`code-${i}`}
                        name={`code-${i}`}
                        ref={el => {
                            inputsRef.current[i] = el;
                        }}
                        type="text"
                        inputMode="numeric"
                        pattern="\d*"
                        maxLength={1}
                        value={digit}
                        onFocus={() => handleFocus(i)}
                        onChange={e => handleChange(e.target.value, i)}
                        onKeyDown={e => handleKeyDown(e, i)}
                        onPaste={handlePaste}
                        autoComplete="off"
                        aria-label={`Cyfra ${i + 1} kodu weryfikacyjnego`}
                        className="h-14 w-14 text-center text-lg font-semibold border-2 border-input focus-visible:ring-2 focus-visible:ring-primary"
                    />
                ))}
            </div>

            <div className="flex gap-2">
                <Button variant="main" onClick={() => submitCode(code.join(''))} disabled={code.some(d => d === '') || isLoading}>
                    Wyślij kod
                </Button>

                <Button variant="outline" onClick={resendCode} disabled={isResending || isLoading}>
                    Wyślij ponownie kod
                </Button>
            </div>

            <MessageContainer
                loading={isLoading || isResending}
                loadingText={isLoading ? 'Sprawdzanie kodu...' : 'Wysyłanie nowego kodu...'}
                error={error}
                success={success}
            />
        </div>
    );
}
