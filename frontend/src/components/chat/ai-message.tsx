"use client"

import { FC } from "react";

export interface MessageProps {
    message: string;
    timestamp: Date;
}

export const AIMessage: FC<MessageProps> = ({ message, timestamp }) => {
    return (
        <div className="flex justify-start">
            <div className="bg-(--main)/10 rounded-2xl rounded-tl-sm px-4 py-2 max-w-xs shadow-md">
                <p className="text-sm">{message}</p>
                <p className="text-xs dark:text-white/50 text-black/50 mt-1">
                    {timestamp.toLocaleTimeString('pl-PL', {
                        hour: '2-digit',
                        minute: '2-digit'
                    })}
                </p>
            </div>
        </div>
    );
};