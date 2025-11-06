"use client";

interface ErrorMessageProps {
  error?: string | null;
}

export function ErrorMessage({ error }: ErrorMessageProps) {
  return (
    <div className="h-1 text-center">
      {error && <p className="text-sm text-red-500 leading-1">{error}</p>}
    </div>
  );
}
