"use client";

interface MessageContainerProps {
  loading?: boolean;
  loadingText?: string;
  error?: string | null;
  success?: string | null;
}

export function MessageContainer({
  loading,
  loadingText,
  error,
  success,
}: MessageContainerProps) {
  let message = null;
  let colorClass = "";

  if (loading) {
    message = loadingText || "Ładowanie...";
    colorClass = "text-gray-500";
  } else if (error) {
    message = error;
    colorClass = "text-red-500";
  } else if (success) {
    message = success;
    colorClass = "text-green-500";
  }

  return (
    <div className="h-4 text-center">
      {message && (
        <p className={`text-sm leading-2 ${colorClass}`}>{message}</p>
      )}
    </div>
  );
}
