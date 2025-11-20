"use client";

import { CanvasSection } from '@/components/canvas/CanvasSection';
import { InstructionCard } from '@/components/canvas/InstructionCard';
import { MathInput } from '@/components/canvas/MathInput';
import { TaskCard } from '@/components/canvas/TaskCard';
import { ContentLayout } from '@/components/layout/admin-panel/content-layout/content-layout';
import { useAuth } from '@/components/providers/auth-provider';
import { useDailyExercise } from '@/hooks/api/useDailyExercise';
import { useCanvas } from '@/hooks/canvas/useCanvas';
import { useSaveCanvas } from '@/hooks/canvas/useSaveCanvas';
import { getApiErrorMessage } from '@/lib/axios';
import React, { ReactNode, useState } from 'react';
import { toast } from 'sonner';

export default function MathSolver(): ReactNode {
    const canvas = useCanvas();
    const { saveWriteArea } = useSaveCanvas();
    const { exercise, solveDaily, refetch } = useDailyExercise();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [answer, setAnswer] = useState('');
    const { refreshUser } = useAuth();

    const handleSaveWriteArea = async (): Promise<void> => {
        const imageData = canvas.getCanvasImage();
        const success = await saveWriteArea(imageData);

        if (success) {
            toast.success('Obszar zapisany pomyślnie!');
        } else {
            toast.error('Nie udało się zapisać obszaru.');
        }
    };

    const handleSubmitAnswer = async (): Promise<void> => {
        if (!answer.trim()) {
            toast('Nie wprowadzono odpowiedzi.');
            return;
        }

        setIsSubmitting(true);
        try {
            const response = await solveDaily(answer);

            if (response?.status === 200) {
                toast.success(response.message);

                await refreshUser();
                refetch();
            } else {
                toast.error(response?.message);
            }
        } catch (error) {
            toast.error(getApiErrorMessage(error));
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <ContentLayout title="Dzienne wyzwanie">
            <div className="w-full">
                <TaskCard exercise={exercise} />

                <CanvasSection
                    canvasRef={canvas.canvasRef as React.RefObject<HTMLCanvasElement>}
                    containerRef={canvas.containerRef as React.RefObject<HTMLDivElement>}
                    onMouseDown={canvas.handleMouseDown}
                    onMouseMove={canvas.handleMouseMove}
                    onMouseUp={canvas.handleMouseUp}
                    onClearAll={canvas.clearCanvas}
                    onSaveWrite={handleSaveWriteArea}
                    width={700}
                    height={650}
                />

                <MathInput
                    value={answer}
                    onChange={setAnswer}
                    placeholder="Wpisz odpowiedź lub użyj przycisków"
                    isSubmitting={isSubmitting}
                    handleSubmitAnswer={handleSubmitAnswer}
                />

                <InstructionCard />
            </div>
        </ContentLayout>
    );
}
