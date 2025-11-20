"use client"
import { Message } from "@/types/Messages";
import { FC, useState } from "react";
import { ChatBubble } from "./chat-buble";
import { ChatWindow } from "./chat-window";
import { useChatMessage } from "@/hooks/api/useChatMessage";

const Chat: FC = () => {
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [input, setInput] = useState<string>('');
    const [messages, setMessages] = useState<Message[]>([
        {
            id: '1',
            question: 'Cześć! Jak się masz?',
            response: 'Cześć! Mam się dobrze, dziękuję za pytanie!',
            createdAt: new Date(Date.now() - 3600000)
        },
        {
            id: '2',
            question: 'Co potrafisz robić?',
            response: 'Mogę Ci pomóc w wielu rzeczach: odpowiadać na pytania, pomagać w kodowaniu, wyjaśniać koncepty i wiele więcej!',
            createdAt: new Date(Date.now() - 1800000)
        }
    ]);

    const messageId = String(messages.length + 1);

    const { sendMessage, isLoading, error } = useChatMessage(
        (newMessage: Message) => {
            setMessages([...messages, newMessage]);
        },
        messageId
    );

    const handleSend = async (): Promise<void> => {
        if (input.trim()) {
            const userMessage: Message = {
                id: messageId,
                question: input,
                response: '',
                createdAt: new Date()
            };
            setMessages([...messages, userMessage]);
            const promptToSend = input;
            setInput('');

            await sendMessage(promptToSend);
        }
    };

    return (
        <div>
            <ChatBubble isOpen={isOpen} onClick={() => setIsOpen(!isOpen)} />
            <ChatWindow
                isOpen={isOpen}
                onClose={() => setIsOpen(false)}
                messages={messages}
                input={input}
                setInput={setInput}
                onSend={handleSend}
                isLoading={isLoading}
                error={error}
            />
        </div>
    );
};

export default Chat;