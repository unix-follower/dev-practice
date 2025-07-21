import { Suspense } from "react"
import ApiHttpClient, {
  ApiHttpClientSettings,
  ApiHttpClientType,
} from "@/lib/features/chemistry/food-additives/ApiHttpClient"
import { getBackendURL } from "@/config/config"
import FoodAdditivesMUIDataGrid, {
  FoodAdditivesMUIDataGridWithRTK,
} from "@/app/components/food-additives/FoodAdditivesMUIDataGrid"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import Menu from "@/app/components/Menu"
import { getI18nDictionary } from "@/app/[lang]/dictionaries"

export default async function Page({
  searchParams,
}: {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>
}) {
  const { mode, page, size, lang } = await searchParams

  const params = { page: (page as string) || null, size: (size as string) || null }
  const translations = await getI18nDictionary((lang as string) || "en")

  if (mode === "rtk") {
    return <FoodAdditivesMUIDataGridWithRTK translations={translations} />
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

  const [pageNumber, pageSize] = parsePaginationParams(params)

  const client = new ApiHttpClient(clientSettings)
  const foodAdditives = client.getAllFoodAdditives(pageNumber, pageSize)

  return (
    <>
      <Menu />
      <Suspense fallback={<div>Loading...</div>}>
        <FoodAdditivesMUIDataGrid foodAdditivesPromise={foodAdditives} translations={translations} />
      </Suspense>
    </>
  )
}
