import { ChemistryGraphResponse } from "./graphModels"

export default interface CompoundGraphApi {
  doPubChemGraphGetAll(page: number, pageSize: number): Promise<ChemistryGraphResponse>

  doPubChemGraphGetCompoundDataByName(name: string, page: number, pageSize: number): Promise<ChemistryGraphResponse>
}
