'use client';

import { useRef } from 'react';
import { Button } from '@/components/ui/button';
import { Upload, Download, Trash2 } from 'lucide-react';
import { useAuth } from '@/components/providers/auth-provider';
import DashboardSection from './dashboard-section';
import { CustomAvatar } from '@/components/custom/custom-avatar';

export function ProfileImageSection() {
    const { uploadProfileImage, user, deleteProfileImage, downloadProfileImage } = useAuth();
    const fileInputRef = useRef<HTMLInputElement>(null);
    const hasImage = !!user?.profilePhotoUrl;

    const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;
        await uploadProfileImage(file);
        if (fileInputRef.current) fileInputRef.current.value = '';
    };

    const handleChangeClick = () => fileInputRef.current?.click();

    const handleDelete = async () => {
        if (!window.confirm('Na pewno chcesz usunąć zdjęcie profilowe?')) return;
        await deleteProfileImage();
    };

    const handleDownload = async () => {
        await downloadProfileImage();
    };

    return (
        <DashboardSection icon={Upload} title="Zdjęcie profilowe">
            <div className="space-y-6 w-full">
                <div className="flex justify-center">
                    <CustomAvatar className="relative w-32 h-32 bg-slate-200 dark:bg-slate-800 text-3xl rounded-2xl overflow-hidden ring-2 ring-slate-200 dark:ring-slate-700" />
                </div>

                <input ref={fileInputRef} type="file" accept="image/*" onChange={handleImageUpload} className="hidden" />

                <div className="space-y-3">
                    <Button animated variant="main" onClick={handleChangeClick} className="w-full">
                        <Upload className="h-4 w-4" />
                        <span>Zmień zdjęcie</span>
                    </Button>

                    <div className="grid grid-cols-2 gap-2">
                        <Button
                            animated
                            variant="outline"
                            disabled={!hasImage}
                            loadingText="Pobieranie..."
                            onClick={handleDownload}
                            className="w-full flex items-center justify-center gap-1"
                        >
                            <Download className="h-4 w-4" />
                            <span className="hidden sm:inline">Pobierz</span>
                        </Button>

                        <Button
                            animated
                            variant="destructive"
                            disabled={!hasImage}
                            loadingText="Usuwanie..."
                            onClick={handleDelete}
                            className="w-full flex items-center justify-center gap-1"
                        >
                            <Trash2 className="h-4 w-4" />
                            <span className="hidden sm:inline">Usuń</span>
                        </Button>
                    </div>

                    <p className="text-xs text-slate-500 dark:text-slate-400">Obsługiwane formaty: JPG, PNG, GIF. Maksymalnie 5MB.</p>
                </div>
            </div>
        </DashboardSection>
    );
}
