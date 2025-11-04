"use client";

import { useRef, useState } from "react";
import { post, getApiErrorMessage } from "@/lib/axios";
import { Button } from "@/components/ui/button";
import { MessageContainer } from "@/components/responses/MessageContainer";

export default function VerificationCodeInput() {
  const [code, setCode] = useState(Array(6).fill(""));
  const [isLoading, setIsLoading] = useState(false);
  const [isResending, setIsResending] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const inputsRef = useRef<Array<HTMLInputElement | null>>([]);

  const email = "krzysztof02gach@gmail.com"; // hardkod na razie

  const handleChange = (value: string, index: number) => {
    if (!/^\d?$/.test(value)) return;
    if (index !== 0 && code.slice(0, index).some((v) => v === "")) return;

    const newCode = [...code];
    newCode[index] = value;
    setCode(newCode);

    if (value && index < 5) {
      inputsRef.current[index + 1]?.focus();
    }

    if (newCode.every((d) => d !== "")) {
      submitCode(newCode.join(""));
    }
  };

  const handleKeyDown = (
    e: React.KeyboardEvent<HTMLInputElement>,
    index: number
  ) => {
    if (e.key === "Backspace") {
      const newCode = [...code];
      if (newCode[index] === "" && index > 0) {
        inputsRef.current[index - 1]?.focus();
      }
      newCode[index] = "";
      setCode(newCode);
    }
  };

  const handlePaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    const pasted = e.clipboardData.getData("text").trim();
    if (/^\d{6}$/.test(pasted)) {
      const newCode = pasted.split("");
      setCode(newCode);
      newCode.forEach((_, i) => inputsRef.current[i]?.focus());
      submitCode(pasted);
    }
    e.preventDefault();
  };

  const submitCode = async (codeValue: string) => {
    setIsLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const response = await post<{ message: string }>(
        `/auth/verify-code?email=${email}&code=${codeValue}`
      );

      setSuccess("Kod zweryfikowany pomyślnie!");
      console.log("Kod zweryfikowany!", response);
      // tutaj np. router.push("/dashboard");
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  };

  const resendCode = async () => {
    setIsResending(true);
    setError(null);
    setSuccess(null);

    try {
      const response = await post<{ message: string }>(
        `/auth/resend-code?email=${email}`
      );

      setSuccess(response.message || "Nowy kod został wysłany!");
      setCode(Array(6).fill(""));
      inputsRef.current[0]?.focus();
    } catch (err) {
      setError(getApiErrorMessage(err));
    } finally {
      setIsResending(false);
    }
  };

  return (
    <div className="flex flex-col items-center gap-4">
      <div className="flex gap-2">
        {code.map((digit, i) => (
          <input
            key={i}
            ref={(el) => {
              inputsRef.current[i] = el;
            }}
            type="text"
            inputMode="numeric"
            pattern="\d*"
            maxLength={1}
            value={digit}
            onChange={(e) => handleChange(e.target.value, i)}
            onKeyDown={(e) => handleKeyDown(e, i)}
            onPaste={handlePaste}
            className="w-12 h-12 text-center border rounded-md focus:outline-none focus:ring-2 focus:ring-primary"
          />
        ))}
      </div>

      <div className="flex gap-2">
        <Button
          variant="main"
          onClick={() => submitCode(code.join(""))}
          disabled={code.some((d) => d === "") || isLoading}
        >
          Wyślij kod
        </Button>

        <Button
          variant="outline"
          onClick={resendCode}
          disabled={isResending || isLoading}
        >
          Wyślij ponownie kod
        </Button>
      </div>

      <MessageContainer
        loading={isLoading || isResending}
        loadingText={
          isLoading ? "Sprawdzanie kodu..." : "Wysyłanie nowego kodu..."
        }
        error={error}
        success={success}
      />
    </div>
  );
}
