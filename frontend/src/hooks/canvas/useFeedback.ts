import { useState } from 'react';

export type FeedbackType = '' | 'success' | 'error' | 'empty';

export const useFeedback = () => {
    const [feedback, setFeedback] = useState<FeedbackType>('');

    const showSuccess = (): void => setFeedback('success');
    const showError = (): void => setFeedback('error');
    const showEmpty = (): void => setFeedback('empty');
    const clear = (): void => setFeedback('');

    return {
        feedback,
        showSuccess,
        showError,
        showEmpty,
        clear,
        setFeedback,
    };
};
