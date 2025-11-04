"use client";
import * as React from "react";
import { cn } from "@/lib/utils";

interface TextProps extends React.HTMLAttributes<HTMLParagraphElement> {
  size?: "xs" | "sm" | "md" | "lg";
  weight?: "normal" | "medium" | "bold";
}

export const Text: React.FC<TextProps> = ({
  size = "md",
  weight = "normal",
  className,
  ...props
}) => {
  const sizeClasses = {
    xs: "text-xs",
    sm: "text-sm",
    md: "text-base",
    lg: "text-lg",
  };

  const weightClasses = {
    normal: "font-normal",
    medium: "font-medium",
    bold: "font-bold",
  };

  return (
    <p
      className={cn(sizeClasses[size], weightClasses[weight], className)}
      {...props}
    />
  );
};
