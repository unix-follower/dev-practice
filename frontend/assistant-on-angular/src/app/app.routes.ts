import { Routes } from "@angular/router"
import FoodAdditives from "@/app/components/food-additives/food-additives"
import { App } from "./app"

export const routes: Routes = [
  {
    path: "",
    component: App,
    title: "Assistant on Angular",
  },
  {
    path: "chemistry/food-additives",
    component: FoodAdditives,
    title: "Assistant on Angular",
  },
]
