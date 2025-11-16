import { z } from 'zod';

const usernameSchema = z
    .string()
    .min(3, 'Nazwa użytkownika musi mieć co najmniej 3 znaki')
    .regex(/^[a-zA-Z0-9._-]+$/, 'Nazwa użytkownika może zawierać tylko litery, cyfry, ., _, -');

const emailSchema = z.string().email('Niepoprawny adres e-mail');

const passwordSchema = z.string().min(6, 'Hasło musi mieć co najmniej 6 znaków');

export const deleteAccountSchema = z.object({
    password: passwordSchema,
    confirmation: z.string().refine(val => val === 'USUŃ MOJE KONTO', 'Wpisz dokładnie "USUŃ MOJE KONTO" aby potwierdzić'),
});

export const loginSchema = z.object({
    identifier: z
        .string()
        .min(3, 'Wprowadź login lub email')
        .refine(val => (val.includes('@') ? emailSchema.safeParse(val).success : usernameSchema.safeParse(val).success), 'Niepoprawny login lub email'),
    password: passwordSchema,
});

export const registerSchema = z
    .object({
        email: emailSchema,
        password: passwordSchema,
        confirmPassword: z.string(),
        login: usernameSchema,
        acceptTerms: z.boolean().refine(val => val === true, {
            message: 'Musisz zaakceptować regulamin',
        }),
    })
    .refine(data => data.password === data.confirmPassword, {
        path: ['confirmPassword'],
        message: 'Hasła nie są identyczne',
    });

export const forgotPasswordSchema = z.object({
    email: emailSchema,
});

export const passwordResetSchema = z
    .object({
        password: passwordSchema,
        confirmPassword: z.string(),
    })
    .refine(data => data.password === data.confirmPassword, {
        path: ['confirmPassword'],
        message: 'Hasła nie są takie same',
    });

export const changePasswordSchema = z
    .object({
        currentPassword: z.string().min(1, 'Aktualne hasło jest wymagane'),
        newPassword: passwordSchema,
        confirmPassword: z.string().min(1, 'Potwierdzenie hasła jest wymagane'),
    })
    .refine(data => data.newPassword === data.confirmPassword, {
        path: ['confirmPassword'],
        message: 'Hasła nie są identyczne',
    });
export const profileSchema = z
    .object({
        firstname: z.string().min(2, 'Imię musi mieć co najmniej 2 znaki').optional().or(z.literal('')),
        lastname: z.string().min(2, 'Nazwisko musi mieć co najmniej 2 znaki').optional().or(z.literal('')),
        phoneNumber: z
            .string()
            .regex(/^[\d\s\-+()]*$/, 'Niepoprawny numer telefonu')
            .optional()
            .or(z.literal('')),
        address: z.string().optional().or(z.literal('')),
        dateOfBirth: z.string().optional().or(z.literal('')),
        gender: z.enum(['MALE', 'FEMALE', 'OTHER']).optional(),
        notificationsEnabled: z.boolean().optional(),
        twoFactorAuthEnabled: z.boolean().optional(),
    })
    .refine(data => !!data.gender, {
        message: 'Wybierz płeć',
        path: ['gender'],
    })
    .refine(data => typeof data.notificationsEnabled === 'boolean', {
        message: 'Wybierz ustawienie powiadomień',
        path: ['notificationsEnabled'],
    })
    .refine(data => typeof data.twoFactorAuthEnabled === 'boolean', {
        message: 'Włącz autoryzację dwuetapową',
        path: ['twoFactorAuthEnabled'],
    });

export type ProfileFormData = z.infer<typeof profileSchema>;
export type ChangePasswordRequest = z.infer<typeof changePasswordSchema>;
export type DeleteAccountRequest = z.infer<typeof deleteAccountSchema>;
export type PasswordResetRequest = z.infer<typeof passwordResetSchema>;
export type LoginRequest = z.infer<typeof loginSchema>;
export type RegisterRequest = z.infer<typeof registerSchema>;
export type ForgotPasswordRequest = z.infer<typeof forgotPasswordSchema>;
