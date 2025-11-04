"use client";

interface LoadingMessageProps {
  loading?: boolean;
  text?: string;
}

export function LoadingMessage({ loading, text }: LoadingMessageProps) {
  return (
    <div className="h-2 text-center">
      {loading && <p className="text-sm text-gray-500 leading-2">{text}</p>}
    </div>
  );
}
