"use client"

import React, { useEffect, useState } from "react"
import { useQuery } from "@tanstack/react-query"
import Button from "@mui/material/Button"
import { DataGrid, GridColDef, GridRowParams } from "@mui/x-data-grid"
import Paper from "@mui/material/Paper"
import { useTranslation } from "react-i18next"
import I18nTranslation from "@/lib/features/common/translation"
import i18n from "@/config/i18n"
import PageStyleSettingsDialog from "./PageStyleSettingsDialog"
import { TFunction } from "i18next"
import { useStyleEngine } from "@/lib/hooks/styleEngineHooks"
import { ApiHttpClientSettings } from "@/lib/api/ApiHttpClient"
import GpuApiHttpClient from "@/lib/api/hardware/GpuApiHttpClient"
import { StyleEngineProvider } from "@/lib/hooks/styleEngineHooks"
import { mapToGPUList } from "@/lib/api/hardware/gpuMapper"
import { GPU } from "@/lib/api/hardware/gpuModel"
import GpuDetails from "./GpuDetails"
import type GpuDetailsUpdateCallbackParams from "./model"

function createColumns(t: TFunction<"translation", undefined>): GridColDef[] {
  return [
    { field: "id", headerName: t("computerScienceHardwarePage.id") },
    { field: "modelName", headerName: t("computerScienceHardwarePage.modelName") },
    { field: "launch", headerName: t("computerScienceHardwarePage.launch") },
    { field: "codeName", headerName: t("computerScienceHardwarePage.codeName") },
    { field: "fabNm", headerName: t("computerScienceHardwarePage.fabNm") },
    { field: "transistorsMillion", headerName: t("computerScienceHardwarePage.transistorsMillion") },
    { field: "dieSizeMm", headerName: t("computerScienceHardwarePage.dieSizeMm") },
    { field: "busInterface", headerName: t("computerScienceHardwarePage.busInterface") },
    { field: "coreClockMHz", headerName: t("computerScienceHardwarePage.coreClockMHz") },
    { field: "memoryClockMHz", headerName: t("computerScienceHardwarePage.memoryClockMHz") },
    { field: "coreConfig", headerName: t("computerScienceHardwarePage.coreConfig") },
    { field: "memorySizeMiB", headerName: t("computerScienceHardwarePage.memorySizeMiB") },
    { field: "memoryBandwidthGbPerSec", headerName: t("computerScienceHardwarePage.memoryBandwidthGbPerSec") },
    { field: "memoryBusType", headerName: t("computerScienceHardwarePage.memoryBusType") },
    { field: "memoryBusWidthBit", headerName: t("computerScienceHardwarePage.memoryBusWidthBit") },
    { field: "fillrateMOpsPerSec", headerName: t("computerScienceHardwarePage.fillrateMOpsPerSec") },
    { field: "fillrateMPixelsPerSec", headerName: t("computerScienceHardwarePage.fillrateMPixelsPerSec") },
    { field: "fillrateMTexelsPerSec", headerName: t("computerScienceHardwarePage.fillrateMTexelsPerSec") },
    { field: "fillrateMVerticesPerSec", headerName: t("computerScienceHardwarePage.fillrateMVerticesPerSec") },
    { field: "latestAPISupportDirect3D", headerName: t("computerScienceHardwarePage.latestAPISupportDirect3D") },
    { field: "tdpWatts", headerName: t("computerScienceHardwarePage.tdpWatts") },
    { field: "vendor", headerName: t("computerScienceHardwarePage.vendor") },
    { field: "model", headerName: t("computerScienceHardwarePage.model") },
    { field: "dieSize", headerName: t("computerScienceHardwarePage.dieSize") },
    { field: "modelCodename", headerName: t("computerScienceHardwarePage.modelCodename") },
    { field: "modelCodeName", headerName: t("computerScienceHardwarePage.modelCodeName") },
  ]
}

interface GPUListProps extends I18nTranslation {
  httpClientSettings: ApiHttpClientSettings
}

const pageSizeOptions = [5, 10, 20, 50, 100]
const paginationModel = { page: 1, pageSize: 10 }

function GPUListStyled({ gpuList }: { gpuList: GPU[] }) {
  const { t } = useTranslation()
  const { setStyleEngine } = useStyleEngine()
  const [openChangeStyleDialog, setOpenChangeStyleDialog] = useState<boolean>(false)
  const [gpuIdClicked, setGpuIdClicked] = useState<string>("")
  const [gpuDetailsStyleTemplate, setGpuDetailsStyleTemplate] = useState<string>("")

  function handleOpenChangeStyleClick() {
    setOpenChangeStyleDialog(true)
  }

  function handlePageStyleSettingsDialogSubmit({ savedStyleEngine, styleTemplate }: GpuDetailsUpdateCallbackParams) {
    setOpenChangeStyleDialog(false)
    setStyleEngine(savedStyleEngine)
    setGpuDetailsStyleTemplate(styleTemplate)
  }

  function handlePageStyleSettingsDialogClose() {
    setOpenChangeStyleDialog(false)
  }

  function handleOnRowClick(params: GridRowParams) {
    const id = params.id as string
    if (id !== gpuIdClicked) {
      setGpuIdClicked(id)
    }
  }

  const columns = createColumns(t)

  return (
    <div>
      <Button onClick={handleOpenChangeStyleClick}>{t("computerScienceHardwarePage.changeStyle")}</Button>
      {openChangeStyleDialog ? (
        <PageStyleSettingsDialog
          handleSubmit={handlePageStyleSettingsDialogSubmit}
          handleClose={handlePageStyleSettingsDialogClose}
        />
      ) : null}
      <p>Total GPUs={Object.keys(gpuList).length}</p>
      <Paper sx={{ height: 400, width: "100%" }}>
        <DataGrid
          rows={gpuList}
          columns={columns}
          initialState={{ pagination: { paginationModel } }}
          pageSizeOptions={pageSizeOptions}
          checkboxSelection
          sx={{ border: 0 }}
          onRowClick={handleOnRowClick}
        />
      </Paper>
      {gpuIdClicked && <GpuDetails id={gpuIdClicked} className={gpuDetailsStyleTemplate} />}
    </div>
  )
}

export default function GPUList({ lang, translations, httpClientSettings }: GPUListProps) {
  useEffect(() => {
    i18n.addResourceBundle(lang || "en", "translation", translations, true, true)
  }, [lang, translations])

  const httpClient = new GpuApiHttpClient(httpClientSettings)

  const { isPending, error, data } = useQuery({
    queryKey: ["gpuData"],
    queryFn: () => httpClient.getData(),
  })

  if (isPending) {
    return <p>Loading...</p>
  }

  if (error) {
    return <p>An error has occurred: ${error.message}</p>
  }

  const gpuList = mapToGPUList(data)
  return (
    <StyleEngineProvider>
      <GPUListStyled gpuList={gpuList} />
    </StyleEngineProvider>
  )
}
