import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { FoodAdditiveSubstanceResponseDto } from "@/lib/api/chemistry/FoodAdditiveResponse"
import { getBackendURL } from "@/config/config"
import { makeGetAllFoodAdditivesUrl } from "@/lib/api/utils"

const tag = "pubChemFda"

interface PubChemFdaQueryString {
  page: number
  pageSize: number
}

export const pubChemFdaSlice = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: getBackendURL() }),
  reducerPath: "chemistryPubChemFdaApi",
  tagTypes: [tag],
  endpoints: (build) => ({
    getAll: build.query<FoodAdditiveSubstanceResponseDto, PubChemFdaQueryString>({
      query: ({ page = 1, pageSize = 10 }) => `${makeGetAllFoodAdditivesUrl()}?page=${page}&pageSize=${pageSize}`,
      providesTags: [tag],
    }),
  }),
})

export const { useGetAllQuery } = pubChemFdaSlice
