'use client';

import { Sidebar } from '@/components/layout/admin-panel/menu/sidebar';
import { useSidebar } from '@/components/providers/use-sidebar';
import { cn } from '@/lib/utils';

export default function AdminPanelLayout({ children }: { children: React.ReactNode }) {
    const { settings, isMounted, openState } = useSidebar();

    if (!isMounted) return null;

    return (
        <>
            <Sidebar />
            <main
                className={cn(
                    'min-h-[calc(100vh)] bg-zinc-50 dark:bg-zinc-900 transition-[margin-left] ease-in-out duration-300',
                    !settings.disabled && (!openState ? 'lg:ml-[90px]' : 'lg:ml-72')
                )}
            >
                {children}
            </main>
        </>
    );
}
