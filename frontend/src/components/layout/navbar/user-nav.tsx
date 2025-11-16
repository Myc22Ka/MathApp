'use client';

import Link from 'next/link';
import { LayoutGrid, LogOut, Flame, TrendingUp } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { Tooltip, TooltipContent, TooltipTrigger, TooltipProvider } from '@/components/ui/tooltip';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { useAuth } from '@/components/providers/auth-provider';
import { Badge } from '@/components/ui/badge';
import { roleStyles } from '@/utils/role-style';
import { CustomAvatar } from '@/components/custom/custom-avatar';

const items = [
    {
        icon: LayoutGrid,
        link: '/dashboard',
        text: 'Dashboard',
    },
];

export function UserNav() {
    const { user, logout } = useAuth();

    return (
        <DropdownMenu>
            <TooltipProvider disableHoverableContent>
                <Tooltip delayDuration={100}>
                    <TooltipTrigger asChild>
                        <DropdownMenuTrigger asChild>
                            <Button variant="outline" className="relative h-8 w-8 rounded-full">
                                <CustomAvatar className="h-8 w-8" />
                            </Button>
                        </DropdownMenuTrigger>
                    </TooltipTrigger>
                    <TooltipContent side="bottom">Profile</TooltipContent>
                </Tooltip>
            </TooltipProvider>

            <DropdownMenuContent className="w-64" align="end" forceMount>
                <DropdownMenuLabel className="font-normal px-2 py-3">
                    <div className="flex items-start gap-3">
                        <CustomAvatar className="h-12 w-12" />
                        <div className="flex-1 space-y-2">
                            <p className="text-sm font-semibold leading-none">{user?.login}</p>
                            <p className="text-xs leading-none text-muted-foreground">{user?.email}</p>

                            <div className="flex items-center gap-2 pt-1">
                                <Badge variant="secondary" className="text-xs">
                                    Level {user?.level}
                                </Badge>
                                <Badge className={roleStyles[user?.role || 'STUDENT']} variant="default">
                                    {user?.role}
                                </Badge>
                            </div>
                        </div>
                    </div>
                </DropdownMenuLabel>

                <DropdownMenuSeparator />

                <div className="px-2 py-2 space-y-2">
                    <div className="flex items-center justify-between rounded-md bg-muted py-2 px-3">
                        <div className="flex items-center gap-2">
                            <Flame className="w-4 h-4 text-orange-500" />
                            <span className="text-xs font-medium">Seria</span>
                        </div>
                        <span className="text-sm font-bold">{user?.dailyExercise.streak} dni</span>
                    </div>
                    <div className="flex items-center justify-between rounded-md bg-muted py-2 px-3">
                        <div className="flex items-center gap-2">
                            <TrendingUp className="w-4 h-4 text-emerald-500" />
                            <span className="text-xs font-medium">Punkty</span>
                        </div>
                        <span className="text-sm font-bold">{user?.points ?? 0} XP</span>
                    </div>
                </div>

                <DropdownMenuSeparator />

                <DropdownMenuGroup>
                    {items.map((item, key) => {
                        return (
                            <DropdownMenuItem className="hover:cursor-pointer" asChild key={key}>
                                <Link href={item.link} className="flex items-center">
                                    <item.icon className="w-4 h-4 mr-3 text-muted-foreground" />
                                    {item.text}
                                </Link>
                            </DropdownMenuItem>
                        );
                    })}
                </DropdownMenuGroup>

                <DropdownMenuSeparator />

                <DropdownMenuItem className="hover:cursor-pointer text-destructive" onClick={() => logout()}>
                    <LogOut className="w-4 h-4 mr-3" />
                    Wyloguj się
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}
