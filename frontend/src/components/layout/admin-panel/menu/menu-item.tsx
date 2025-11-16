'use client';

import Link from 'next/link';
import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import MenuTooltip from '@/components/tooltip/tooltip';
import { LucideIcon } from 'lucide-react';
import { Submenu } from '@/lib/menu-list';
import { CollapseMenuButton } from './submenu/collapsed-menu-button';

interface MenuItemProps {
    href: string;
    label: string;
    icon: LucideIcon;
    isActive: boolean;
    isOpen?: boolean;
    submenus?: Submenu[];
}

export function MenuItem({ href, label, icon: Icon, isActive, isOpen = true, submenus }: MenuItemProps) {
    if (submenus && submenus.length > 0) {
        return (
            <CollapseMenuButton
                icon={Icon}
                label={label}
                active={isActive}
                submenus={submenus.map(sm => ({
                    href: sm.href,
                    label: sm.label,
                    active: sm.active,
                }))}
                isOpen={isOpen}
            />
        );
    }

    return (
        <MenuTooltip content={label}>
            <Button
                variant={isActive ? 'ghostMain' : 'menu'}
                className={cn('w-full justify-start h-10 mb-1 transition-colors', isActive && 'bg-(--main-accent)/10 font-semibold text-(--main)')}
                asChild
            >
                <Link href={href}>
                    <span className={cn(isOpen === false ? '' : 'mr-4')}>
                        <Icon />
                    </span>
                    <p className={cn('max-w-[200px] truncate transition-all', isOpen === false ? '-translate-x-96 opacity-0' : 'translate-x-0 opacity-100')}>
                        {label}
                    </p>
                </Link>
            </Button>
        </MenuTooltip>
    );
}
