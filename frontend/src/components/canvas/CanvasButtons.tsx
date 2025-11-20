import React from 'react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import { Save, Trash2 } from 'lucide-react';

export interface CanvasButtonsProps {
    onClearAll: () => void;
    onSaveWrite: () => void;
}

export const CanvasButtons: React.FC<CanvasButtonsProps> = ({
    onClearAll,
    onSaveWrite,
}) => (
    <div className="grid grid-cols-2 gap-2 mt-4">

        <Button onClick={onClearAll} variant="destructive" className="h-10 justify-center">
            <Trash2 size={18} />
            <p className={cn('whitespace-nowrap transition-all')}>
                Wyczyść wszystko
            </p>
        </Button>

        <Button onClick={onSaveWrite} variant="main" className="h-10 justify-center">
            <Save size={18} />
            <p className={cn('whitespace-nowrap transition-all')}>
                Zapisz tablicę
            </p>
        </Button>
    </div>
);