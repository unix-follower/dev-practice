import i18n from "i18next"
import { initReactI18next } from "react-i18next"
import LanguageDetector from "i18next-browser-languagedetector"
import enTranslation from "/public/locales/en/translation.json"

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: "en",
    supportedLngs: ["en"],
    debug: process.env.NODE_ENV === "development",
    resources: {
      en: {
        translation: enTranslation,
      },
    },
    interpolation: {
      escapeValue: false, // React already escapes values
    },
  })

export default i18n
