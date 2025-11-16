'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AiFillLock } from 'react-icons/ai';
import { Button } from '@/components/ui/button';
import PasswordInput from '@/components/inputs/auth/PasswordInput';
import { Input } from '@/components/ui/input';
import { Lock } from 'lucide-react';
import { ChangePasswordRequest, changePasswordSchema } from '@/lib/zod';
import { useAuth } from '@/components/providers/auth-provider';
import { Separator } from '@/components/ui/separator';
import DashboardSection from '@/components/layout/admin-panel/sections/dashboard-section';
import { ContentLayout } from '@/components/layout/admin-panel/content-layout/content-layout';

export default function ChangePassword() {
    const form = useForm<ChangePasswordRequest>({
        resolver: zodResolver(changePasswordSchema),
        defaultValues: {
            currentPassword: '',
            newPassword: '',
            confirmPassword: '',
        },
    });

    const { isSubmitted, changePassword } = useAuth();

    const onSubmit = async (data: ChangePasswordRequest) => {
        await changePassword(data.currentPassword, data.newPassword);
    };

    return (
        <ContentLayout title="Edytuj profil">
            <form onSubmit={form.handleSubmit(onSubmit)}>
                <DashboardSection
                    title="Zmiana hasła"
                    icon={Lock}
                    subtitle="Aby zmienić hasło, podaj swoje obecne hasło i nowe hasło."
                >
                    <div className="w-full space-y-5">
                        <PasswordInput
                            error={form.formState.errors.currentPassword?.message}
                            id="currentPassword"
                            label="Aktualne hasło"
                            showForgotPassword={false}
                        >
                            <Input
                                id="currentPassword"
                                type="password"
                                placeholder="••••••••"
                                {...form.register('currentPassword')}
                                className="pl-10"
                            />
                        </PasswordInput>

                        <Separator />

                        <PasswordInput
                            error={form.formState.errors.newPassword?.message}
                            id="newPassword"
                            label="Nowe hasło"
                            showForgotPassword={false}
                        >
                            <Input
                                id="newPassword"
                                type="password"
                                placeholder="••••••••"
                                {...form.register('newPassword')}
                                className="pl-10"
                            />
                        </PasswordInput>

                        <div className="mt-3 text-xs text-gray-600 dark:text-gray-400 space-y-1">
                            <p className="font-medium">Wymagania dla hasła:</p>
                            <ul className="list-disc list-inside space-y-0.5">
                                <li>Minimum 8 znaków</li>
                                <li>Przynajmniej jedna wielka litera (A-Z)</li>
                                <li>Przynajmniej jedna mała litera (a-z)</li>
                                <li>Przynajmniej jedna cyfra (0-9)</li>
                            </ul>
                        </div>

                        <PasswordInput
                            error={form.formState.errors.confirmPassword?.message}
                            id="confirmPassword"
                            label="Potwierdź nowe hasło"
                            showForgotPassword={false}
                        >
                            <Input
                                id="confirmPassword"
                                type="password"
                                placeholder="••••••••"
                                {...form.register('confirmPassword')}
                                className="pl-10"
                            />
                        </PasswordInput>

                        <Button
                            animated
                            type="submit"
                            className="w-full flex items-center justify-center gap-2"
                            disabled={isSubmitted}
                            variant="main"
                            loading={isSubmitted}
                        >
                            <AiFillLock className="h-5 w-5" />
                            Zmień hasło
                        </Button>
                    </div>
                </DashboardSection>
            </form>
        </ContentLayout>
    );

}
