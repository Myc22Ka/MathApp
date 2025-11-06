import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const token = request.cookies.get("auth_token")?.value;

  const staticPublicRoutes = ["/"];

  const prefixPublicRoutes = ["/auth", "/forgot-password", "/verify"];

  const isStaticPublic = staticPublicRoutes.includes(pathname);

  const isPrefixPublic = prefixPublicRoutes.some((route) =>
    pathname.startsWith(route)
  );

  const isPublic = isStaticPublic || isPrefixPublic;

  if (!token && !isPublic) {
    const loginUrl = request.nextUrl.clone();
    loginUrl.pathname = "/login";
    loginUrl.searchParams.set("from", pathname);
    return NextResponse.redirect(loginUrl);
  }

  if (token && isPublic && pathname !== "/") {
    const homeUrl = request.nextUrl.clone();
    homeUrl.pathname = "/";
    return NextResponse.redirect(homeUrl);
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/((?!_next/static|_next/image|favicon.ico).*)"],
};
