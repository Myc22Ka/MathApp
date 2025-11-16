'use client';

import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import { usePathname } from 'next/navigation';

export interface SubmenuItemProps {
    href: string;
    label: string;
    active?: boolean;
    isOpen: boolean | undefined;
}

export function SubmenuItem({ href, label, active, isOpen }: SubmenuItemProps) {
    const pathname = usePathname();
    const isActive = (active === undefined && pathname === href) || active;

    return (
        <Button variant={isActive ? 'ghostMain' : 'menu'} className="w-full justify-start h-10 mb-1" asChild>
            <Link href={href}>
                <p className={cn('max-w-[170px] truncate', isOpen ? 'translate-x-0 opacity-100' : '-translate-x-96 opacity-0')}>{label}</p>
            </Link>
        </Button>
    );
}
