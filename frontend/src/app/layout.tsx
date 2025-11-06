import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "../styles/globals.css";
import { ContextWrapper } from "@/components/auth/layout/wrappers/ContextWrapper";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Twoja aplikacja",
  description: "Strona logowania i inne",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pl" suppressHydrationWarning>
      <body className={inter.className}>
        <ContextWrapper>{children}</ContextWrapper>
      </body>
    </html>
  );
}
