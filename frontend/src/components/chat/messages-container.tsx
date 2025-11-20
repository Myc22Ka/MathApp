import { FC } from "react";
import { AIMessage } from "./ai-message";
import { UserMessage } from "./user-message";
import { Message } from "@/types/Messages";

export interface MessagesContainerProps {
    messages: Message[];
}

export const MessagesContainer: FC<MessagesContainerProps> = ({ messages }) => {
    return (
        <div className="flex-1 overflow-y-auto p-4 space-y-3">
            {messages.map((msg) => (
                <div key={msg.id} className="space-y-2">
                    <UserMessage message={msg.question} timestamp={msg.createdAt} />
                    <AIMessage message={msg.response} timestamp={msg.createdAt} />
                </div>
            ))}
        </div>
    );
};