import LoginHero from "@/components/auth/login/LoginHero";
import { Card, CardContent } from "@/components/ui/card";
import React from "react";

type LayoutProps = {
  children: React.ReactNode;
};

const layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-muted/40 p-4">
      <Card className="p-0 w-full max-w-7xl overflow-hidden shadow-lg">
        <div className="grid grid-cols-1 md:grid-cols-[3fr_2fr]">
          <LoginHero />

          <CardContent className="p-8 flex flex-col justify-center">
            {children}
          </CardContent>
        </div>
      </Card>
    </div>
  );
};

export default layout;
