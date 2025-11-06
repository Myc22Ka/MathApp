import React from "react";
import { Metadata } from "next";
import { Card, CardContent } from "@/components/ui/card";
import { Si2Fas } from "react-icons/si";

type LayoutProps = {
  children: React.ReactNode;
};

export const metadata: Metadata = {
  title: "Weryfikacja kodu – MathApp",
  description: "Wprowadź kod weryfikacyjny aby potwierdzić tożsamość",
};

export default function VerifyLayout({ children }: LayoutProps) {
  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-900 p-4">
      <Card className="w-full max-w-md shadow-md bg-white dark:bg-slate-800">
        <CardContent className="p-6 flex flex-col gap-4">
          <div className="flex flex-col items-center text-center">
            <Si2Fas className="h-20 w-20 pb-4 text-(--main)" />
            <h2 className="text-xl font-semibold text-slate-900 dark:text-slate-100">
              Weryfikacja dwuetapowa
            </h2>
            <p className="text-sm text-slate-600 dark:text-slate-300 mt-1">
              Wpisz kod weryfikacyjny wysłany na Twój adres e-mail.
            </p>
          </div>

          {children}
        </CardContent>
      </Card>
    </div>
  );
}
