"use client";

import { Separator } from "@/components/ui/separator";
import { Text } from "@/components/ui/text";
import OAuthButtons from "@/components/inputs/auth/OAuthButtons";

export default function OAuthSection() {
  return (
    <div className="mt-6 w-full">
      <div className="my-6 flex items-center gap-3">
        <Separator className="flex-1" />
        <Text size="xs" className="uppercase text-gray-500 font-medium">
          lub
        </Text>
        <Separator className="flex-1" />
      </div>

      <OAuthButtons />
    </div>
  );
}
