'use client';

import { useAuth } from '@/components/providers/auth-provider';
import React, { useEffect } from 'react';
import { Navbar } from '@/components/layout/navbar/navbar';
import Chat from '@/components/chat/chat';

const Home = () => {
    const { user } = useAuth();

    useEffect(() => {
        console.log(user);
    }, [user]);

    return (
        <div>
            <Navbar title="Home" />
            <Chat />
        </div>
    );
};

export default Home;
