import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { getBackendURL } from "@/config/config"
import { makeUrlForPubChemGraphGetAll } from "@/lib/api/utils"
import { ChemistryGraphResponse } from "@/lib/api/chemistry/graphModels"
import { PaginationParams } from "@/lib/api/common"

const tag = "pubChemGraph"

interface GetCompoundDataByNameQueryString extends PaginationParams {
  name: string
}

export const pubChemGraphSlice = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: getBackendURL() }),
  reducerPath: "chemistryPubChemGraphApi",
  tagTypes: [tag],
  endpoints: (build) => ({
    getAllCompounds: build.query<ChemistryGraphResponse, PaginationParams>({
      query: ({ page = 1, pageSize = 10 }) => makeUrlForPubChemGraphGetAll({ page, pageSize }),
      providesTags: [tag],
    }),

    getCompoundDataByName: build.query<ChemistryGraphResponse, GetCompoundDataByNameQueryString>({
      query: ({ page = 1, pageSize = 10, name }) => makeUrlForPubChemGraphGetAll({ page, pageSize, name }),
      providesTags: [tag],
    }),
  }),
})

export const { useGetAllCompoundsQuery, useGetCompoundDataByNameQuery } = pubChemGraphSlice
