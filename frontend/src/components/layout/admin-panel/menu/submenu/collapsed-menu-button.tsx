'use client';

import { useState } from 'react';
import { usePathname } from 'next/navigation';
import { LucideIcon } from 'lucide-react';
import { ExpandedMenuView } from './expanded-menu-view';
import { CollapsedMenuView } from './collapsed-menu-view';

export type Submenu = {
    href: string;
    label: string;
    active?: boolean;
};

export interface CollapseMenuButtonProps {
    icon: LucideIcon;
    label: string;
    active: boolean;
    submenus: Submenu[];
    isOpen: boolean | undefined;
}

export function CollapseMenuButton({ icon, label, active, submenus, isOpen }: CollapseMenuButtonProps) {
    const pathname = usePathname();
    const isSubmenuActive = submenus.some(submenu => (submenu.active === undefined ? submenu.href === pathname : submenu.active));
    const [isCollapsed, setIsCollapsed] = useState<boolean>(isSubmenuActive);

    return isOpen ? (
        <ExpandedMenuView
            icon={icon}
            label={label}
            active={active}
            submenus={submenus}
            isOpen={isOpen}
            isCollapsed={isCollapsed}
            onCollapsedChange={setIsCollapsed}
            isSubmenuActive={isSubmenuActive}
        />
    ) : (
        <CollapsedMenuView icon={icon} label={label} active={active} submenus={submenus} isOpen={isOpen} isSubmenuActive={isSubmenuActive} />
    );
}
