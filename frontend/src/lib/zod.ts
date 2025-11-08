import { z } from "zod";

const usernameSchema = z
  .string()
  .min(3, "Nazwa użytkownika musi mieć co najmniej 3 znaki")
  .regex(
    /^[a-zA-Z0-9._-]+$/,
    "Nazwa użytkownika może zawierać tylko litery, cyfry, ., _, -"
  );

const emailSchema = z.string().email("Niepoprawny adres e-mail");

const passwordSchema = z
  .string()
  .min(6, "Hasło musi mieć co najmniej 6 znaków");

export const loginSchema = z.object({
  identifier: z
    .string()
    .min(3, "Wprowadź login lub email")
    .refine(
      (val) =>
        val.includes("@")
          ? emailSchema.safeParse(val).success
          : usernameSchema.safeParse(val).success,
      "Niepoprawny login lub email"
    ),
  password: passwordSchema,
});

export const registerSchema = z
  .object({
    email: emailSchema,
    password: passwordSchema,
    confirmPassword: z.string(),
    login: usernameSchema,
    acceptTerms: z.boolean().refine((val) => val === true, {
      message: "Musisz zaakceptować regulamin",
    }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    path: ["confirmPassword"],
    message: "Hasła nie są identyczne",
  });

export const forgotPasswordSchema = z.object({
  email: emailSchema,
});

export const passwordResetSchema = z
  .object({
    password: passwordSchema,
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    path: ["confirmPassword"],
    message: "Hasła nie są takie same",
  });

export type PasswordResetRequest = z.infer<typeof passwordResetSchema>;
export type LoginRequest = z.infer<typeof loginSchema>;
export type RegisterRequest = z.infer<typeof registerSchema>;
export type ForgotPasswordRequest = z.infer<typeof forgotPasswordSchema>;
