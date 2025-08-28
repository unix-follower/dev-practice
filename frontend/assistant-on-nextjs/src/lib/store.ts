import { Action, combineSlices, ThunkAction } from "@reduxjs/toolkit"
import { configureStore } from "@reduxjs/toolkit"
import { pubChemFdaSlice } from "@/lib/features/chemistry/pubChemFdaSlice"
import { pubChemGraphSlice } from "@/lib/features/chemistry/pubChemGraphSlice"
import { stockMarketSlice } from "@/lib/features/finance/stockMarketSlice"
import { compoundGraphViewerSlice } from "@/lib/features/chemistry/compoundGraphViewerSlice"

const rootReducer = combineSlices(pubChemFdaSlice, stockMarketSlice, pubChemGraphSlice, compoundGraphViewerSlice)
export type RootState = ReturnType<typeof rootReducer>

export const setupStore = (preloadedState?: Partial<RootState>) => {
  return configureStore({
    reducer: rootReducer,
    preloadedState,
    middleware: (getDefaultMiddleware) => {
      return getDefaultMiddleware()
        .concat(pubChemFdaSlice.middleware)
        .concat(pubChemGraphSlice.middleware)
        .concat(stockMarketSlice.middleware)
    },
  })
}

export type AppStore = ReturnType<typeof setupStore>
export type AppDispatch = AppStore["dispatch"]
export type AppThunk<ThunkReturnType = void> = ThunkAction<ThunkReturnType, RootState, unknown, Action>
