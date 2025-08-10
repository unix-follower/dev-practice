import type { Action, ThunkAction } from "@reduxjs/toolkit"
import { combineSlices, configureStore } from "@reduxjs/toolkit"
import { pubChemFdaSlice } from "@/lib/features/chemistry/pubChemFdaSlice"
import { stockMarketSlice } from "@/lib/features/finance/stockMarketSlice"

const rootReducer = combineSlices(pubChemFdaSlice, stockMarketSlice)
export type RootState = ReturnType<typeof rootReducer>

export const makeStore = () => {
  return configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) => {
      return getDefaultMiddleware().concat(pubChemFdaSlice.middleware).concat(stockMarketSlice.middleware)
    },
  })
}

export type AppStore = ReturnType<typeof makeStore>
export type AppDispatch = AppStore["dispatch"]
export type AppThunk<ThunkReturnType = void> = ThunkAction<ThunkReturnType, RootState, unknown, Action>
