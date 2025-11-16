'use client';
import { Flame } from 'lucide-react';
import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';
import { useAuth } from '@/components/providers/auth-provider';

export function StreakBadge() {
    const { user } = useAuth();

    const streak = user?.dailyExercise.streak ?? 0;
    const isSolved = user?.dailyExercise?.isSolved ?? false;

    return (
        <TooltipProvider disableHoverableContent>
            <Tooltip delayDuration={100}>
                <TooltipTrigger asChild>
                    <motion.div
                        initial={isSolved ? { scale: 0.8, opacity: 0 } : {}}
                        animate={isSolved ? { scale: 1, opacity: 1 } : {}}
                        transition={{ type: 'spring', stiffness: 200, damping: 15 }}
                        whileHover={isSolved ? { scale: 1.1 } : {}}
                    >
                        <Button
                            className={`
                rounded-full h-8 mr-2 gap-1 relative overflow-hidden
                transition-all duration-300 ease-out
                ${
                    isSolved
                        ? 'bg-linear-to-r from-orange-500 to-red-500 text-white shadow-md shadow-orange-500/50'
                        : 'bg-background border border-border hover:border-orange-500/50'
                }
              `}
                            variant={isSolved ? 'default' : 'outline'}
                            size="sm"
                        >
                            {isSolved && (
                                <motion.div
                                    className="absolute inset-0 bg-linear-to-r from-transparent via-white/30 to-transparent"
                                    initial={{ x: '-100%' }}
                                    animate={{ x: '100%' }}
                                    transition={{ duration: 2, repeat: Infinity, repeatDelay: 1 }}
                                />
                            )}

                            <div className="relative flex items-center gap-1 z-10">
                                <span className="text-sm font-bold">{streak}</span>

                                {isSolved ? (
                                    <div className="relative">
                                        <motion.div animate={{ y: [0, -4, 0] }} transition={{ duration: 1.2, repeat: Infinity, repeatDelay: 0.5 }}>
                                            <Flame className="w-[1.2rem] h-[1.2rem] text-white" />
                                        </motion.div>
                                    </div>
                                ) : (
                                    <Flame className="w-[1.2rem] h-[1.2rem] text-orange-500 opacity-60" />
                                )}
                            </div>

                            {isSolved && (
                                <>
                                    <motion.div
                                        className="absolute top-1 left-2 w-1.5 h-1.5 bg-yellow-300 rounded-full"
                                        animate={{
                                            y: [-20, -40],
                                            opacity: [1, 0],
                                            x: [0, -10],
                                        }}
                                        transition={{ duration: 1.5, repeat: Infinity, repeatDelay: 1 }}
                                    />
                                    <motion.div
                                        className="absolute bottom-1 right-3 w-1.5 h-1.5 bg-yellow-300 rounded-full"
                                        animate={{
                                            y: [0, -30],
                                            opacity: [1, 0],
                                            x: [0, 15],
                                        }}
                                        transition={{ duration: 1.5, repeat: Infinity, repeatDelay: 1.3, delay: 0.3 }}
                                    />
                                    <motion.div
                                        className="absolute top-2 right-1 w-1 h-1 bg-orange-300 rounded-full"
                                        animate={{
                                            y: [-15, -35],
                                            opacity: [1, 0],
                                            x: [0, 5],
                                        }}
                                        transition={{ duration: 1.5, repeat: Infinity, repeatDelay: 1.1, delay: 0.6 }}
                                    />
                                </>
                            )}
                        </Button>
                    </motion.div>
                </TooltipTrigger>
                <TooltipContent side="bottom" className={isSolved ? 'bg-linear-to-r from-orange-500 to-red-500 text-white border-0' : ''}>
                    <motion.div
                        initial={{ opacity: 0, y: -5 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}
                        className={isSolved ? 'flex items-center gap-2' : ''}
                    >
                        <span>Seria aktywna: {streak} dni!</span>
                    </motion.div>
                </TooltipContent>
            </Tooltip>
        </TooltipProvider>
    );
}
