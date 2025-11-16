'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { FaTrashAlt } from 'react-icons/fa';
import { Button } from '@/components/ui/button';
import PasswordInput from '@/components/inputs/auth/PasswordInput';
import { Input } from '@/components/ui/input';
import TextInput from '@/components/inputs/auth/TextInput';
import { LoaderCircle, AlertTriangle } from 'lucide-react';
import { ContentLayout } from '@/components/layout/admin-panel/content-layout/content-layout';
import { DeleteAccountRequest, deleteAccountSchema } from '@/lib/zod';
import { useAuth } from '@/components/providers/auth-provider';
import DashboardSection from '@/components/layout/admin-panel/sections/dashboard-section';

export default function DeleteAccount() {
    const form = useForm<DeleteAccountRequest>({
        resolver: zodResolver(deleteAccountSchema),
        defaultValues: {
            password: '',
            confirmation: '',
        },
    });

    const { isSubmitted, deleteAccount } = useAuth();

    const onSubmit = async (data: DeleteAccountRequest) => {
        await deleteAccount(data.password);
    };

    return (
        <ContentLayout title="Usuń konto">
            <form onSubmit={form.handleSubmit(onSubmit)}>
                <DashboardSection
                    title="Usuń konto"
                    icon={AlertTriangle}
                    subtitle="Uwaga! Ta akcja jest nieodwracalna. Po usunięciu konta wszystkie dane przepadną jak twoje merge requesty bez testów."
                >
                    <div className="w-full space-y-5">

                        <div className="bg-red-50 dark:bg-red-950/30 border border-red-200 dark:border-red-900 rounded-lg p-4 flex gap-3">
                            <AlertTriangle className="w-5 h-5 text-red-600 dark:text-red-400 shrink-0 mt-0.5" />
                            <div>
                                <p className="text-sm font-medium text-red-900 dark:text-red-100">
                                    Uwaga! Akcja nie może być cofnięta
                                </p>
                                <p className="text-xs text-red-700 dark:text-red-300 mt-1">
                                    Usunięcie konta spowoduje trwałą utratę wszystkich danych i dostępu do aplikacji.
                                </p>
                            </div>
                        </div>

                        <PasswordInput
                            error={form.formState.errors.password?.message}
                            id="password"
                        >
                            <Input
                                id="password"
                                type="password"
                                placeholder="••••••••"
                                {...form.register('password')}
                                className="pl-10"
                            />
                        </PasswordInput>

                        <TextInput
                            error={form.formState.errors.confirmation?.message}
                            id="confirmation"
                            label='Wpisz "USUŃ MOJE KONTO" aby potwierdzić'
                        >
                            <Input
                                id="confirmation"
                                type="text"
                                placeholder="USUŃ MOJE KONTO"
                                {...form.register('confirmation')}
                                className="pl-10 font-mono text-sm"
                            />
                        </TextInput>

                        <Button
                            animated
                            type="submit"
                            className="w-full flex items-center justify-center gap-2"
                            disabled={isSubmitted}
                            variant="destructive"
                            loading={isSubmitted}
                        >
                            {isSubmitted ? (
                                <LoaderCircle className="animate-spin h-5 w-5" />
                            ) : (
                                <>
                                    <FaTrashAlt className="h-5 w-5" />
                                    Usuń konto
                                </>
                            )}
                        </Button>

                        <p className="text-xs text-gray-500 dark:text-gray-400 text-center">
                            Jeśli masz pytania, skontaktuj się z naszym zespołem wsparcia przed usunięciem konta.
                        </p>

                    </div>
                </DashboardSection>
            </form>
        </ContentLayout>
    );
}
