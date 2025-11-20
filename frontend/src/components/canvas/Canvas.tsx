import React from 'react';

export interface CanvasProps {
    canvasRef: React.RefObject<HTMLCanvasElement>;
    containerRef: React.RefObject<HTMLDivElement>;
    onMouseDown: (e: React.MouseEvent<HTMLCanvasElement>) => void;
    onMouseMove: (e: React.MouseEvent<HTMLCanvasElement>) => void;
    onMouseUp: () => void;
    width?: number;
    height?: number;
}

export const Canvas: React.FC<CanvasProps> = ({
    canvasRef,
    containerRef,
    onMouseDown,
    onMouseMove,
    onMouseUp,
    width = 700,
    height = 650,
}) => (
    <div
        ref={containerRef}
        className="rounded-lg overflow-hidden"
        style={{ display: 'inline-block', width: '100%' }}
    >
        <canvas
            ref={canvasRef}
            width={width}
            height={height}
            onMouseDown={onMouseDown}
            onMouseMove={onMouseMove}
            onMouseUp={onMouseUp}
            onMouseLeave={onMouseUp}
            className="w-full cursor-crosshair block"
            style={{ touchAction: 'none' }}
        />
    </div>
);