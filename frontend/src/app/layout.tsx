import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import '../styles/globals.css';
import { ContextWrapper } from '@/components/auth/layout/wrappers/ContextWrapper';
import { ThemeProvider } from '@/components/providers/theme-provider';
import { Toaster } from 'sonner';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
    title: 'Twoja aplikacja',
    description: 'Strona logowania i inne',
};

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="pl" suppressHydrationWarning>
            <body className={inter.className}>
                <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
                    <ContextWrapper>{children}</ContextWrapper>
                </ThemeProvider>
                <Toaster />
            </body>
        </html>
    );
}
