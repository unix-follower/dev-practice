import { useCytoscape } from "@/lib/chemistryHooks"
import { useAppDispatch, useAppSelector } from "@/lib/hooks"
import { getIsAddElementDialogOpen, setIsAddElementDialogOpen } from "@/lib/features/chemistry/compoundGraphViewerSlice"
import React from "react"
import Dialog from "@mui/material/Dialog"
import DialogTitle from "@mui/material/DialogTitle"
import DialogContent from "@mui/material/DialogContent"
import TextField from "@mui/material/TextField"
import DialogActions from "@mui/material/DialogActions"
import Button from "@mui/material/Button"
import Paper, { PaperProps } from "@mui/material/Paper"
import Draggable from "react-draggable"
import { useTranslation } from "react-i18next"

function PaperComponent(props: PaperProps) {
  const nodeRef = React.useRef<HTMLDivElement>(null)
  return (
    <Draggable
      nodeRef={nodeRef as React.RefObject<HTMLDivElement>}
      handle="#draggable-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <Paper {...props} ref={nodeRef} />
    </Draggable>
  )
}

const ID_FIELD = "id"
const LABEL_FIELD = "label"
const X_FIELD = "x"
const Y_FIELD = "y"

export default function DraggableDialog() {
  const cy = useCytoscape()
  const dispatch = useAppDispatch()
  const open = useAppSelector(getIsAddElementDialogOpen)
  const { t } = useTranslation()

  function handleClose() {
    dispatch(setIsAddElementDialogOpen(false))
  }

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const id = formData.get(ID_FIELD)!.toString()
    const label = formData.get(LABEL_FIELD)
    const x = Number.parseFloat(formData.get(X_FIELD)?.toString() || "0")
    const y = Number.parseFloat(formData.get(Y_FIELD)?.toString() || "0")

    if (cy) {
      cy.add({
        group: "nodes",
        data: {
          id,
          label,
          position: { x, y },
        },
      })
    }

    handleClose()
  }

  return (
    <Dialog open={open} onClose={handleClose} PaperComponent={PaperComponent} aria-labelledby="draggable-dialog-title">
      <DialogTitle style={{ cursor: "move" }} id="draggable-dialog-title">
        {t("chemistryPubChemGraphCompoundPage.addNode")}
      </DialogTitle>
      <DialogContent>
        <form onSubmit={handleSubmit} id="addNodeForm">
          <TextField
            autoFocus
            required
            margin="dense"
            id={ID_FIELD}
            name={ID_FIELD}
            label={t("chemistryPubChemGraphCompoundPage.nodeId")}
            type="text"
            fullWidth
            variant="standard"
          />
          <TextField
            autoFocus
            required
            margin="dense"
            id={LABEL_FIELD}
            name={LABEL_FIELD}
            label={t("chemistryPubChemGraphCompoundPage.nodeLabel")}
            type="text"
            fullWidth
            variant="standard"
          />
          <TextField
            autoFocus
            margin="dense"
            id={X_FIELD}
            name={X_FIELD}
            label="x"
            type="number"
            fullWidth
            variant="standard"
          />
          <TextField
            autoFocus
            margin="dense"
            id={Y_FIELD}
            name={Y_FIELD}
            label="y"
            type="number"
            fullWidth
            variant="standard"
          />
        </form>
      </DialogContent>
      <DialogActions>
        <Button type="submit" form="addNodeForm">
          {t("chemistryPubChemGraphCompoundPage.add")}
        </Button>
        <Button autoFocus onClick={handleClose}>
          {t("chemistryPubChemGraphCompoundPage.cancel")}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
