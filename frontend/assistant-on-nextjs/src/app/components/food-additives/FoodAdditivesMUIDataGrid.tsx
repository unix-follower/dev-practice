"use client"

import { use, useEffect } from "react"
import { useSearchParams } from "next/navigation"
import { TFunction } from "i18next"
import { useTranslation } from "react-i18next"
import i18n from "@/config/i18n"
import { DataGrid, GridColDef } from "@mui/x-data-grid"
import Paper from "@mui/material/Paper"

import {
  FoodAdditiveSubstanceDto,
  FoodAdditiveSubstanceResponseDto,
} from "@/lib/features/chemistry/food-additives/model/FoodAdditiveResponse"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import { useGetAllQuery } from "@/lib/features/chemistry/food-additives/foodAdditivesSlice"

const getRowId = (value: FoodAdditiveSubstanceDto) => value.compoundCid

function createColumns(t: TFunction<"translation", undefined>): GridColDef[] {
  return [
    { field: "compoundCid", headerName: t("chemistryFoodAdditives.compoundId"), type: "number", width: 130 },
    { field: "name", headerName: t("chemistryFoodAdditives.name"), width: 130 },
    { field: "synonyms", headerName: t("chemistryFoodAdditives.synonyms"), width: 130 },
    { field: "molecularWeight", headerName: t("chemistryFoodAdditives.molecularWeight"), type: "number", width: 130 },
    { field: "molecularFormula", headerName: t("chemistryFoodAdditives.molecularFormula"), width: 130 },
    { field: "polarArea", headerName: t("chemistryFoodAdditives.polarArea"), type: "number", width: 130 },
    { field: "complexity", headerName: t("chemistryFoodAdditives.complexity"), type: "number" },
    { field: "xlogp", headerName: t("chemistryFoodAdditives.xlogp"), type: "number" },
    { field: "heavyAtomCount", headerName: t("chemistryFoodAdditives.heavyAtomCount"), type: "number" },
    { field: "hBondDonorCount", headerName: t("chemistryFoodAdditives.hBondDonorCount"), type: "number" },
    { field: "hBondAcceptorCount", headerName: t("chemistryFoodAdditives.hBondAcceptorCount"), type: "number" },
    { field: "rotatableBondCount", headerName: t("chemistryFoodAdditives.rotatableBondCount"), type: "number" },
    { field: "inchi", headerName: t("chemistryFoodAdditives.inchi") },
    { field: "smiles", headerName: t("chemistryFoodAdditives.smiles") },
    { field: "inchiKey", headerName: t("chemistryFoodAdditives.inchiKey") },
    { field: "iupacName", headerName: t("chemistryFoodAdditives.iupacName") },
    { field: "exactMass", headerName: t("chemistryFoodAdditives.exactMass"), type: "number" },
    { field: "monoisotopicMass", headerName: t("chemistryFoodAdditives.monoisotopicMass"), type: "number" },
    { field: "charge", headerName: t("chemistryFoodAdditives.charge"), type: "number" },
    { field: "covalentUnitCount", headerName: t("chemistryFoodAdditives.covalentUnitCount"), type: "number" },
    { field: "isotopicAtomCount", headerName: t("chemistryFoodAdditives.isotopicAtomCount"), type: "number" },
    { field: "totalAtomStereoCount", headerName: t("chemistryFoodAdditives.totalAtomStereoCount"), type: "number" },
    { field: "definedAtomStereoCount", headerName: t("chemistryFoodAdditives.definedAtomStereoCount"), type: "number" },
    {
      field: "undefinedAtomStereoCount",
      headerName: t("chemistryFoodAdditives.undefinedAtomStereoCount"),
      type: "number",
    },
    { field: "totalBondStereoCount", headerName: t("chemistryFoodAdditives.totalBondStereoCount"), type: "number" },
    { field: "definedBondStereoCount", headerName: t("chemistryFoodAdditives.definedBondStereoCount"), type: "number" },
    {
      field: "undefinedBondStereoCount",
      headerName: t("chemistryFoodAdditives.undefinedBondStereoCount"),
      type: "number",
    },
    {
      field: "linkedPubChemLiteratureCount",
      headerName: t("chemistryFoodAdditives.linkedPubChemLiteratureCount"),
      type: "number",
    },
    {
      field: "linkedPubChemPatentCount",
      headerName: t("chemistryFoodAdditives.linkedPubChemPatentCount"),
      type: "number",
    },
    {
      field: "linkedPubChemPatentFamilyCount",
      headerName: t("chemistryFoodAdditives.linkedPubChemPatentFamilyCount"),
      type: "number",
    },
    { field: "meshHeadings", headerName: t("chemistryFoodAdditives.meshHeadings") },
    { field: "annotationContent", headerName: t("chemistryFoodAdditives.annotationContent") },
    { field: "annotationTypeCount", headerName: t("chemistryFoodAdditives.annotationTypeCount"), type: "number" },
    { field: "linkedBioAssays", headerName: t("chemistryFoodAdditives.linkedBioAssays"), type: "number" },
    {
      field: "createDate",
      headerName: t("chemistryFoodAdditives.createDate"),
      valueFormatter: (value?: string) => {
        if (value == null) {
          return null
        }
        return new Date(value)
      },
    },
    { field: "dataSource", headerName: t("chemistryFoodAdditives.dataSource") },
    { field: "dataSourceCategory", headerName: t("chemistryFoodAdditives.dataSourceCategory") },
    { field: "taggedByPubChem", headerName: t("chemistryFoodAdditives.taggedByPubChem") },
  ]
}

const paginationModel = { page: 1, pageSize: 10 }

interface FoodAdditivesMUIDataGridProps {
  foodAdditivesPromise: Promise<FoodAdditiveSubstanceResponseDto>
  translations: Record<string, string | Record<string, string>>
}

const pageSizeOptions = [5, 10, 20, 50, 100]

export default function FoodAdditivesMUIDataGrid({
  foodAdditivesPromise,
  translations,
}: FoodAdditivesMUIDataGridProps) {
  const foodAdditiveList = use(foodAdditivesPromise)
  const { t } = useTranslation()

  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  const foodAdditiveColumns = createColumns(t)
  return (
    <Paper sx={{ height: 400, width: "100%" }}>
      <DataGrid
        getRowId={getRowId}
        rows={foodAdditiveList}
        columns={foodAdditiveColumns}
        initialState={{ pagination: { paginationModel } }}
        pageSizeOptions={pageSizeOptions}
        checkboxSelection
        sx={{ border: 0 }}
      />
    </Paper>
  )
}

interface FoodAdditivesMUIDataGridWithRTKProps {
  translations: Record<string, string | Record<string, string>>
}

export function FoodAdditivesMUIDataGridWithRTK({ translations }: FoodAdditivesMUIDataGridWithRTKProps) {
  const searchParams = useSearchParams()
  const [page, size] = parsePaginationParams(searchParams)

  const { t } = useTranslation()

  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  const foodAdditiveColumns = createColumns(t)

  const { data } = useGetAllQuery({ page, size })

  return (
    <Paper sx={{ height: 400, width: "100%" }}>
      <DataGrid
        getRowId={getRowId}
        rows={data}
        columns={foodAdditiveColumns}
        initialState={{ pagination: { paginationModel } }}
        pageSizeOptions={pageSizeOptions}
        checkboxSelection
        sx={{ border: 0 }}
      />
    </Paper>
  )
}
