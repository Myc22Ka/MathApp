"use client";

interface ErrorMessageProps {
  error?: string | null;
}

export function ErrorMessage({ error }: ErrorMessageProps) {
  return (
    <div className="h-2 text-center">
      {error && <p className="text-sm text-red-500 leading-2">{error}</p>}
    </div>
  );
}
