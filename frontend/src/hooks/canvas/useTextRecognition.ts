import { useState, useCallback } from 'react';

interface TesseractWindow extends Window {
    Tesseract?: {
        recognize: (
            image: string,
            lang: string,
            options: Record<string, unknown>
        ) => Promise<{
            data: {
                text: string;
            };
        }>;
    };
}

export const useTextRecognition = () => {
    const [recognizedAnswer, setRecognizedAnswer] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const loadTesseractScript = (): Promise<void> => {
        return new Promise((resolve, reject) => {
            const tesseractWindow = window as TesseractWindow;
            if (tesseractWindow.Tesseract) {
                resolve();
                return;
            }

            const script = document.createElement('script');
            script.src = 'https://cdnjs.cloudflare.com/ajax/libs/tesseract.js/4.1.1/tesseract.min.js';

            script.onload = () => resolve();
            script.onerror = () => reject(new Error('Nie udało się załadować Tesseract.js'));

            document.head.appendChild(script);
        });
    };

    const recognizeText = useCallback(async (imageData: string): Promise<string> => {
        setIsLoading(true);
        try {
            await loadTesseractScript();

            const tesseractWindow = window as TesseractWindow;
            if (!tesseractWindow.Tesseract) {
                throw new Error('Tesseract nie jest dostępny');
            }

            const result = await tesseractWindow.Tesseract.recognize(imageData, 'eng', {
                logger: console.log,
                tessedit_char_whitelist: '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ∅{}(),∪∩',
                psm: 6,
            });

            const text: string = result.data.text.trim();
            setRecognizedAnswer(text);
            return text;
        } catch (error: unknown) {
            console.error('Błąd przy rozpoznawaniu tekstu:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    }, []);

    const clearAnswer = (): void => {
        setRecognizedAnswer('');
    };

    return {
        recognizedAnswer,
        isLoading,
        recognizeText,
        clearAnswer,
        setRecognizedAnswer,
    };
};
