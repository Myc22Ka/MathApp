'use client';

import { Button } from '@/components/ui/button';
import { AiOutlineGoogle, AiOutlineGithub } from 'react-icons/ai';

export default function OAuthButtons() {
    const handleOAuthLogin = (provider: string) => {
        const backendUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
        window.location.href = `${backendUrl}/oauth2/authorization/${provider}`;
    };

    const providers = [
        {
            name: 'Google',
            icon: AiOutlineGoogle,
            provider: 'google',
        },
        {
            name: 'GitHub',
            icon: AiOutlineGithub,
            provider: 'github',
        },
    ];

    return (
        <div className="grid grid-cols-2 gap-3">
            {providers.map(({ name, icon: Icon, provider }) => (
                <Button
                    key={provider}
                    variant="outline"
                    size="lg"
                    onClick={() => handleOAuthLogin(provider)}
                    className="flex items-center justify-center gap-2 w-full"
                >
                    <Icon className="h-5 w-5" />
                    {name}
                </Button>
            ))}
        </div>
    );
}
