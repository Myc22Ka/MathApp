import { useState, useCallback } from 'react';
import { Message } from '@/types/Messages';
import { get, getApiErrorMessage } from '@/lib/axios';

interface UseChatMessageReturn {
    sendMessage: (prompt: string) => Promise<void>;
    isLoading: boolean;
    error: string | null;
}

export const useChatMessage = (onMessageReceived: (newMessage: Message) => void, messageId: string): UseChatMessageReturn => {
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const sendMessage = useCallback(
        async (prompt: string): Promise<void> => {
            if (!prompt.trim()) {
                setError('Wiadomość nie może być pusta');
                return;
            }

            setIsLoading(true);
            setError(null);

            try {
                const response = await get<string>('/ollama/chat', { prompt });

                const aiResponse = typeof response === 'string' ? response : String(response);

                const newMessage: Message = {
                    id: messageId,
                    question: prompt,
                    response: aiResponse,
                    createdAt: new Date(),
                };

                onMessageReceived(newMessage);
            } catch (err) {
                const errorMessage = getApiErrorMessage(err);
                setError(errorMessage);
                console.error('Chat error:', err);
            } finally {
                setIsLoading(false);
            }
        },
        [onMessageReceived, messageId]
    );

    return { sendMessage, isLoading, error };
};
