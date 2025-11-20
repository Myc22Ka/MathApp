"use client"

import { Send } from "lucide-react";
import { FC } from "react";
import { Button } from "../ui/button";

export interface ChatInputProps {
    input: string;
    setInput: (value: string) => void;
    onSend: () => void;
}


export const ChatInput: FC<ChatInputProps> = ({ input, setInput, onSend }) => {
    return (
        <div className="p-3">
            <div className="flex gap-2">
                <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && onSend()}
                    placeholder="Napisz wiadomość..."
                    className="flex-1 rounded-full px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-(--main)/50 transition"
                />
                <Button
                    animated
                    onClick={onSend}
                    className="rounded-full"
                    variant="main"
                >
                    <Send size={18} strokeWidth={3} />
                </Button>
            </div>
        </div>
    );
};