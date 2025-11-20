"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";


interface NavLink {
    href: string;
    label: string;
}

const navLinks: NavLink[] = [
    { href: "/", label: "Strona główna", },
    { href: "/dashboard", label: "Dashboard" },
    { href: "/elearning", label: "E-learning" },
];

export function NavLinks() {
    const pathname = usePathname();

    return (
        <nav className="hidden sm:flex items-center space-x-4 text-sm text-muted-foreground">
            {navLinks.map((link) => (
                <Link
                    key={link.href}
                    href={link.href}
                    className={`hover:text-foreground transition-colors ${pathname === link.href ? "text-(--main)!" : ""
                        }`}
                >
                    {link.label}
                </Link>
            ))}
        </nav>
    );
}