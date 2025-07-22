import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { FoodAdditiveSubstanceResponseDto } from "@/lib/features/chemistry/food-additives/model/FoodAdditiveResponse"
import { getBackendURL } from "@/config/config"
import { makeGetAllFoodAdditivesUrl } from "./utils"

const FOOD_ADDITIVES_TAG = "FoodAdditives"

interface FoodAdditiveQueryString {
  page: number
  size: number
}

export const foodAdditivesSlice = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: getBackendURL() }),
  reducerPath: "foodAdditivesApi",
  tagTypes: [FOOD_ADDITIVES_TAG],
  endpoints: (build) => ({
    getAll: build.query<FoodAdditiveSubstanceResponseDto, FoodAdditiveQueryString>({
      query: ({ page = 1, size = 10 }) => `${makeGetAllFoodAdditivesUrl()}?page=${page}&size=${size}`,
      providesTags: [FOOD_ADDITIVES_TAG],
    }),
  }),
})

export const { useGetAllQuery } = foodAdditivesSlice
