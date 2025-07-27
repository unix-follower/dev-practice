import { Injectable, Inject } from "@angular/core"
import axios from "axios"
import { FoodAdditiveSubstanceResponseDto } from "@/lib/features/chemistry/food-additives/model/FoodAdditiveResponse"
import { makeGetAllFoodAdditivesUrl } from "./utils"

export enum ApiHttpClientType {
  FETCH,
  AXIOS,
}

export interface ApiHttpClientSettings {
  apiURL: string
  clientStrategy: ApiHttpClientType | undefined
}

@Injectable({
  providedIn: "root",
})
export default class ApiHttpClient {
  private delegate: FetchHttpClient

  constructor(@Inject("apiHttpClientSettings") settings: ApiHttpClientSettings) {
    const { apiURL, clientStrategy } = settings
    switch (clientStrategy) {
      case ApiHttpClientType.AXIOS:
        this.delegate = new AxiosHttpClient(apiURL)
        break
      case ApiHttpClientType.FETCH:
      default:
        this.delegate = new FetchHttpClient(apiURL)
    }
  }

  async getAllFoodAdditives(page: number, size: number) {
    return this.delegate.getAllFoodAdditives(page, size)
  }
}

async function executeFailOn4xxOr5xx(fn: () => Promise<Response>) {
  const response = await fn()
  if (!response.ok) {
    return Promise.reject()
  }
  return response.json()
}

abstract class AbstractHttpClient {
  protected readonly apiURL: string

  constructor(apiURL: string) {
    this.apiURL = apiURL
  }

  abstract getAllFoodAdditives(page: number, size: number): Promise<FoodAdditiveSubstanceResponseDto>

  getAllFoodAdditivesUrl(page: number, size: number) {
    const url = makeGetAllFoodAdditivesUrl(this.apiURL)
    return `${url}?page=${page}&size=${size}`
  }
}

class FetchHttpClient extends AbstractHttpClient {
  async getAllFoodAdditives(page: number, size: number): Promise<FoodAdditiveSubstanceResponseDto> {
    const options = {
      method: "GET",
    }

    const url = this.getAllFoodAdditivesUrl(page, size)
    const apiCall = async () => fetch(url, options)
    return executeFailOn4xxOr5xx(apiCall)
  }
}

class AxiosHttpClient extends AbstractHttpClient {
  async getAllFoodAdditives(page: number, size: number): Promise<FoodAdditiveSubstanceResponseDto> {
    const url = this.getAllFoodAdditivesUrl(page, size)
    const response = await axios.get(url)
    return response.data
  }
}
