'use client';

import { SidebarToggle } from '@/components/admin/sidebar-toggle';
import { useSidebar } from '@/components/providers/use-sidebar';
import { cn } from '@/lib/utils';
import LogoSidebar from '../../../admin/logo-sidebar';
import { Menu } from './menu';

export function Sidebar() {
    const { settings, isMounted, setIsHover, openState, toggleOpen } = useSidebar();

    if (!isMounted) return null;

    return (
        <aside
            className={cn(
                'fixed top-0 left-0 z-20 h-screen -translate-x-full lg:translate-x-0 transition-[width] ease-in-out duration-300',
                !openState ? 'w-[90px]' : 'w-72',
                settings.disabled && 'hidden'
            )}
        >
            <SidebarToggle isOpen={openState} setIsOpen={toggleOpen} />
            <div
                onMouseEnter={() => setIsHover(true)}
                onMouseLeave={() => setIsHover(false)}
                className="relative h-full flex flex-col px-3 py-4 overflow-y-auto shadow-md dark:shadow-zinc-800"
            >
                <LogoSidebar isOpen={openState} />
                <Menu isOpen={openState} />
            </div>
        </aside>
    );
}
