import globals from "globals"
import pluginJs from "@eslint/js"
import tseslint from "typescript-eslint"

/** @type {import("eslint").Linter.Config[]} */
export default [
  {
    ignores: ["node_modules/**", "build/**"],
  },
  {
    files: ["**/*.{js,mjs,cjs,ts}"],
    languageOptions: { globals: globals.browser },
    settings: {
      "import/resolver": {
        alias: {
          map: [["@", "./src"]],
          extensions: [".js", ".jsx", ".ts", ".tsx"],
        },
      },
    },
    rules: {
      "@typescript-eslint/no-unused-vars": [
        "error", // or "error" if you want stricter enforcement
        {
          varsIgnorePattern: "^_", // Ignore variables starting with `_`
          argsIgnorePattern: "^_", // Ignore arguments starting with `_`
        },
      ],
    },
  },
  pluginJs.configs.recommended,
  ...tseslint.configs.recommended,
]
