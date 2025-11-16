'use client';

import { ChevronDown, LucideIcon } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible';
import { cn } from '@/lib/utils';
import { SubmenuList } from './submenu-list';

export interface Submenu {
    href: string;
    label: string;
    active?: boolean;
}

export interface ExpandedMenuViewProps {
    icon: LucideIcon;
    label: string;
    active: boolean;
    submenus: Submenu[];
    isOpen: boolean | undefined;
    isCollapsed: boolean;
    onCollapsedChange: (state: boolean) => void;
    isSubmenuActive: boolean;
}

export function ExpandedMenuView({ icon: Icon, label, submenus, isCollapsed, onCollapsedChange, isSubmenuActive }: ExpandedMenuViewProps) {
    return (
        <Collapsible open={isCollapsed} onOpenChange={onCollapsedChange} className="w-full">
            <Button
                variant={isSubmenuActive ? 'ghostMain' : 'menu'}
                className="w-full justify-start h-10 mb-1 [&[data-state=open]>div>div>svg]:rotate-180"
                asChild
            >
                <CollapsibleTrigger>
                    <div className="w-full items-center flex justify-between dark:text-white text-black">
                        <div className="flex items-center">
                            <span className="mr-4">
                                <Icon size={18} />
                            </span>
                            <p className={cn('max-w-[150px] truncate')}>{label}</p>
                        </div>
                        <div className="whitespace-nowrap">
                            <ChevronDown size={18} className="transition-transform duration-200" />
                        </div>
                    </div>
                </CollapsibleTrigger>
            </Button>
            <CollapsibleContent className="overflow-hidden data-[state=closed]:animate-collapsible-up data-[state=open]:animate-collapsible-down">
                <SubmenuList submenus={submenus} isOpen={true} />
            </CollapsibleContent>
        </Collapsible>
    );
}
