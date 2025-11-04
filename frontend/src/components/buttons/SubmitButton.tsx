"use client";

import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";
import { AiOutlineArrowRight } from "react-icons/ai";

interface SubmitButtonProps {
  isLoading: boolean;
  label: string;
  loadingLabel?: string;
}

export function SubmitButton({
  isLoading,
  label,
  loadingLabel = label,
}: SubmitButtonProps) {
  return (
    <Button
      type="submit"
      className="w-full flex items-center justify-center gap-2"
      disabled={isLoading}
      variant="main"
    >
      {isLoading ? (
        <>
          <Loader2 className="h-5 w-5 animate-spin" />
          {loadingLabel}...
        </>
      ) : (
        <>
          {label}
          <AiOutlineArrowRight className="h-5 w-5" />
        </>
      )}
    </Button>
  );
}
