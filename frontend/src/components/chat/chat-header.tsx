"use client"

import { X } from "lucide-react";
import { FC } from "react";
import { Button } from "../ui/button";

export interface ChatHeaderProps {
    onClose: () => void;
}


export const ChatHeader: FC<ChatHeaderProps> = ({ onClose }) => {
    return (
        <div className="p-4 flex justify-between items-center">
            <div>
                <h1 className="text-lg font-bold">Mathfi</h1>
            </div>
            <Button
                onClick={onClose}
                className="transition"
                variant="ghost"
            >
                <X size={20} strokeWidth={3} />
            </Button>
        </div>
    );
};