'use client';

import { TooltipProvider } from '@radix-ui/react-tooltip';
import { Tooltip, TooltipContent, TooltipTrigger } from '../ui/tooltip';

interface MenuTooltipProps {
    content: React.ReactNode;
    children: React.ReactNode;
    side?: 'right' | 'top' | 'bottom' | 'left';
    delay?: number;
}

export default function MenuTooltip({ content, children, side = 'right', delay = 100 }: MenuTooltipProps) {
    return (
        <TooltipProvider disableHoverableContent>
            <Tooltip delayDuration={delay}>
                <TooltipTrigger asChild>{children}</TooltipTrigger>
                <TooltipContent side={side}>{content}</TooltipContent>
            </Tooltip>
        </TooltipProvider>
    );
}
