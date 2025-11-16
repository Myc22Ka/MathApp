'use client';

import { createContext, useContext, useReducer, useEffect, useCallback, useState, ReactNode } from 'react';

type SidebarSettings = {
    disabled: boolean;
    isHoverOpen: boolean;
};

type SidebarState = {
    isOpen: boolean;
    isHover: boolean;
    settings: SidebarSettings;
};

type SidebarAction =
    | { type: 'TOGGLE_OPEN' }
    | { type: 'SET_OPEN'; payload: boolean }
    | { type: 'SET_HOVER'; payload: boolean }
    | { type: 'SET_SETTINGS'; payload: Partial<SidebarSettings> }
    | { type: 'HYDRATE'; payload: SidebarState };

type SidebarContextType = {
    state: SidebarState;
    dispatch: (action: SidebarAction) => void;
    isMounted: boolean;
    toggleOpen: () => void;
    setIsOpen: (isOpen: boolean) => void;
    setIsHover: (isHover: boolean) => void;
    setSettings: (settings: Partial<SidebarSettings>) => void;
    openState: boolean;
    settings: SidebarSettings;
};

const SidebarContext = createContext<SidebarContextType | undefined>(undefined);

const STORAGE_KEY = 'sidebar-state';

const initialState: SidebarState = {
    isOpen: true,
    isHover: false,
    settings: { disabled: false, isHoverOpen: false },
};

const sidebarReducer = (state: SidebarState, action: SidebarAction): SidebarState => {
    switch (action.type) {
        case 'TOGGLE_OPEN':
            return { ...state, isOpen: !state.isOpen };

        case 'SET_OPEN':
            return { ...state, isOpen: action.payload };

        case 'SET_HOVER':
            return { ...state, isHover: action.payload };

        case 'SET_SETTINGS':
            return {
                ...state,
                settings: { ...state.settings, ...action.payload },
            };

        case 'HYDRATE':
            return action.payload;

        default:
            return state;
    }
};

const getStoredState = (): SidebarState | null => {
    if (typeof window === 'undefined') return null;

    try {
        const stored = localStorage.getItem(STORAGE_KEY);
        return stored ? JSON.parse(stored) : null;
    } catch (error) {
        console.error('Failed to parse sidebar state:', error);
        return null;
    }
};

const saveToStorage = (state: SidebarState) => {
    if (typeof window !== 'undefined') {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
        } catch (error) {
            console.error('Failed to save sidebar state:', error);
        }
    }
};

export function SidebarProvider({ children }: { children: ReactNode }) {
    const [state, dispatch] = useReducer(sidebarReducer, initialState);
    const [isMounted, setIsMounted] = useState(false);

    // Hydrate z localStorage
    useEffect(() => {
        const storedState = getStoredState();
        if (storedState) {
            dispatch({ type: 'HYDRATE', payload: storedState });
        }
        setIsMounted(true);
    }, []);

    // Persist na zmiany stanu
    useEffect(() => {
        if (isMounted) {
            saveToStorage(state);
        }
    }, [state, isMounted]);

    const toggleOpen = useCallback(() => {
        dispatch({ type: 'TOGGLE_OPEN' });
    }, []);

    const setIsOpen = useCallback((isOpen: boolean) => {
        dispatch({ type: 'SET_OPEN', payload: isOpen });
    }, []);

    const setIsHover = useCallback((isHover: boolean) => {
        dispatch({ type: 'SET_HOVER', payload: isHover });
    }, []);

    const setSettings = useCallback((settings: Partial<SidebarSettings>) => {
        dispatch({ type: 'SET_SETTINGS', payload: settings });
    }, []);

    const openState = state.isOpen || (state.settings.isHoverOpen && state.isHover);

    const value: SidebarContextType = {
        state,
        dispatch,
        isMounted,
        toggleOpen,
        setIsOpen,
        setIsHover,
        setSettings,
        openState,
        settings: state.settings,
    };

    return <SidebarContext.Provider value={value}>{children}</SidebarContext.Provider>;
}

export function useSidebar() {
    const context = useContext(SidebarContext);
    if (!context) {
        throw new Error('useSidebar must be used within SidebarProvider');
    }
    return {
        ...context,
        settings: context.settings || { disabled: false, isHoverOpen: false },
    };
}
