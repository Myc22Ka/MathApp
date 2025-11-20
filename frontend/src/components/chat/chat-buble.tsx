"use client"

import { MessageSquareMore, X } from "lucide-react";
import { FC } from "react";
import { Button } from "../ui/button";

export interface ChatBubbleProps {
    isOpen: boolean;
    onClick: () => void;
}

export const ChatBubble: FC<ChatBubbleProps> = ({ isOpen, onClick }) => {
    return (
        <Button
            onClick={onClick}
            className="fixed! bottom-6! right-6! rounded-2xl w-14 h-14 shadow-lg transition transform flex items-center justify-center z-40 [&_svg:not([class*='size-'])]:size-6!"
            variant="main"
        >
            {isOpen ? (
                <X strokeWidth={3} />
            ) : (
                <MessageSquareMore strokeWidth={3} />
            )}
        </Button>
    );
};