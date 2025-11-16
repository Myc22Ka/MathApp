'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { Breadcrumb, BreadcrumbItem, BreadcrumbLink, BreadcrumbList, BreadcrumbPage, BreadcrumbSeparator } from '@/components/ui/breadcrumb';

const capitalize = (str: string) => str.charAt(0).toUpperCase() + str.slice(1);

export function Breadcrumbs() {
    const pathname = usePathname();
    const segments = pathname.split('/').filter(Boolean);

    if (segments.length === 0) return null;

    const crumbs = segments.map((seg, i) => {
        const href = '/' + segments.slice(0, i + 1).join('/');
        const isLast = i === segments.length - 1;

        return (
            <span key={href} className="flex items-center">
                <BreadcrumbItem>
                    {isLast ? (
                        <BreadcrumbPage className="text-(--main) font-medium">{capitalize(seg)}</BreadcrumbPage>
                    ) : (
                        <BreadcrumbLink asChild>
                            <Link href={href} className="transition-colors hover:text-(--main)">
                                {capitalize(seg)}
                            </Link>
                        </BreadcrumbLink>
                    )}
                </BreadcrumbItem>
                {!isLast && <BreadcrumbSeparator />}
            </span>
        );
    });

    return (
        <Breadcrumb className="mb-6">
            <BreadcrumbList>
                <BreadcrumbItem>
                    <BreadcrumbLink asChild>
                        <Link href="/" className="transition-colors hover:text-[--main]">
                            Home
                        </Link>
                    </BreadcrumbLink>
                </BreadcrumbItem>
                {segments.length > 0 && <BreadcrumbSeparator />}
                {crumbs}
            </BreadcrumbList>
        </Breadcrumb>
    );
}
