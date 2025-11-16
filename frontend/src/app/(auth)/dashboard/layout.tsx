import AdminPanelLayout from '@/components/layout/admin-panel/admin-panel-layout';
import { SidebarProvider } from '@/components/providers/use-sidebar';

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
    return (
        <SidebarProvider>
            <AdminPanelLayout>{children}</AdminPanelLayout>
        </SidebarProvider>
    );
}
