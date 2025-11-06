import { z } from "zod";

const usernameSchema = z
  .string()
  .min(3, "Nazwa użytkownika musi mieć co najmniej 3 znaki");
const emailSchema = z.string().email("Niepoprawny adres e-mail");
const passwordSchema = z
  .string()
  .min(6, "Hasło musi mieć co najmniej 6 znaków");

export const loginSchema = z.object({
  identifier: z.union([emailSchema, usernameSchema]),
  password: passwordSchema,
});

export const registerSchema = z
  .object({
    email: emailSchema,
    password: passwordSchema,
    confirmPassword: z.string().min(6, "Potwierdź hasło"),
    login: usernameSchema,
    acceptTerms: z.boolean().refine((val) => val === true, {
      message: "Musisz zaakceptować regulamin",
    }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Hasła nie są identyczne",
    path: ["confirmPassword"],
  });

export const forgotPasswordSchema = z.object({
  email: emailSchema,
});

export type LoginRequest = z.infer<typeof loginSchema>;
export type RegisterRequest = z.infer<typeof registerSchema>;
export type ForgotPasswordRequest = z.infer<typeof forgotPasswordSchema>;
