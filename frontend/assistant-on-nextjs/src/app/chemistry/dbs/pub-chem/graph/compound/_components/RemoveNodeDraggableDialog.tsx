import { useCytoscape } from "@/lib/chemistryHooks"
import { useAppDispatch, useAppSelector } from "@/lib/hooks"
import {
  getIsRemoveElementDialogOpened,
  setIsRemoveElementDialogOpened,
} from "@/lib/features/chemistry/compoundGraphViewerSlice"
import React from "react"
import Dialog from "@mui/material/Dialog"
import DialogTitle from "@mui/material/DialogTitle"
import DialogContent from "@mui/material/DialogContent"
import TextField from "@mui/material/TextField"
import DialogActions from "@mui/material/DialogActions"
import Button from "@mui/material/Button"
import { useTranslation } from "react-i18next"
import PaperComponent from "./PaperComponent"
import { DialogContentText } from "@mui/material"

const SELECTOR_FIELD = "selector"

export default function RemoveNodeDraggableDialog() {
  const { t } = useTranslation()
  const cy = useCytoscape()
  const dispatch = useAppDispatch()
  const open = useAppSelector(getIsRemoveElementDialogOpened)

  function handleClose() {
    dispatch(setIsRemoveElementDialogOpened(false))
  }

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const selector = formData.get(SELECTOR_FIELD)!.toString()

    if (cy) {
      cy.remove(selector)
    }

    handleClose()
  }

  return (
    <Dialog open={open} onClose={handleClose} PaperComponent={PaperComponent} aria-labelledby="draggable-dialog-title">
      <DialogTitle style={{ cursor: "move" }} id="draggable-dialog-title">
        {t("chemistryPubChemGraphCompoundPage.removeNode")}
      </DialogTitle>
      <DialogContent>
        <DialogContentText>
          <h1>{t("chemistryPubChemGraphCompoundPage.examples")}</h1>
          <p>[id = &#34;1125899906842627&#34;]</p>
          <p>
            [label *= &#34;Compound&#34;] - {t("chemistryPubChemGraphCompoundPage.removeCompoundsByLblDescription")}
          </p>
          <p>[label *= &#34;Element&#34;] - {t("chemistryPubChemGraphCompoundPage.removeElementByLblDescription")}</p>
        </DialogContentText>
        <form onSubmit={handleSubmit} id="removeNodeForm">
          <TextField
            autoFocus
            required
            margin="dense"
            id={SELECTOR_FIELD}
            name={SELECTOR_FIELD}
            label={t("chemistryPubChemGraphCompoundPage.selector")}
            type="text"
            fullWidth
            variant="standard"
          />
        </form>
      </DialogContent>
      <DialogActions>
        <Button type="submit" form="removeNodeForm">
          {t("chemistryPubChemGraphCompoundPage.remove")}
        </Button>
        <Button autoFocus onClick={handleClose}>
          {t("chemistryPubChemGraphCompoundPage.cancel")}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
