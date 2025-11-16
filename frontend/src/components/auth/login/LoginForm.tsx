'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AiOutlineArrowRight } from 'react-icons/ai';
import { Button } from '@/components/ui/button';
import PasswordInput from '@/components/inputs/auth/PasswordInput';
import { loginSchema, LoginRequest } from '@/lib/zod';
import FormFooter from '../layout/FormFooter';
import { Input } from '@/components/ui/input';
import TextInput from '@/components/inputs/auth/TextInput';
import { useAuth } from '@/components/providers/auth-provider';
import { LoaderCircle } from 'lucide-react';

export default function LoginForm() {
    const form = useForm<LoginRequest>({
        resolver: zodResolver(loginSchema),
        defaultValues: { identifier: '', password: '' },
    });

    const { login, isSubmitted } = useAuth();

    const onSubmit = async (data: LoginRequest) => {
        await login(data);
    };

    return (
        <div className="w-full max-w-md mx-auto">
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
                <TextInput error={form.formState.errors.identifier?.message} id="identifier">
                    <Input id="identifier" type="text" placeholder="email@domain.com" {...form.register('identifier')} className="pl-10" />
                </TextInput>
                <PasswordInput error={form.formState.errors.password?.message} id="password">
                    <Input id="password" type="password" placeholder="••••••••" {...form.register('password')} className="pl-10" />
                </PasswordInput>

                <Button animated type="submit" className="w-full flex items-center justify-center gap-2" disabled={isSubmitted} variant="main">
                    {isSubmitted ? (
                        <LoaderCircle className="animate-spin" />
                    ) : (
                        <>
                            Zaloguj się
                            <AiOutlineArrowRight className="h-5 w-5" />
                        </>
                    )}
                </Button>
            </form>

            <FormFooter type="login" />
        </div>
    );
}
