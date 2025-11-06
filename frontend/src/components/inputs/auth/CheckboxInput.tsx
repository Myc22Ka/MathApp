"use client";

import { Label } from "@/components/ui/label";

type Props = {
  error?: string | null;
  children: React.ReactNode;
  label?: React.ReactNode;
  id?: string;
};

export default function CheckboxInput({ children, error, label, id }: Props) {
  return (
    <div className="space-y-1 m-0">
      <div className="flex items-center space-x-2 p-1">
        {children}
        {label && (
          <Label htmlFor={id}>
            <div
              className={`text-sm leading-2 ${
                error ? "text-red-500" : "text-gray-400"
              }`}
            >
              {label}
            </div>
          </Label>
        )}
      </div>
    </div>
  );
}
