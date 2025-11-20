import React from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { AlertCircle } from 'lucide-react';

export const InstructionCard: React.FC = () => (
    <Card className="mt-6 shadow-lg">
        <CardContent>
            <div className="flex flex-col gap-2">
                <div className='flex gap-2'>
                    <AlertCircle className="h-5 w-5 shrink-0 mt-0.5 text-(--main)" />
                    <p className="font-semibold mb-2 text-(--main)">Instrukcja:</p>
                </div>
                <div className="text-sm text-white/70 leading-5">
                    <p>1. W górnej części narysuj notatki i obliczenia</p>
                    <p>2. Kliknij Zapisz tablicę aby wysłać notatki na backend</p>
                    <p>3. W dolnej części narysuj ostateczną odpowiedź</p>
                    <p>4. Kliknij Rozpoznaj tekst z odpowiedzi aby zamienić rysunek na tekst</p>
                    <p>5. Przejrzyj rozpoznaną odpowiedź w podglądzie</p>
                    <p>6. Kliknij Wyślij Odpowiedź aby wysłać odpowiedź na backend</p>
                </div>
            </div>
        </CardContent>
    </Card>
);