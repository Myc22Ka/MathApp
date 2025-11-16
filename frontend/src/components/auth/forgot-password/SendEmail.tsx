'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AiOutlineArrowRight } from 'react-icons/ai';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import TextInput from '@/components/inputs/auth/TextInput';
import { useAuth } from '@/components/providers/auth-provider';
import { LoaderCircle } from 'lucide-react';
import { ForgotPasswordRequest, forgotPasswordSchema } from '@/lib/zod';
import { MessageContainer } from '@/components/responses/MessageContainer';

export default function SendEmail() {
    const form = useForm<ForgotPasswordRequest>({
        resolver: zodResolver(forgotPasswordSchema),
        defaultValues: { email: '' },
    });

    const { forgotPassword, isSubmitted } = useAuth();

    const onSubmit = async (data: ForgotPasswordRequest) => {
        await forgotPassword(data.email);
    };

    const getFieldError = (fieldName: keyof ForgotPasswordRequest) => form.formState.errors[fieldName]?.message;

    return (
        <div className="w-full max-w-md mx-auto">
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
                <TextInput error={form.formState.errors.email?.message} id="email" label="Adres e-mail">
                    <Input
                        id="email"
                        type="text"
                        placeholder="email@domain.com"
                        {...form.register('email')}
                        className="pl-10"
                        error={!!getFieldError('email')}
                    />
                </TextInput>

                <Button animated type="submit" className="w-full flex items-center justify-center gap-2" disabled={isSubmitted} variant="main">
                    {isSubmitted ? (
                        <LoaderCircle className="animate-spin" />
                    ) : (
                        <>
                            Wyślij link
                            <AiOutlineArrowRight className="h-5 w-5" />
                        </>
                    )}
                </Button>
            </form>
        </div>
    );
}
