import React, { use, Suspense } from "react"
import { type ElementDefinition } from "cytoscape"
import ApiHttpClient, { ApiHttpClientSettings, ApiHttpClientType } from "@/lib/api/ApiHttpClient"
import { getBackendURL } from "@/config/config"
import CompoundGraphViewer from "@/app/chemistry/dbs/pub-chem/compound/graph/_components/CompoundGraphViewer"
import CompoundGraphWithRTK, {
  CompoundGraphListWithRTK,
} from "@/app/chemistry/dbs/pub-chem/compound/graph/_components/CompoundGraphWithRTK"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import { mapGraphResponse } from "@/lib/api/chemistry/mapper"
import { getI18nDictionary } from "@/app/[lang]/dictionaries"

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { mode, name, page, pageSize, lang } = await searchParams

  const params = { page: (page as string) || null, pageSize: (pageSize as string) || null }
  const translations = await getI18nDictionary((lang as string) || "en")

  if (mode === "rtk") {
    if (name) {
      return (
        <ViewerLayout>
          <CompoundGraphWithRTK translations={translations} />
        </ViewerLayout>
      )
    }
    return (
      <ViewerLayout>
        <CompoundGraphListWithRTK translations={translations} />
      </ViewerLayout>
    )
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
  let compoundDataPromise: Promise<ElementDefinition[]>
  if (name) {
    compoundDataPromise = client
      .doPubChemGraphGetCompoundDataByName(name as string, pageNumber, pageSizeNum)
      .then(mapGraphResponse)
  } else {
    compoundDataPromise = client.doPubChemGraphGetAll(pageNumber, pageSizeNum).then(mapGraphResponse)
  }

  return (
    <ViewerLayout>
      <Suspense fallback={<div>Loading...</div>}>
        <AwaitGraphData translations={translations} compoundDataPromise={compoundDataPromise} />
      </Suspense>
    </ViewerLayout>
  )
}

function ViewerLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <div id="compound-graph-viewer" className="grid grid-cols-[12%_88%] grid-rows-1 gap-4">
      {children}
    </div>
  )
}

interface AwaitGraphDataProps {
  compoundDataPromise: Promise<ElementDefinition[]>
  translations: Record<string, string | Record<string, string>>
}

function AwaitGraphData({ translations, compoundDataPromise }: AwaitGraphDataProps) {
  const compoundData = use(compoundDataPromise)

  return <CompoundGraphViewer translations={translations} compoundData={compoundData} />
}
