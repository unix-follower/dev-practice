"use client"

import React from "react"
import { useSearchParams } from "next/navigation"
import { parsePaginationParams } from "@/app/components/utils/urlUtils"
import { useGetAllCompoundsQuery, useGetCompoundDataByNameQuery } from "@/lib/features/chemistry/pubChemGraphSlice"
import { mapGraphResponse } from "@/lib/api/chemistry/mapper"
import CompoundGraph from "@/app/components/chemistry/CompoundGraph"

export default function CompoundGraphWithRTK() {
  const searchParams = useSearchParams()

  const [page, pageSize] = parsePaginationParams(searchParams)
  const name = searchParams.get("name")!
  const { data } = useGetCompoundDataByNameQuery({ page, pageSize, name })

  if (!data) return null
  const compoundData = mapGraphResponse(data)
  return <CompoundGraph compoundData={compoundData} />
}

export function CompoundGraphListWithRTK() {
  const searchParams = useSearchParams()
  const [page, pageSize] = parsePaginationParams(searchParams)

  const { data } = useGetAllCompoundsQuery({ page, pageSize })
  if (!data) return null
  const compoundData = mapGraphResponse(data)
  return <CompoundGraph compoundData={compoundData} />
}
