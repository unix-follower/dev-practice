export function makeGetAllFoodAdditivesUrl(baseUrl?: string) {
  return `${baseUrl ?? ""}/api/v1/chemistry/food-additives`
}
