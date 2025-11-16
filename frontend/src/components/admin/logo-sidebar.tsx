import { cn } from '@/lib/utils';
import React from 'react';
import { Button } from '../ui/button';
import Link from 'next/link';
import { PanelsTopLeft } from 'lucide-react';

type LogoSidebarProps = {
    isOpen: boolean | undefined;
};

const LogoSidebar: React.FC<LogoSidebarProps> = ({ isOpen }) => {
    return (
        <Button className={cn('transition-transform ease-in-out duration-300 mb-1', !isOpen ? 'translate-x-1' : 'translate-x-0')} variant="none" asChild>
            <Link href="/dashboard" className="flex items-center gap-2">
                <PanelsTopLeft className="w-6 h-6 mr-1" />
                <h1
                    className={cn(
                        'font-bold text-lg whitespace-nowrap transition-[transform,opacity,display] ease-in-out duration-300',
                        !isOpen ? '-translate-x-96 opacity-0 hidden' : 'translate-x-0 opacity-100'
                    )}
                >
                    MathApp
                </h1>
            </Link>
        </Button>
    );
};

export default LogoSidebar;
