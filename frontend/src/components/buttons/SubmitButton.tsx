"use client";

import { Button } from "@/components/ui/button";
import { AiOutlineArrowRight } from "react-icons/ai";

interface SubmitButtonProps {
  isLoading: boolean;
  label: string;
  onClick?: () => void;
  disabled?: boolean;
}

export function SubmitButton({
  isLoading,
  label,
  onClick
}: SubmitButtonProps) {
  return (
    <Button
      type="submit"
      className="w-full flex items-center justify-center gap-2"
      disabled={isLoading}
      variant="main"
      loading={isLoading}
      onClick={onClick}
    >
      {label}
      <AiOutlineArrowRight className="h-5 w-5" />
    </Button>
  );
}
