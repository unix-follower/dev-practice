import React, { Suspense } from "react"
import ApiHttpClient, { ApiHttpClientSettings, ApiHttpClientType } from "@/lib/api/ApiHttpClient"
import { getBackendURL } from "@/config/config"
import PubChemFdaMUIDataGrid, { PubChemFdaMUIDataGridWithRTK } from "@/app/components/chemistry/PubChemFdaMUIDataGrid"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import { getI18nDictionary } from "@/app/[lang]/dictionaries"

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { mode, page, pageSize, lang } = await searchParams

  const params = { page: (page as string) || null, pageSize: (pageSize as string) || null }
  const translations = await getI18nDictionary((lang as string) || "en")

  if (mode === "rtk") {
    return <PubChemFdaMUIDataGridWithRTK translations={translations} />
  }

  let clientSettings: ApiHttpClientSettings
  if (mode === "axios") {
    clientSettings = {
      apiURL: getBackendURL(),
      clientStrategy: ApiHttpClientType.AXIOS,
    }
  } else {
    clientSettings = {
      apiURL: getBackendURL(),
      clientStrategy: ApiHttpClientType.FETCH,
    }
  }

  const [pageNumber, pageSizeNum] = parsePaginationParams(params)

  const client = new ApiHttpClient(clientSettings)
  const foodAdditives = client.getAllFoodAdditives(pageNumber, pageSizeNum)

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <PubChemFdaMUIDataGrid foodAdditivesPromise={foodAdditives} translations={translations} />
    </Suspense>
  )
}
