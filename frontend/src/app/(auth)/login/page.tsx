import LoginForm from "@/components/auth/login/LoginForm";

export default function LoginPage() {
  return (
    <div className="mx-auto w-full max-w-sm space-y-6">
      <div className="text-center">
        <h1 className="text-2xl font-semibold tracking-tight">Zaloguj się</h1>
        <p className="text-sm text-muted-foreground mt-1">
          Wpisz swoje dane, aby kontynuować
        </p>
      </div>
      <LoginForm />
    </div>
  );
}
