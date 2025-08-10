import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import StocksResponseDto from "@/lib/api/finance/stockMarketModel"
import { getBackendURL } from "@/config/config"
import { makeFinanceStockMarketGetByTickerUrl } from "@/lib/api/utils"

const tag = "FinanceStockMarket"

interface GetByTickerQueryString {
  ticker: string
  page: number
  pageSize: number
}

export const stockMarketSlice = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: getBackendURL() }),
  reducerPath: "financeStockMarketApi",
  tagTypes: [tag],
  endpoints: (build) => ({
    getByTicker: build.query<StocksResponseDto, GetByTickerQueryString>({
      query: ({ ticker, page = 1, pageSize = 10 }) =>
        `${makeFinanceStockMarketGetByTickerUrl()}?ticker=${ticker}&page=${page}&pageSize=${pageSize}`,
      providesTags: [tag],
    }),
  }),
})

export const { useGetByTickerQuery } = stockMarketSlice
