"use client";

interface SuccessMessageProps {
  success?: string | null;
}

export function SuccessMessage({ success }: SuccessMessageProps) {
  return (
    <div className="h-2 text-center">
      {success && <p className="text-sm text-green-500 leading-2">{success}</p>}
    </div>
  );
}
