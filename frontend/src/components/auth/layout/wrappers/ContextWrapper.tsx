"use client";

import { ReactNode } from "react";
import { AuthProvider } from "@/context/AuthContext";

export function ContextWrapper({ children }: { children: ReactNode }) {
  return <AuthProvider>{children}</AuthProvider>;
}
