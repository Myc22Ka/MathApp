"use client";

import { CheckCircle2 } from "lucide-react";

export default function LoginSuccess() {
  return (
    <div className="flex flex-col items-center justify-center space-y-4 py-12">
      <div className="rounded-full bg-emerald-100 p-4 animate-pulse">
        <CheckCircle2 className="h-8 w-8 text-emerald-600" />
      </div>
      <h3 className="text-lg font-semibold text-gray-900">
        Logowanie pomyślne!
      </h3>
      <p className="text-sm text-gray-600">Trwa przekierowanie...</p>
    </div>
  );
}
