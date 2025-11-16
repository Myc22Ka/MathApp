'use client';

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { useAuth } from '@/components/providers/auth-provider';
import { getUserInitials } from '@/utils/utils';

interface CustomAvatarProps {
    className?: string;
}

export function CustomAvatar({ className }: CustomAvatarProps) {
    const { user } = useAuth();

    const currentImage = user?.profilePhotoUrl;
    const hasImage = !!currentImage && currentImage !== '';

    return (
        <Avatar className={className}>
            {hasImage && <AvatarImage src={currentImage} alt="Avatar" />}
            <AvatarFallback className="bg-transparent">{getUserInitials(user)}</AvatarFallback>
        </Avatar>
    );
}
