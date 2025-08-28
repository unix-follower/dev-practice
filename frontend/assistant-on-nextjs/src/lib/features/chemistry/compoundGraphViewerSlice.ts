import { createSlice } from "@reduxjs/toolkit"
import type { PayloadAction } from "@reduxjs/toolkit"
import { RootState } from "@/lib/store"

export interface CompoundGraphViewerState {
  isAddElementDialogOpen: boolean
}

const initialState: CompoundGraphViewerState = {
  isAddElementDialogOpen: false,
}

export const compoundGraphViewerSlice = createSlice({
  name: "compoundGraphViewer",
  initialState,
  reducers: {
    setIsAddElementDialogOpen: (state, action: PayloadAction<boolean>) => {
      state.isAddElementDialogOpen = action.payload
    },
  },
})

export default compoundGraphViewerSlice.reducer

export const { setIsAddElementDialogOpen } = compoundGraphViewerSlice.actions

export const getIsAddElementDialogOpen = (state: RootState) => state.compoundGraphViewer.isAddElementDialogOpen
