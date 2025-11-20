import { useCallback } from 'react';

export const useSaveCanvas = () => {
    const saveWriteArea = useCallback(async (imageData: string): Promise<boolean> => {
        try {
            // Tutaj możesz wysłać dane na backend
            console.log('Zapisana część pisania:', imageData);
            return true;
        } catch (error: unknown) {
            console.error('Błąd przy zapisywaniu tablicy:', error);
            return false;
        }
    }, []);

    return {
        saveWriteArea,
    };
};
