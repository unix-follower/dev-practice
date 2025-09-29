import React, { Suspense } from "react"
import GPUList from "./_components/GPUList"
import { getI18nDictionary } from "@/app/[lang]/dictionaries"
import { getGpuBackendURL } from "@/config/config"
import { getApiHttpClientSettings } from "@/lib/api/ApiHttpClient"

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { lang, httpClient } = await searchParams
  const translations = await getI18nDictionary((lang as string) || "en")

  const clientSettings = getApiHttpClientSettings(getGpuBackendURL, httpClient as string)

  return (
    <div id="computer-science-hardware-page">
      <Suspense fallback={<div>Loading...</div>}>
        <GPUList translations={translations} httpClientSettings={clientSettings} />
      </Suspense>
    </div>
  )
}
