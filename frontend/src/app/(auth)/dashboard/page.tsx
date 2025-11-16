'use client';

import { ContentLayout } from '@/components/layout/admin-panel/content-layout/content-layout';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';
import { useSidebar } from '@/components/providers/use-sidebar';

export default function DashboardPage() {
    const { settings, setSettings, isMounted } = useSidebar();

    if (!isMounted) return null;

    return (
        <ContentLayout title="Dashboard">
            <TooltipProvider>
                <div className="flex gap-6">
                    <Tooltip>
                        <TooltipTrigger asChild>
                            <div className="flex items-center space-x-2">
                                <Switch id="is-hover-open" onCheckedChange={x => setSettings({ isHoverOpen: x })} checked={settings.isHoverOpen} />
                                <Label htmlFor="is-hover-open">Hover Open</Label>
                            </div>
                        </TooltipTrigger>
                        <TooltipContent>
                            <p>When hovering on the sidebar in mini state, it will open</p>
                        </TooltipContent>
                    </Tooltip>
                    <Tooltip>
                        <TooltipTrigger asChild>
                            <div className="flex items-center space-x-2">
                                <Switch id="disable-sidebar" onCheckedChange={x => setSettings({ disabled: x })} checked={settings.disabled} />
                                <Label htmlFor="disable-sidebar">Disable Sidebar</Label>
                            </div>
                        </TooltipTrigger>
                        <TooltipContent>
                            <p>Hide sidebar</p>
                        </TooltipContent>
                    </Tooltip>
                </div>
            </TooltipProvider>
        </ContentLayout>
    );
}
