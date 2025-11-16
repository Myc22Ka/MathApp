import Link from 'next/link';
import {
    DropdownMenu,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger,
    DropdownMenuContent,
    DropdownMenuSeparator,
} from '@/components/ui/dropdown-menu';
import { DropdownMenuArrow } from '@radix-ui/react-dropdown-menu';
import { Button } from '@/components/ui/button';
import { Tooltip, TooltipTrigger, TooltipContent, TooltipProvider } from '@/components/ui/tooltip';
import { cn } from '@/lib/utils';
import { usePathname } from 'next/navigation';
import { LucideIcon } from 'lucide-react';

export interface Submenu {
    href: string;
    label: string;
    active?: boolean;
}

export interface CollapsedMenuViewProps {
    icon: LucideIcon;
    label: string;
    active: boolean;
    submenus: Submenu[];
    isOpen: boolean | undefined;
    isSubmenuActive: boolean;
}

export function CollapsedMenuView({ icon: Icon, label, submenus, isSubmenuActive }: CollapsedMenuViewProps) {
    const pathname = usePathname();

    return (
        <DropdownMenu>
            <TooltipProvider disableHoverableContent>
                <Tooltip delayDuration={100}>
                    <TooltipTrigger asChild>
                        <DropdownMenuTrigger asChild>
                            <Button variant={isSubmenuActive ? 'ghostMain' : 'menu'} className="w-full justify-start h-10 mb-1">
                                <div className="w-full items-center flex justify-between">
                                    <div className="flex items-center">
                                        <span>
                                            <Icon size={18} />
                                        </span>
                                    </div>
                                </div>
                            </Button>
                        </DropdownMenuTrigger>
                    </TooltipTrigger>
                    <TooltipContent side="right" align="start" alignOffset={2}>
                        {label}
                    </TooltipContent>
                </Tooltip>
            </TooltipProvider>
            <DropdownMenuContent side="right" sideOffset={25} align="start">
                <DropdownMenuLabel className="max-w-[190px] truncate">{label}</DropdownMenuLabel>
                <DropdownMenuSeparator />
                {submenus.map(({ href, label: submenuLabel, active }, index) => (
                    <DropdownMenuItem key={index} asChild>
                        <Link className={cn('cursor-pointer', ((active === undefined && pathname === href) || active) && 'bg-secondary')} href={href}>
                            <p className="max-w-[180px] truncate">{submenuLabel}</p>
                        </Link>
                    </DropdownMenuItem>
                ))}
                <DropdownMenuArrow className="fill-border" />
            </DropdownMenuContent>
        </DropdownMenu>
    );
}
