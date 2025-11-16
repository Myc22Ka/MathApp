export type DailyExercise = {
    dailyTasksCompleted: number;
    isSolved: boolean;
    lastDailyTaskDate: Date | null;
    streak: number;
};

export type Gender = 'MALE' | 'FEMALE' | 'OTHER';

export type UserRole = 'ADMIN' | 'STUDENT' | 'TEACHER' | 'MODERATOR' | 'GUEST';

export type User = {
    id: number;
    login: string;
    firstname: string | null;
    lastname: string | null;
    email: string;
    role: UserRole;
    points: number;
    level: number;
    phoneNumber: string | null;
    address: string | null;
    dailyExercise: DailyExercise;
    dateOfBirth: string | null;
    gender: Gender | null;
    profilePhotoUrl: string | null;
    exerciseImages: { url: string; name: string }[];
    verified: boolean;
    twoFactorEnabled: boolean;
    notificationsEnabled: boolean;
};
