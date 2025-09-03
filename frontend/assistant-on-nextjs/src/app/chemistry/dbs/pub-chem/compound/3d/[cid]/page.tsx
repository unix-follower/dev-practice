import React, { Suspense, use } from "react"
import ApiHttpClient, { ApiHttpClientSettings, ApiHttpClientType } from "@/lib/api/ApiHttpClient"
import { getBackendURL } from "@/config/config"
import SDFViewer from "./_components/SDFViewer"
import SDFWithRTK from "./_components/SDFWithRTK"
import { CompoundSDFDataResponse } from "@/lib/api/chemistry/compoundModels"

export default async function Page({
  params,
  searchParams,
}: {
  params: Promise<{ cid: number }>
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { cid } = await params
  const { mode } = await searchParams

  if (mode === "rtk") {
    return <SDFWithRTK />
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

  const client = new ApiHttpClient(clientSettings)
  const compoundDataPromise = client.doPubChemGetCompoundSDFDataByCid(cid)

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <SDFViewerProxy compoundDataPromise={compoundDataPromise} />
    </Suspense>
  )
}

interface SDFViewerProxyProps {
  compoundDataPromise: Promise<CompoundSDFDataResponse>
}

function SDFViewerProxy({ compoundDataPromise }: SDFViewerProxyProps) {
  const compoundData = use(compoundDataPromise)
  return <SDFViewer compoundData={compoundData} />
}
