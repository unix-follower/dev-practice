import React, { Suspense } from "react"
import ApiHttpClient, { ApiHttpClientSettings, ApiHttpClientType } from "@/lib/api/ApiHttpClient"
import { getBackendURL } from "@/config/config"
import StockMUIDataGrid, { StockMUIDataGridWithRTK } from "@/app/components/finance/StockMUIDataGrid"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import { getI18nDictionary } from "@/app/[lang]/dictionaries"

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { mode, ticker, page, pageSize, lang } = await searchParams

  const params = { page: (page as string) || null, pageSize: (pageSize as string) || null }
  const translations = await getI18nDictionary((lang as string) || "en")

  if (mode === "rtk") {
    return <StockMUIDataGridWithRTK translations={translations} />
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
  const tickerToSearchFor = (ticker as string) || "KO"
  const stocksPromise = client.getStockByTicker(tickerToSearchFor, pageNumber, pageSizeNum)

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <StockMUIDataGrid stocksResponsePromise={stocksPromise} translations={translations} />
    </Suspense>
  )
}
