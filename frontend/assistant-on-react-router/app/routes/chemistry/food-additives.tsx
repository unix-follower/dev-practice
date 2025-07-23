import React from "react"
import type { Route } from "./+types/food-additives"
import { useLoaderData } from "react-router"
import FoodAdditives from "~/components/food-additives/FoodAdditives"
import type { FoodAdditiveSubstanceResponseDto } from "~/lib/features/chemistry/food-additives/model/FoodAdditiveResponse"
import data from "~/components/food-additives/food-additives-sample.json"

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export function meta(_: Route.MetaArgs) {
  return [{ title: "Substances" }, { name: "description", content: "PubChem Food additive substances" }]
}

async function loadData(): Promise<FoodAdditiveSubstanceResponseDto> {
  return data as unknown as FoodAdditiveSubstanceResponseDto
}

export async function loader() {
  const foodAdditives = await loadData()
  return { foodAdditives }
}

export default function Page() {
  const { foodAdditives } = useLoaderData<typeof loader>()
  return <FoodAdditives foodAdditives={foodAdditives} />
}
