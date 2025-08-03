import { type Config } from "jest"

const config: Config = {
  clearMocks: true,
  collectCoverage: true,
  coverageDirectory: "coverage",
  coverageProvider: "v8",
  extensionsToTreatAsEsm: [".ts", ".tsx"],
  moduleNameMapper: {
    "^~/(.*)$": "<rootDir>/app/$1",
    "^/public/(.*)$": "<rootDir>/public/$1",
  },
  testEnvironment: "jsdom",
  testTimeout: 180000,
  testMatch: ["<rootDir>/__tests__/webdriver/**/*.ts"],
  transform: {
    "^.+\\.tsx?$": [
      "ts-jest",
      {
        useESM: true,
      },
    ],
  },
  verbose: true,
}

export default config
