'use client';

import { SubmenuItem } from './submenu-item';

export interface Submenu {
    href: string;
    label: string;
    active?: boolean;
}

export interface SubmenuListProps {
    submenus: Submenu[];
    isOpen: boolean | undefined;
}

export function SubmenuList({ submenus, isOpen }: SubmenuListProps) {
    return (
        <>
            {submenus.map(({ href, label, active }, index) => (
                <SubmenuItem key={index} href={href} label={label} active={active} isOpen={isOpen} />
            ))}
        </>
    );
}
