import { useCallback, useEffect, useState } from 'react';
import { get, post, getApiErrorMessage, ApiDefaultResponse } from '@/lib/axios';
import { ExerciseDTO } from '@/types/Exercises';

export function useDailyExercise() {
    const [exercise, setExercise] = useState<ExerciseDTO | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchDaily = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await get<ExerciseDTO>('/api/exercises/daily');
            setExercise(data);
        } catch (err) {
            setError(getApiErrorMessage(err));
        } finally {
            setLoading(false);
        }
    }, []);

    const solveDaily = useCallback(async (answer: string): Promise<ApiDefaultResponse | null> => {
        setLoading(true);
        setError(null);

        try {
            const data = await post<ApiDefaultResponse>('/api/exercises/solve-daily', { answer });
            return data;
        } catch (err) {
            setError(getApiErrorMessage(err));
            return null;
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchDaily();
    }, [fetchDaily]);

    return {
        exercise,
        loading,
        error,
        solveDaily,
        refetch: fetchDaily,
    };
}
