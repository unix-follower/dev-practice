import axios, { AxiosError, AxiosResponse } from "axios"
import { FoodAdditiveSubstanceResponseDto } from "./chemistry/FoodAdditiveResponse"
import { makeFinanceStockMarketGetByTickerUrl, makeGetAllFoodAdditivesUrl } from "./utils"
import ApiError, { ApiErrorCode, type ApiErrorResponse, ErrorCode } from "./ApiError"
import FinanceStockMarketApi from "./finance/FinanceStockMarketApi"
import StocksResponseDto from "@/lib/api/finance/stockMarketModel"
import OrganicChemistryApi from "@/lib/api/chemistry/OrganicChemistryApi"

function getApiErrorCode(response: ApiErrorResponse | unknown): ApiErrorCode {
  let errorCode = ApiErrorCode.UNKNOWN
  if (response !== null && typeof response === "object" && "errorCode" in response) {
    errorCode = response.errorCode as ApiErrorCode
  }
  return errorCode
}

export enum ApiHttpClientType {
  FETCH,
  AXIOS,
}

export interface ApiHttpClientSettings {
  apiURL: string
  clientStrategy: ApiHttpClientType | undefined
}

export default class ApiHttpClient implements FinanceStockMarketApi {
  private delegate: FetchHttpClient

  constructor(settings: ApiHttpClientSettings) {
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

  getStockByTicker(ticker: string, page: number, pageSize: number): Promise<StocksResponseDto> {
    return this.delegate.getStockByTicker(ticker, page, pageSize)
  }

  getAllFoodAdditives(page: number, pageSize: number) {
    return this.delegate.getAllPubChemFoodAdditives(page, pageSize)
  }
}

async function executeFetchCatching(fn: () => Promise<Response>) {
  try {
    const response = await fn()
    if (!response.ok) {
      const responseBody = await response.json()
      const errorCode = getApiErrorCode(responseBody)
      return Promise.reject(new ApiError(errorCode, response))
    }
    return response.json()
  } catch (error) {
    let errorCode: ApiErrorCode | ErrorCode = ApiErrorCode.UNKNOWN
    if (error instanceof TypeError) {
      errorCode = ErrorCode.CONNECTION_REFUSED
    }
    throw new ApiError(errorCode, undefined, error)
  }
}

async function executeAxiosCatching(fn: () => Promise<AxiosResponse>) {
  try {
    const response = await fn()
    return response.data
  } catch (error) {
    let errorCode: ApiErrorCode | ErrorCode = ApiErrorCode.UNKNOWN
    if (error instanceof AxiosError) {
      if (error.code === "ECONNREFUSED") {
        errorCode = ErrorCode.CONNECTION_REFUSED
      } else {
        errorCode = getApiErrorCode(error.response?.data)
      }
    }
    throw new ApiError(errorCode, undefined, error)
  }
}

abstract class AbstractHttpClient {
  protected readonly apiURL: string

  constructor(apiURL: string) {
    this.apiURL = apiURL
  }

  getAllFoodAdditivesUrl(page: number, pageSize: number) {
    const url = makeGetAllFoodAdditivesUrl(this.apiURL)
    return `${url}?page=${page}&pageSize=${pageSize}`
  }

  getFinanceStockMarketGetByTickerUrl(ticker: string, page: number, pageSize: number) {
    const url = makeFinanceStockMarketGetByTickerUrl(this.apiURL)
    return `${url}?ticker=${ticker}&page=${page}&pageSize=${pageSize}`
  }
}

class FetchHttpClient extends AbstractHttpClient implements OrganicChemistryApi, FinanceStockMarketApi {
  async getAllPubChemFoodAdditives(page: number, pageSize: number): Promise<FoodAdditiveSubstanceResponseDto> {
    const options = {
      method: "GET",
    }

    const url = this.getAllFoodAdditivesUrl(page, pageSize)
    const apiCall = async () => fetch(url, options)
    return executeFetchCatching(apiCall)
  }

  async getStockByTicker(ticker: string, page: number, pageSize: number): Promise<StocksResponseDto> {
    const options = {
      method: "GET",
    }

    const url = this.getFinanceStockMarketGetByTickerUrl(ticker, page, pageSize)
    const apiCall = async () => fetch(url, options)
    return executeFetchCatching(apiCall)
  }
}

class AxiosHttpClient extends AbstractHttpClient implements OrganicChemistryApi, FinanceStockMarketApi {
  async getAllPubChemFoodAdditives(page: number, pageSize: number): Promise<FoodAdditiveSubstanceResponseDto> {
    const url = this.getAllFoodAdditivesUrl(page, pageSize)
    const response = await axios.get(url)
    return response.data
  }

  async getStockByTicker(ticker: string, page: number, pageSize: number): Promise<StocksResponseDto> {
    const url = this.getFinanceStockMarketGetByTickerUrl(ticker, page, pageSize)
    const apiCall = async () => axios.get(url)
    return executeAxiosCatching(apiCall)
  }
}
