"use client";

import AuthFooterLinks from "./sections/AuthFooterLinks";
import OAuthSection from "./sections/OAuthSection";

interface FormFooterProps {
  type: "login" | "register";
}

export const FormFooter: React.FC<FormFooterProps> = ({ type }) => {
  return (
    <div className="mt-6 w-full">
      <OAuthSection />
      <AuthFooterLinks type={type} />
    </div>
  );
};

export default FormFooter;
