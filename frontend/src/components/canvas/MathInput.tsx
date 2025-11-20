"use client";

import React, { useRef } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "../ui/card";
import { SubmitButton } from "../buttons/SubmitButton";
import TextInput from "../inputs/auth/TextInput";

interface MathInputProps {
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    isSubmitting: boolean;
    handleSubmitAnswer: () => Promise<void>;
}

const MATH_SYMBOLS = ["∪", "∩", "∖", "∅", "ℝ", "∞"];

export const MathInput: React.FC<MathInputProps> = ({ value, onChange, placeholder, isSubmitting, handleSubmitAnswer }) => {
    const inputRef = useRef<HTMLInputElement>(null);

    const handleSymbolClick = (symbol: string) => {
        if (!inputRef.current) return;

        const input = inputRef.current;
        const start = input.selectionStart || 0;
        const end = input.selectionEnd || 0;

        const newValue = value.slice(0, start) + symbol + value.slice(end);
        onChange(newValue);

        setTimeout(() => {
            input.focus();
            input.setSelectionRange(start + 1, start + 1);
        }, 0);
    };

    return (
        <Card className="mt-6 shadow-lg">
            <CardContent>
                <TextInput label="Odpowiedź">
                    <Input
                        ref={inputRef}
                        type="text"
                        value={value}
                        onChange={(e) => onChange(e.target.value)}
                        placeholder={placeholder || "Wpisz odpowiedź..."}
                    />
                </TextInput>

                <div className="flex gap-2 flex-wrap mb-3 justify-center items-center">
                    {MATH_SYMBOLS.map((symbol) => (
                        <Button key={symbol} variant="outline" size="sm" onClick={() => handleSymbolClick(symbol)}>
                            {symbol}
                        </Button>
                    ))}
                </div>

                <SubmitButton
                    isLoading={isSubmitting}
                    label="Wyślij Odpowiedź"
                    onClick={handleSubmitAnswer}
                    disabled={!inputRef.current?.value.trim() || isSubmitting}
                />
            </CardContent>
        </Card >
    );
};
