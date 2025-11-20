import { ModeToggle } from '@/components/mode-toggle';
import { UserNav } from '@/components/layout/navbar/user-nav';
import { SheetMenu } from '@/components/admin/sheet-menu';
import { StreakBadge } from './streak-badge';
import { NavLinks } from './navlinks';
import { useAuth } from '@/components/providers/auth-provider';
import { Button } from '@/components/ui/button';
import Link from 'next/link';

interface NavbarProps {
    title: string;
}

export function Navbar({ title }: NavbarProps) {
    const { isAuthenticated } = useAuth();

    return (
        <header className="sticky top-0 z-10 w-full bg-background/95 shadow backdrop-blur supports-backdrop-filter:bg-background/60 dark:shadow-secondary">
            <div className="mx-4 sm:mx-8 flex h-14 items-center justify-between">
                <div className="flex items-center space-x-4 lg:space-x-0">
                    <SheetMenu />
                    <h1 className="font-bold">{title}</h1>
                </div>

                <div className="hidden sm:flex items-center space-x-4 text-sm text-muted-foreground">
                    <NavLinks />

                    <div className="flex items-center">

                        {isAuthenticated && <StreakBadge />}
                        <ModeToggle />
                        {isAuthenticated && <UserNav />}

                        {!isAuthenticated && (
                            <div className="flex items-center space-x-2">
                                <Link href="/auth/login">
                                    <Button className="px-3 py-1 border rounded hover:bg-background/20 transition-colors" variant="main">
                                        Zaloguj
                                    </Button>
                                </Link>
                                <Link href="/auth/register">
                                    <Button className="px-3 py-1 border rounded hover:bg-background/20 transition-colors" variant="outline">
                                        Zarejestruj
                                    </Button>
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </header >
    );
}
