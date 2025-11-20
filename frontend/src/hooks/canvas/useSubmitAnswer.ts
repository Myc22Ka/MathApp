import { useState, useCallback } from 'react';

interface SubmitResponse {
    ok: boolean;
    status: number;
}

export const useSubmitAnswer = () => {
    const [submittedAnswer, setSubmittedAnswer] = useState<string>('');
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const submitAnswer = useCallback(async (answer: string): Promise<boolean> => {
        if (!answer.trim()) {
            return false;
        }

        setIsSubmitting(true);
        try {
            const response: SubmitResponse = (await fetch('/api/submit-answer', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    answer: answer,
                    timestamp: new Date().toISOString(),
                }),
            })) as unknown as SubmitResponse;

            if (response.ok) {
                setSubmittedAnswer(answer);
                return true;
            } else {
                return false;
            }
        } catch (error: unknown) {
            console.error('Błąd przy wysyłaniu:', error);
            // Fallback: przyjmij odpowiedź nawet jeśli błąd
            setSubmittedAnswer(answer);
            return true;
        } finally {
            setIsSubmitting(false);
        }
    }, []);

    return {
        submittedAnswer,
        isSubmitting,
        submitAnswer,
    };
};
