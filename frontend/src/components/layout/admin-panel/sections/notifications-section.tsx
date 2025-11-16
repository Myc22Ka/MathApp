'use client';

import { Bell } from 'lucide-react';
import { UseFormReturn } from 'react-hook-form';
import { ProfileFormData } from '@/lib/zod';
import DashboardSection from './dashboard-section';
import { Switch } from '@/components/ui/switch';

interface NotificationsSectionProps {
    form: UseFormReturn<ProfileFormData>;
}

export function NotificationsSection({ form }: NotificationsSectionProps) {
    const isNotificationsEnabled = form.watch('notificationsEnabled');

    return (
        <DashboardSection title="Powiadomienia" icon={Bell}>
            <div>
                <p className="text-sm font-semibold text-zinc-900 dark:text-zinc-100">Powiadomienia email</p>
                <p className="text-xs text-zinc-600 dark:text-zinc-400 mt-1">Otrzymuj aktualizacje o Twojej aktywności i postępach</p>
            </div>

            <Switch
                checked={isNotificationsEnabled}
                onCheckedChange={checked => form.setValue('notificationsEnabled', checked, { shouldDirty: true })}
                className="data-[state=checked]:bg-(--main) dark:data-[state=checked]:bg-(--main) data-[state=unchecked]:bg-zinc-300 dark:data-[state=unchecked]:bg-zinc-700"
            />
        </DashboardSection>
    );
}
