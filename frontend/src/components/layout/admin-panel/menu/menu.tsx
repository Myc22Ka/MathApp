'use client';

import { Ellipsis, LogOut } from 'lucide-react';
import { usePathname } from 'next/navigation';
import { cn } from '@/lib/utils';
import { getMenuList } from '@/lib/menu-list';
import { Button } from '@/components/ui/button';
import { ScrollArea } from '@/components/ui/scroll-area';
import { MenuItem } from './menu-item';
import { useAuth } from '@/components/providers/auth-provider';
import MenuTooltip from '@/components/tooltip/tooltip';

interface MenuProps {
    isOpen: boolean | undefined;
}

export function Menu({ isOpen }: MenuProps) {
    const { user, logout } = useAuth();
    const pathname = usePathname();
    const menuList = getMenuList(user?.role);

    return (
        <ScrollArea className="[&>div>div[style]]:block!">
            <nav className="mt-8 h-full w-full">
                <ul className="flex flex-col list-none min-h-[calc(100vh-48px-36px-16px-32px)] lg:min-h-[calc(100vh-32px-40px-32px)] items-start space-y-1 px-2">
                    {menuList.map(({ groupLabel, menus }, index) => (
                        <li className={cn('w-full', groupLabel ? 'pt-5' : '')} key={index}>
                            {groupLabel &&
                                (isOpen || isOpen === undefined ? (
                                    <p className="text-xs font-semibold text-muted-foreground px-4 pb-2 max-w-[248px] truncate uppercase tracking-wider">
                                        {groupLabel}
                                    </p>
                                ) : (
                                    <MenuTooltip content={groupLabel}>
                                        <div className="w-full flex justify-center items-center">
                                            <Ellipsis className="h-5 w-5" />
                                        </div>
                                    </MenuTooltip>
                                ))}

                            {menus.map(({ href, label, icon, active, submenus }, idx) => {
                                const isActive = (active === undefined && pathname.endsWith(href)) ?? active;
                                return <MenuItem key={idx} href={href} label={label} icon={icon} isActive={isActive} isOpen={isOpen} submenus={submenus} />;
                            })}
                        </li>
                    ))}

                    <li className="w-full grow flex items-end">
                        <MenuTooltip content="Sign out">
                            <Button onClick={logout} variant="outline" className="w-full justify-center h-10 mt-5">
                                <span className={cn(isOpen === false ? '' : 'mr-4')}>
                                    <LogOut size={18} />
                                </span>
                                <p className={cn('whitespace-nowrap transition-all', isOpen === false ? 'opacity-0 hidden' : 'opacity-100')}>Sign out</p>
                            </Button>
                        </MenuTooltip>
                    </li>
                </ul>
            </nav>
        </ScrollArea>
    );
}
