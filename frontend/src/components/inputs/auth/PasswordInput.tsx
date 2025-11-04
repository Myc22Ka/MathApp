"use client";

import { Lock } from "lucide-react";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { ErrorMessage } from "@/components/responses/ErrorResponse";

type Props = {
  label?: string;
  error?: string | null;
  showForgotPassword?: boolean;
  children: React.ReactNode;
  id?: string;
};

export default function PasswordInput({
  error,
  showForgotPassword = true,
  children,
  label = "Hasło",
  id = "password",
}: Props) {
  return (
    <div className="space-y-2">
      <div className="flex items-center justify-between">
        <Label htmlFor={id}>{label}</Label>
        {showForgotPassword && (
          <Button
            variant="link"
            size="sm"
            asChild
            className="h-min px-0 leading-none"
          >
            <a href="/forgot-password">Zapomniałem hasła?</a>
          </Button>
        )}
      </div>

      <div className="relative">
        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
        {children}
      </div>

      <ErrorMessage error={error} />
    </div>
  );
}
