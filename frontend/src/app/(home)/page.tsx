'use client';

import { Button } from '@/components/ui/button';
import { useAuth } from '@/components/providers/auth-provider';
import React, { useEffect } from 'react';

const Home = () => {
    const { user, logout } = useAuth();

    useEffect(() => {
        console.log(user);
    }, [user]);

    return (
        <div>
            <div>Home</div>
            <Button variant="ghostMain" onClick={() => logout()}>
                Wyloguj
            </Button>
        </div>
    );
};

export default Home;
