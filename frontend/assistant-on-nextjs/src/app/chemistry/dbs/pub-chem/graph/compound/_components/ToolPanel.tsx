import React from "react"
import { useTranslation } from "react-i18next"
import { useAppDispatch, useAppSelector } from "@/lib/hooks"
import { getIsAddElementDialogOpen, setIsAddElementDialogOpen } from "@/lib/features/chemistry/compoundGraphViewerSlice"
import Button from "@mui/material/Button"
import DraggableDialog from "./DraggableDialog"

export default function ToolPanel() {
  const dispatch = useAppDispatch()
  const isAddElementDialogOpen = useAppSelector(getIsAddElementDialogOpen)
  const { t } = useTranslation()

  function handleAddElementClick() {
    dispatch(setIsAddElementDialogOpen(true))
  }

  return (
    <div id="compound-graph-viewer-tool-panel" className="grid grid-cols-1 grid-rows-3">
      <div>
        <span>{t("chemistryPubChemGraphCompoundPage.graphManipulation")}</span>
        <div>
          <Button variant="outlined" onClick={handleAddElementClick}>
            {t("chemistryPubChemGraphCompoundPage.add")}
          </Button>
          {isAddElementDialogOpen && <DraggableDialog />}
        </div>
      </div>
      <div>
        <span>{t("chemistryPubChemGraphCompoundPage.data")}</span>
      </div>
      <div>
        <span>{t("chemistryPubChemGraphCompoundPage.events")}</span>
      </div>
      <div>
        <span>{t("chemistryPubChemGraphCompoundPage.viewportManipulation")}</span>
      </div>
    </div>
  )
}
