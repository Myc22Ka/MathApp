'use client';

import SendEmail from '@/components/auth/forgot-password/SendEmail';
import { Suspense } from 'react';

export default function ForgotPasswordRequestPage() {
    return (
        <Suspense fallback={<div>Ładowanie...</div>}>
            <SendEmail />;
        </Suspense>
    );
}
