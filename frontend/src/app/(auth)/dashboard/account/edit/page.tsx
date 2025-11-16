'use client';

import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { ContentLayout } from '@/components/layout/admin-panel/content-layout/content-layout';
import { useAuth } from '@/components/providers/auth-provider';
import { ProfileFormData, profileSchema } from '@/lib/zod';
import { ProfileImageSection } from '@/components/layout/admin-panel/sections/profile-image-section';
import { PersonalDataSection } from '@/components/layout/admin-panel/sections/personal-data-section';
import { NotificationsSection } from '@/components/layout/admin-panel/sections/notifications-section';
import { TwoFactorSection } from '@/components/layout/admin-panel/sections/two-factor-section';
import { Button } from '@/components/ui/button';

export default function ProfileEditorPage() {
    const { user, updateProfile, isSubmitted } = useAuth();

    const form = useForm<ProfileFormData>({
        resolver: zodResolver(profileSchema),
        defaultValues: {
            firstname: undefined,
            lastname: undefined,
            phoneNumber: undefined,
            address: undefined,
            dateOfBirth: undefined,
            gender: 'OTHER',
            notificationsEnabled: true,
        },
    });

    useEffect(() => {
        if (user) {
            form.reset({
                firstname: user.firstname || undefined,
                lastname: user.lastname || undefined,
                phoneNumber: user.phoneNumber || undefined,
                address: user.address || undefined,
                dateOfBirth: user.dateOfBirth || undefined,
                gender: user.gender || 'OTHER',
                notificationsEnabled: user.notificationsEnabled ?? true,
                twoFactorAuthEnabled: user.twoFactorEnabled ?? false,
            });
        }
    }, [form, user]);

    const onSubmitAll = async (data: ProfileFormData) => {
        await updateProfile(data);
    };

    return (
        <ContentLayout title="Edytuj profil">
            <form onSubmit={form.handleSubmit(onSubmitAll)}>
                <div className="max-w-4xl space-y-6">
                    <ProfileImageSection />
                    <PersonalDataSection form={form} />
                    <NotificationsSection form={form} />
                    <TwoFactorSection form={form} />
                    <div className='w-full'>
                        <Button animated type="submit" disabled={isSubmitted} variant="main" className="w-full flex items-center justify-center gap-2 py-3" loading={isSubmitted}>
                            Zapisz wszystkie zmiany
                        </Button>
                    </div>
                </div>
            </form>
        </ContentLayout>
    );
}
