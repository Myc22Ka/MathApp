/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '9000', // jeśli używasz niestandardowego portu
        pathname: '/**', // dowolna ścieżka
      },
    ],
  },
  output: "standalone",
};

module.exports = nextConfig;
