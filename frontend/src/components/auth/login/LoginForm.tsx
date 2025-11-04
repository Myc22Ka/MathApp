"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AiOutlineArrowRight } from "react-icons/ai";
import { Button } from "@/components/ui/button";
import PasswordInput from "@/components/inputs/auth/PasswordInput";
import { loginSchema, LoginRequest } from "@/lib/zod";
import { useAuthentication } from "@/hooks/auth/useAuthentication";
import { ErrorMessage } from "@/components/responses/ErrorResponse";
import LoginSuccess from "./LoginSuccess";
import FormFooter from "../layout/FormFooter";
import { Input } from "@/components/ui/input";
import { useRouter } from "next/navigation";
import TextInput from "@/components/inputs/auth/TextInput";

export default function LoginForm() {
  const router = useRouter();

  const form = useForm<LoginRequest>({
    resolver: zodResolver(loginSchema),
    defaultValues: { identifier: "", password: "" },
  });

  const { authenticate, user, isLoading, submitted, error } =
    useAuthentication<LoginRequest>("/auth/sign-in");

  const onSubmit = async (data: LoginRequest) => {
    const loggedUser = await authenticate(data);
    if (loggedUser && !loggedUser.verified) {
      router.push("/verify");
    }
  };

  if (submitted) return <LoginSuccess />;

  return (
    <div className="w-full max-w-md mx-auto">
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
        <TextInput
          error={form.formState.errors.identifier?.message}
          id="identifier"
        >
          <Input
            id="email"
            type="text"
            placeholder="email@domain.com"
            {...form.register("identifier")}
            className="pl-10"
          />
        </TextInput>
        <PasswordInput
          error={form.formState.errors.password?.message}
          id="password"
        >
          <Input
            id="password"
            type="password"
            placeholder="••••••••"
            {...form.register("password")}
            className="pl-10"
          />
        </PasswordInput>

        <ErrorMessage error={error} />
        <Button
          animated
          type="submit"
          className="w-full flex items-center justify-center gap-2"
          disabled={isLoading}
          variant="main"
        >
          <>
            Zaloguj się
            <AiOutlineArrowRight className="h-5 w-5" />
          </>
        </Button>
      </form>

      <FormFooter type="login" />
    </div>
  );
}
