export interface ExerciseDTO {
    id: number;
    text: string;
    verified: boolean;
    rating: number | null;
    topic: string | null;
    isSolved: boolean | null;
    requiredLevel: number | null;
}
