export function makeGetAllFoodAdditivesUrl(baseUrl?: string) {
  return `${baseUrl ?? ""}/api/v1/chemistry/food-additives`
}

export function makeFinanceStockMarketGetByTickerUrl(baseUrl?: string) {
  return `${baseUrl ?? ""}/api/v1/finance/stock-market`
}
