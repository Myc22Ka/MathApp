import React from 'react';
import { ExerciseDTO } from '@/types/Exercises';
import { Badge } from '../ui/badge';
import { Star } from 'lucide-react';

interface TaskCardProps {
    exercise: ExerciseDTO | null;
}

export function TaskCard({ exercise }: TaskCardProps) {
    if (!exercise) {
        return (
            <div className="text-gray-500 dark:text-gray-400 italic">
                Ładuje zadanko…
            </div>
        );
    }

    const { verified, rating, text, requiredLevel, topic } = exercise;

    return (
        <div className="flex flex-col gap-3 mb-4">

            <div className="flex items-center gap-2">
                {verified ? (
                    <Badge className="bg-green-600 dark:text-gray-100">Zweryfikowane</Badge>
                ) : (
                    <Badge variant="outline" className="text-gray-600 border-gray-400 dark:text-gray-100">
                        Niezweryfikowane
                    </Badge>
                )}

                {topic && (
                    <Badge variant="secondary" className="bg-blue-200 dark:bg-blue-900">
                        {topic}
                    </Badge>
                )}

                {exercise.isSolved ? (
                    <Badge className="bg-emerald-600 dark:text-gray-100">Zrobione</Badge>
                ) : (
                    <Badge className="bg-red-600 dark:text-gray-100">Niezrobione</Badge>
                )}
            </div>

            <div className="text-2xl font-semibold text-gray-900 dark:text-gray-100">
                {text}
            </div>

            <div className="flex items-center gap-4 text-sm text-gray-600 dark:text-gray-400">
                {rating !== null && (
                    <div className="flex items-center gap-1">
                        <Star className="w-4 h-4 fill-yellow-500 stroke-yellow-500" />
                        {rating.toFixed(1)}
                    </div>
                )}

                {requiredLevel !== null && (
                    <Badge
                        variant="secondary"
                        className="flex items-center gap-1 bg-purple-200 text-purple-800 dark:bg-purple-900 dark:text-purple-300 px-2 py-1"
                    >
                        <span className="font-bold">{requiredLevel}</span>
                        <span className="text-xs">poziom</span>
                    </Badge>
                )}
            </div>
        </div>
    );
}