import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Canvas, CanvasProps } from './Canvas';
import { CanvasButtons, CanvasButtonsProps } from './CanvasButtons';

export interface CanvasSectionProps extends CanvasProps, CanvasButtonsProps { }

export const CanvasSection: React.FC<CanvasSectionProps> = ({
    canvasRef,
    containerRef,
    onMouseDown,
    onMouseMove,
    onMouseUp,
    width,
    height,
    onClearAll,
    onSaveWrite,
}) => (
    <Card className="mb-6 shadow-lg">
        <CardHeader>
            <CardTitle>Brudnopis</CardTitle>
        </CardHeader>
        <CardContent>
            <Canvas
                canvasRef={canvasRef}
                containerRef={containerRef}
                onMouseDown={onMouseDown}
                onMouseMove={onMouseMove}
                onMouseUp={onMouseUp}
                width={width}
                height={height}
            />
            <CanvasButtons
                onClearAll={onClearAll}
                onSaveWrite={onSaveWrite}
            />
        </CardContent>
    </Card>
);