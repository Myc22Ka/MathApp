"use client";

import { Mail } from "lucide-react";
import { Label } from "@/components/ui/label";
import { ErrorMessage } from "@/components/responses/ErrorResponse";

type Props = {
  error?: string | null;
  children: React.ReactNode;
  label?: string;
  id?: string;
};

export default function EmailInput({
  children,
  error,
  label = "Adres e-mail",
  id = "email",
}: Props) {
  return (
    <div className="space-y-2">
      <Label htmlFor={id}>{label}</Label>

      <div className="relative">
        <Mail className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
        {children}
      </div>

      <ErrorMessage error={error} />
    </div>
  );
}
