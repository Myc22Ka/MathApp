"use client";

import { motion } from "framer-motion";
import { TypeAnimation } from "react-type-animation";

export default function LoginHero() {
  return (
    <div className="hidden md:flex flex-col justify-start items-start relative p-10 overflow-hidden bg-(--main)">
      <div className="absolute top-0 right-0 w-96 h-96 blob-primary rounded-full mix-blend-multiply filter blur-3xl opacity-40 bg-(--main)"></div>
      <div className="absolute bottom-0 left-10 w-60 h-60 blob-secondary rounded-full mix-blend-multiply filter blur-3xl opacity-40 bg-(--main)"></div>
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.8 }}
        className="relative z-10"
      >
        <h1 className="text-5xl font-bold leading-snug text-white">
          Odkrywaj matematykę
        </h1>
      </motion.div>
      <p className="text-white mt-6 text-lg max-w-2/4 leading-snug">
        <TypeAnimation
          sequence={[
            "Zaloguj się i rozpocznij przygodę z zadaniami, funkcjami i wizualizacjami.",
            1000,
          ]}
          wrapper="span"
          cursor={true}
          repeat={0}
          speed={35}
        />
      </p>
    </div>
  );
}
