"use client";

import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";

interface AuthFooterLinksProps {
  type: "login" | "register";
}

export default function AuthFooterLinks({ type }: AuthFooterLinksProps) {
  const isLogin = type === "login";

  return (
    <div className="mt-8 space-y-4 text-center">
      <Text size="sm" className="text-gray-600">
        {isLogin ? (
          <>
            <span>Nie masz konta?</span>
            <Button variant="link" size="sm" asChild>
              <a href="/register" className="font-semibold">
                Zarejestruj się
              </a>
            </Button>
          </>
        ) : (
          <>
            <span>Masz już konto?</span>
            <Button variant="link" size="sm" asChild>
              <a href="/login" className="font-semibold">
                Zaloguj się
              </a>
            </Button>
          </>
        )}
      </Text>

      <Text size="xs" className="text-gray-500 leading-relaxed">
        <span>
          Poprzez {isLogin ? "logowanie" : "rejestrację"}, akceptujesz nasze
        </span>
        <Button variant="link" className="text-xs p-0" asChild>
          <a href="/terms">Warunki korzystania z usługi</a>
        </Button>
        <span> oraz </span>
        <Button variant="link" className="text-xs p-0" asChild>
          <a href="/privacy">Politykę prywatności</a>
        </Button>
        .
      </Text>
    </div>
  );
}
