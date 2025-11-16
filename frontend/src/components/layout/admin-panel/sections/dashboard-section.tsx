'use client';

import { LucideIcon } from 'lucide-react';

type DashboardSectionProps = {
    children: React.ReactNode;
    icon?: LucideIcon;
    title?: string;
    subtitle?: string;
};

export default function DashboardSection({ children, icon: Icon, title, subtitle }: DashboardSectionProps) {
    return (
        <div className="bg-white dark:bg-(--main-accent)/30 rounded-lg border border-gray-200 dark:border-slate-800 p-6">
            <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4 flex items-center gap-2">
                {Icon && <Icon className="w-5 h-5" />}
                {title}
            </h2>
            <p className="text-sm text-gray-600 dark:text-gray-400 mb-4">{subtitle}</p>
            <div className="flex items-center justify-between">{children}</div>
        </div>
    );
}
