import React, { useState } from "react"
import Dialog from "@mui/material/Dialog"
import DialogTitle from "@mui/material/DialogTitle"
import DialogContent from "@mui/material/DialogContent"
import DialogActions from "@mui/material/DialogActions"
import Button from "@mui/material/Button"
import Radio from "@mui/material/Radio"
import RadioGroup from "@mui/material/RadioGroup"
import FormControlLabel from "@mui/material/FormControlLabel"
import FormLabel from "@mui/material/FormLabel"
import Switch from "@mui/material/Switch"
import { useTranslation } from "react-i18next"
import PaperComponent from "@/app/_components/common/PaperComponent"
import { StyleEngine } from "@/lib/hooks/styleEngineHooks"
import GpuDetailsUpdateCallbackParams from "./model"

interface PageStyleSettingsDialogProps {
  handleSubmit: (params: GpuDetailsUpdateCallbackParams) => void
  handleClose: () => void
}

const CSS_ENGINE_FIELD = "cssEngine"

export default function PageStyleSettingsDialog({ handleSubmit, handleClose }: PageStyleSettingsDialogProps) {
  const { t } = useTranslation()
  const [open, setOpen] = useState<boolean>(true)

  function handleDialogClose() {
    setOpen(false)
    handleClose()
  }

  function handleDialogSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const savedStyleEngine = formData.get(CSS_ENGINE_FIELD)!.toString() as StyleEngine
    let styleTemplate = "template1"
    if (savedStyleEngine === "tw") {
      styleTemplate = "tw-template1"
    }
    handleSubmit({ savedStyleEngine, styleTemplate })
    handleDialogClose()
  }

  return (
    <Dialog
      open={open}
      onClose={handleDialogClose}
      PaperComponent={PaperComponent}
      aria-labelledby="draggable-dialog-title"
    >
      <DialogTitle style={{ cursor: "move" }} id="draggable-dialog-title">
        {t("computerScienceHardwarePage.changeStyle")}
      </DialogTitle>
      <DialogContent>
        <form onSubmit={handleDialogSubmit} id="changeStyleForm">
          <FormLabel id="css-engine-row-radio-buttons-group-label">{t("computerScienceHardwarePage.engine")}</FormLabel>
          <RadioGroup
            row
            aria-labelledby="css-engine-row-radio-buttons-group-label"
            name={CSS_ENGINE_FIELD}
            defaultValue="css"
          >
            <FormControlLabel
              value="css"
              control={<Radio />}
              label={t("computerScienceHardwarePage.native") + " CSS"}
            />
            <FormControlLabel value="tw" control={<Radio />} label="Tailwind" />
          </RadioGroup>
          <div>{t("computerScienceHardwarePage.typography")}</div>
          <FormControlLabel
            value="bottom"
            control={<Switch color="primary" slotProps={{ input: { "aria-label": "controlled" } }} disabled={true} />}
            label={t("computerScienceHardwarePage.fontWeightBold")}
            labelPlacement="start"
          />
          <div>{t("computerScienceHardwarePage.colors")}</div>
          <div>{t("computerScienceHardwarePage.spacing")}</div>
          <div>{t("computerScienceHardwarePage.states")}</div>
          <div>{t("computerScienceHardwarePage.flexboxAndGrid")}</div>
          <div>{t("computerScienceHardwarePage.sizing")}</div>
          <div>{t("computerScienceHardwarePage.backgrounds")}</div>
          <div>{t("computerScienceHardwarePage.borders")}</div>
          <div>{t("computerScienceHardwarePage.responsive")}</div>
          <div>{t("computerScienceHardwarePage.layout")}</div>
          <div>{t("computerScienceHardwarePage.filtersAndEffects")}</div>
          <div>{t("computerScienceHardwarePage.tables")}</div>
          <div>{t("computerScienceHardwarePage.interactivity")}</div>
          <div>{t("computerScienceHardwarePage.transitionsAndAnimations")}</div>
          <div>{t("computerScienceHardwarePage.transforms")}</div>
        </form>
      </DialogContent>
      <DialogActions>
        <Button type="submit" form="changeStyleForm">
          {t("common.save")}
        </Button>
        <Button autoFocus onClick={handleDialogClose}>
          {t("common.cancel")}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
