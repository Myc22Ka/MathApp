'use client';

import * as React from 'react';
import { Slot } from '@radix-ui/react-slot';
import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/utils';
import { motion } from 'framer-motion';
import { LoaderCircle } from 'lucide-react';

const buttonVariants = cva(
    "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-4 shrink-0 [&_svg]:shrink-0 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive cursor-pointer",
    {
        variants: {
            variant: {
                default: 'bg-primary text-primary-foreground hover:bg-primary/90',
                destructive:
                    'bg-destructive text-white hover:bg-destructive/90 focus-visible:ring-destructive/20 dark:focus-visible:ring-destructive/40 dark:bg-destructive/60',
                outline:
                    'border bg-background shadow-xs hover:bg-accent hover:text-accent-foreground dark:bg-input/30 dark:border-input dark:hover:bg-input/50',
                secondary: 'bg-secondary text-secondary-foreground hover:bg-secondary/80',
                ghost: 'hover:bg-accent hover:text-accent-foreground dark:hover:bg-accent/50',
                link: 'text-(--main)! underline! hover:underline!',
                ghostMain: 'hover:bg-(--main-accent)! dark:bg-(--main-accent)/50 dark:text-foreground',
                main: 'bg-(--main) text-white hover:bg-(--main)/90',
                menu: 'bg-transparent text-foreground/80 hover:bg-(--main-accent)/10 hover:text-(--main) dark:hover:bg-(--main-accent)/20',
                none: "bg-transparent text-inherit border-none shadow-none hover:none focus:none",
            },
            size: {
                default: 'h-9 px-4 py-2 has-[>svg]:px-3',
                sm: 'h-8 rounded-md gap-1.5 px-3 has-[>svg]:px-2.5',
                lg: 'h-10 rounded-md px-6 has-[>svg]:px-4',
                icon: 'size-9',
                'icon-sm': 'size-8',
                'icon-lg': 'size-10',
            },
        },
        defaultVariants: {
            variant: 'default',
            size: 'default',
        },
    }
);

interface ButtonProps extends Omit<React.ComponentProps<'button'>, 'onClick'>, VariantProps<typeof buttonVariants> {
    asChild?: boolean;
    animated?: boolean;
    loading?: boolean;
    loadingText?: string;
    onClick?: () => Promise<void> | void;
    onError?: (error: unknown) => void;
}

const buttonVariantsAnimation = {
    whileTap: { scale: 0.97 },
    whileHover: { scale: 1.02 },
};

function Button({
    className,
    variant,
    size,
    asChild = false,
    animated = false,
    loading = false,
    loadingText,
    children,
    disabled,
    onClick,
    onError,
    ...props
}: ButtonProps) {
    const Comp = asChild ? Slot : 'button';

    const handleClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
        if (!onClick || loading) return;
        try {
            await onClick();
        } catch (err) {
            onError?.(err);
        }
    };

    const content = (
        <Comp onClick={handleClick} className={cn(buttonVariants({ variant, size, className }), 'relative')} disabled={disabled || loading} {...props}>
            {loading ? (
                <>
                    <LoaderCircle className="absolute animate-spin h-4 w-4 opacity-80" />
                    {loadingText ? <span className="opacity-0">{loadingText}</span> : null}
                </>
            ) : (
                children
            )}
        </Comp>
    );

    if (!animated) return content;

    return (
        <motion.div
            whileTap={disabled ? {} : buttonVariantsAnimation.whileTap}
            whileHover={disabled ? {} : buttonVariantsAnimation.whileHover}
            transition={{ type: 'spring', stiffness: 400, damping: 20 }}
        >
            {content}
        </motion.div>
    );
}

export { Button, buttonVariants };
