import { UserRole } from '@/types/User';
import { Tag, Users, Settings, Bookmark, SquarePen, LayoutGrid, LucideIcon, Shield, Wrench } from 'lucide-react';

export type Submenu = {
    href: string;
    label: string;
    active?: boolean;
};

type Menu = {
    href: string;
    label: string;
    active?: boolean;
    icon: LucideIcon;
    submenus?: Submenu[];
};

type Group = {
    groupLabel: string;
    menus: Menu[];
};

export function getMenuList(role: UserRole = 'STUDENT'): Group[] {
    const baseMenu: Group[] = [
        {
            groupLabel: '',
            menus: [
                {
                    href: '/dashboard',
                    label: 'Dashboard',
                    icon: LayoutGrid,
                    submenus: [],
                },
            ],
        },
    ];

    // Menu dla wszystkich ról
    const contentMenu: Group = {
        groupLabel: 'Exercises',
        menus: [
            {
                href: '',
                label: 'Exercises',
                icon: SquarePen,
                submenus: [
                    {
                        href: '/dashboard/exercises/daily',
                        label: 'Daily',
                    },
                    ...(role !== 'STUDENT' ? [{ href: '/posts/new', label: 'New Post' }] : []),
                ],
            },
        ],
    };

    if (role === 'ADMIN' || role === 'TEACHER') {
        contentMenu.menus.push(
            {
                href: '/categories',
                label: 'Categories',
                icon: Bookmark,
            },
            {
                href: '/tags',
                label: 'Tags',
                icon: Tag,
            }
        );
    }

    baseMenu.push(contentMenu);

    // Menu Settings
    const settingsMenu: Group = {
        groupLabel: 'Settings',
        menus: [
            {
                href: '/dashboard/account',
                label: 'Account',
                icon: Settings,
                submenus: [
                    { href: '/dashboard/account/edit', label: 'Edit Account' },
                    { href: '/dashboard/account/change-password', label: 'Change Password' },
                    { href: '/dashboard/account/delete', label: 'Delete Account' },
                ],
            },
        ],
    };

    // Zarządzanie użytkownikami
    if (role === 'ADMIN') {
        settingsMenu.menus.unshift({
            href: '/users',
            label: 'Users',
            icon: Users,
        });
    }

    baseMenu.push(settingsMenu);

    // Panel administracyjny
    if (role === 'ADMIN') {
        baseMenu.push({
            groupLabel: 'Administration',
            menus: [
                {
                    href: '/admin/permissions',
                    label: 'Permissions',
                    icon: Shield,
                },
                {
                    href: '/admin/system',
                    label: 'System Settings',
                    icon: Wrench,
                },
            ],
        });
    }

    return baseMenu;
}
