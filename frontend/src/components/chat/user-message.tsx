"use client"

import { FC } from "react";

export interface MessageProps {
    message: string;
    timestamp: Date;
}

export const UserMessage: FC<MessageProps> = ({ message, timestamp }) => {
    return (
        <div className="flex justify-end">
            <div className="bg-(--main) rounded-2xl rounded-tr-sm px-4 py-2 max-w-xs shadow-md">
                <p className="text-sm">{message}</p>
                <p className="text-xs dark:text-white/70 text-black/50 mt-1">
                    {timestamp.toLocaleTimeString('pl-PL', {
                        hour: '2-digit',
                        minute: '2-digit'
                    })}
                </p>
            </div>
        </div>
    );
};