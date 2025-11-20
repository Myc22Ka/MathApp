"use client"

import { FC } from "react";
import { ChatHeader } from "./chat-header";
import { MessagesContainer } from "./messages-container";
import { ChatInput } from "./chat-input";
import { Message } from "@/types/Messages";
import { Card } from "../ui/card";

export interface ChatWindowProps {
    isOpen: boolean;
    onClose: () => void;
    messages: Message[];
    input: string;
    setInput: (value: string) => void;
    onSend: () => void;
    isLoading?: boolean;
    error?: string | null;
}

export const ChatWindow: FC<ChatWindowProps> = ({
    isOpen,
    onClose,
    messages,
    input,
    setInput,
    onSend,
    error,
    isLoading
}) => {
    if (!isOpen) return null;

    console.log(`Error: ${error}, Loading: ${isLoading}`);

    return (
        <Card className="fixed bottom-24 right-6 w-120 h-136 rounded-2xl! py-0 shadow-2xl flex flex-col z-50">
            <ChatHeader onClose={onClose} />
            <MessagesContainer messages={messages} />
            <ChatInput input={input} setInput={setInput} onSend={onSend} />
        </Card>
    );
};