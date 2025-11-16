'use client';

import { Button } from '@/components/ui/button';
import { LoaderCircle, Shield } from 'lucide-react';
import { useAuth } from '@/components/providers/auth-provider';
import { UseFormReturn } from 'react-hook-form';
import { ProfileFormData } from '@/lib/zod';
import DashboardSection from './dashboard-section';

interface TwoFactorSectionProps {
    form: UseFormReturn<ProfileFormData>;
}

export function TwoFactorSection({ form }: TwoFactorSectionProps) {
    const { isLoading } = useAuth();
    const twoFactorEnabled = form.watch('twoFactorAuthEnabled');

    const handleToggle = () => {
        const newValue = !twoFactorEnabled;
        form.setValue('twoFactorAuthEnabled', newValue, { shouldDirty: true });
    };

    return (
        <DashboardSection
            icon={Shield}
            subtitle="Dodatkowa warstwa bezpieczeństwa dla Twojego konta. Po włączeniu będziesz musiał podać kod weryfikacyjny podczas logowania."
            title="Uwierzytelnianie dwuetapowe (2FA)"
        >
            <div>
                <p className="font-medium text-gray-900 dark:text-white">{twoFactorEnabled ? '✓ Włączone' : 'Wyłączone'}</p>
                <p className="text-xs text-gray-600 dark:text-gray-400 mt-1">
                    {twoFactorEnabled ? 'Twoje konto jest chronione uwierzytelnianiem dwuetapowym' : 'Włącz 2FA, aby zwiększyć bezpieczeństwo'}
                </p>
            </div>

            <Button
                animated
                type="button"
                onClick={handleToggle}
                disabled={isLoading}
                variant={twoFactorEnabled ? 'destructive' : 'main'}
                className="flex items-center gap-2"
            >
                {isLoading ? <LoaderCircle className="animate-spin h-4 w-4" /> : twoFactorEnabled ? 'Wyłącz' : 'Włącz'}
            </Button>
        </DashboardSection>
    );
}
