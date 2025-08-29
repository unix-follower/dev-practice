import React from "react"
import { useTranslation } from "react-i18next"
import { useAppDispatch, useAppSelector } from "@/lib/hooks"
import {
  getIsAddElementDialogOpened,
  getIsRemoveElementDialogOpened,
  getTappedNodeId,
  setIsAddElementDialogOpened,
  setIsRemoveElementDialogOpened,
  setTappedNodeId,
} from "@/lib/features/chemistry/compoundGraphViewerSlice"
import Button from "@mui/material/Button"
import AddNodeDraggableDialog from "./AddNodeDraggableDialog"
import RemoveNodeDraggableDialog from "./RemoveNodeDraggableDialog"
import { useCytoscape } from "@/lib/chemistryHooks"

export default function ToolPanel() {
  const { t } = useTranslation()
  const cy = useCytoscape()
  const dispatch = useAppDispatch()
  const isAddElementDialogOpened = useAppSelector(getIsAddElementDialogOpened)
  const isRemoveElementDialogOpened = useAppSelector(getIsRemoveElementDialogOpened)
  const tappedNodeId = useAppSelector(getTappedNodeId)

  function handleAddNodeClick() {
    dispatch(setIsAddElementDialogOpened(true))
  }

  function handleRemoveNodeClick() {
    if (tappedNodeId) {
      cy?.nodes()
        .filter((ele) => ele.id() == tappedNodeId)
        .first()
        .remove()
      return
    }
    dispatch(setIsRemoveElementDialogOpened(true))
    dispatch(setTappedNodeId(null))
  }

  return (
    <div id="compound-graph-viewer-tool-panel" className="grid grid-cols-1 grid-rows-3">
      <div>
        <span>{t("chemistryPubChemGraphCompoundPage.graphManipulation")}</span>
        <div>
          <Button variant="outlined" onClick={handleAddNodeClick}>
            {t("chemistryPubChemGraphCompoundPage.add")}
          </Button>
          {isAddElementDialogOpened && <AddNodeDraggableDialog />}
        </div>
        <div>
          <Button variant="outlined" onClick={handleRemoveNodeClick}>
            {t("chemistryPubChemGraphCompoundPage.remove")}
          </Button>
          {isRemoveElementDialogOpened && <RemoveNodeDraggableDialog />}
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
