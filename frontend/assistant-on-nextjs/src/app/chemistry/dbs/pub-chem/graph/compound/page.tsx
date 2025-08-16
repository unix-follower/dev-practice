import React, { use, Suspense } from "react"
import { type ElementDefinition } from "cytoscape"
import ApiHttpClient, { ApiHttpClientSettings, ApiHttpClientType } from "@/lib/api/ApiHttpClient"
import { getBackendURL } from "@/config/config"
import CompoundGraph from "@/app/components/chemistry/CompoundGraph"
import CompoundGraphWithRTK, { CompoundGraphListWithRTK } from "@/app/components/chemistry/CompoundGraphWithRTK"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import { mapGraphResponse } from "@/lib/api/chemistry/mapper"

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { mode, name, page, pageSize } = await searchParams

  const params = { page: (page as string) || null, pageSize: (pageSize as string) || null }

  if (mode === "rtk") {
    if (name) {
      return <CompoundGraphWithRTK />
    }
    return <CompoundGraphListWithRTK />
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
  const compoundDataPromise: Promise<ElementDefinition[]> = client
    .doPubChemGraphGetAll(pageNumber, pageSizeNum)
    .then(mapGraphResponse)

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <CompoundGraphProxy compoundDataPromise={compoundDataPromise} />
    </Suspense>
  )
}

interface CompoundGraphProxyProps {
  compoundDataPromise: Promise<ElementDefinition[]>
}

function CompoundGraphProxy({ compoundDataPromise }: CompoundGraphProxyProps) {
  const compoundData = use(compoundDataPromise)
  return <CompoundGraph compoundData={compoundData} />
}
