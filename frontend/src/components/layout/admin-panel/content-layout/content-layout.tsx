import { Navbar } from '@/components/layout/navbar/navbar';
import { Breadcrumbs } from '../breadcrumb/breadcrumbs';

interface ContentLayoutProps {
    title: string;
    children: React.ReactNode;
}

export function ContentLayout({ title, children }: ContentLayoutProps) {
    return (
        <div>
            <Navbar title={title} />
            <div className="container pt-8 pb-8 px-4 sm:px-8">
                <Breadcrumbs />
                <div className="flex items-center justify-center">{children}</div>
            </div>
        </div>
    );
}
