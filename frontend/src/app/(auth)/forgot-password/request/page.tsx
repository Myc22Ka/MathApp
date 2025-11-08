'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AiOutlineArrowRight } from 'react-icons/ai';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import TextInput from '@/components/inputs/auth/TextInput';
import { LoaderCircle } from 'lucide-react';
import { useSearchParams } from 'next/navigation';
import { MessageContainer } from '@/components/responses/MessageContainer';
import { PasswordResetRequest, passwordResetSchema } from '@/lib/zod';
import { useAuth } from '@/context/AuthContext';

export default function ForgotPasswordRequestPage() {
    const params = useSearchParams();
    const token = params.get('token');

    const form = useForm<PasswordResetRequest>({
        resolver: zodResolver(passwordResetSchema),
        defaultValues: {
            password: '',
            confirmPassword: '',
        },
    });

    const {
        handleSubmit,
        register,
        formState: { errors },
    } = form;

    const { error, isSubmitted, success, resetPassword } = useAuth();

    const onSubmit = async (data: PasswordResetRequest) => {
        console.log(token);

        if (!token) return;

        await resetPassword(token, data.password);
    };

    return (
        <div className="w-full max-w-md mx-auto">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
                <TextInput error={errors.password?.message} id="password" label="Nowe hasło">
                    <Input id="password" type="password" placeholder="••••••••" {...register('password')} className="pl-10" />
                </TextInput>

                <TextInput error={errors.confirmPassword?.message} id="confirmPassword" label="Potwierdź hasło">
                    <Input id="confirmPassword" type="password" placeholder="••••••••" {...register('confirmPassword')} className="pl-10" />
                </TextInput>

                <MessageContainer error={error} success={success} />

                <Button animated type="submit" className="w-full flex items-center justify-center gap-2" disabled={isSubmitted} variant="main">
                    {isSubmitted ? (
                        <LoaderCircle className="animate-spin" />
                    ) : (
                        <>
                            Zmień hasło
                            <AiOutlineArrowRight className="h-5 w-5" />
                        </>
                    )}
                </Button>
            </form>
        </div>
    );
}
