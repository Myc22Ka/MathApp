'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AiOutlineArrowRight } from 'react-icons/ai';
import { Button } from '@/components/ui/button';
import EmailInput from '@/components/inputs/auth/EmailInput';
import PasswordInput from '@/components/inputs/auth/PasswordInput';
import { registerSchema, RegisterRequest } from '@/lib/zod';
import FormFooter from '../layout/FormFooter';
import { Input } from '@/components/ui/input';
import TextInput from '@/components/inputs/auth/TextInput';
import { LoaderCircle } from 'lucide-react';
import { useAuth } from '@/components/providers/auth-provider';
import CheckboxInput from '@/components/inputs/auth/CheckboxInput';
import { Checkbox } from '@/components/ui/checkbox';

export default function RegisterForm() {
    const form = useForm<RegisterRequest>({
        resolver: zodResolver(registerSchema),
        defaultValues: {
            email: '',
            password: '',
            confirmPassword: '',
            login: '',
            acceptTerms: false,
        },
    });

    const { register, isSubmitted } = useAuth();

    const onSubmit = async (data: RegisterRequest) => {
        await register(data);
    };

    const getFieldError = (fieldName: keyof RegisterRequest) => form.formState.errors[fieldName]?.message;

    return (
        <div className="w-full max-w-md mx-auto">
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
                <TextInput error={getFieldError('login')} id="username">
                    <Input
                        id="username"
                        type="text"
                        placeholder="Nazwa użytkownika"
                        {...form.register('login')}
                        className="pl-10"
                        error={!!getFieldError('login')}
                    />
                </TextInput>

                <EmailInput error={getFieldError('email')} id="email">
                    <Input
                        id="email"
                        type="email"
                        placeholder="email@domain.com"
                        {...form.register('email')}
                        className="pl-10"
                        error={!!getFieldError('email')}
                    />
                </EmailInput>

                <PasswordInput error={getFieldError('password')} showForgotPassword={false} id="password">
                    <Input
                        id="password"
                        type="password"
                        placeholder="••••••••"
                        {...form.register('password')}
                        className="pl-10"
                        error={!!getFieldError('password')}
                    />
                </PasswordInput>

                <PasswordInput error={getFieldError('confirmPassword')} showForgotPassword={false} id="confirmPassword" label="Powtórz hasło">
                    <Input
                        id="confirmPassword"
                        type="password"
                        placeholder="••••••••"
                        {...form.register('confirmPassword')}
                        className="pl-10"
                        error={!!getFieldError('confirmPassword')}
                    />
                </PasswordInput>

                <CheckboxInput
                    id="acceptTerms"
                    label={
                        <>
                            Akceptuję{' '}
                            <a href="/terms" className="text-(--main) underline!" target="_blank">
                                regulamin serwisu
                            </a>
                        </>
                    }
                    error={getFieldError('acceptTerms')}
                >
                    <Checkbox id="acceptTerms" onCheckedChange={checked => form.setValue('acceptTerms', checked === true)} className="cursor-pointer" />
                </CheckboxInput>

                <Button animated type="submit" className="w-full flex items-center justify-center gap-2 mt-2" disabled={isSubmitted} variant="main">
                    {isSubmitted ? (
                        <LoaderCircle className="animate-spin" />
                    ) : (
                        <>
                            Zarejestruj się
                            <AiOutlineArrowRight className="h-5 w-5" />
                        </>
                    )}
                </Button>
            </form>
            <FormFooter type="register" />
        </div>
    );
}
