import { User } from '@/types/User';

export function getUserInitials(user: User | null): string {
    if (!user) return '';

    if (!user.firstname && !user.lastname) return user.login.substring(0, 2).toUpperCase();

    const first = user.firstname?.charAt(0).toUpperCase() || '';
    const last = user.lastname?.charAt(0).toUpperCase() || '';

    return `${first}${last}` || '?';
}
