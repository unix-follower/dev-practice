import { ReadonlyURLSearchParams } from "next/navigation"

interface PaginationParams {
  page: string | null
  size: string | null
}

export function parsePaginationParams(searchParams: ReadonlyURLSearchParams | PaginationParams) {
  let pageStr: string | null
  let sizeStr: string | null
  if (searchParams instanceof ReadonlyURLSearchParams) {
    pageStr = searchParams.get("page")
    sizeStr = searchParams.get("size")
  } else {
    pageStr = searchParams.page
    sizeStr = searchParams.size
  }

  let page = Number.parseInt(pageStr ?? "1")
  let size = Number.parseInt(sizeStr ?? "10")
  if (isNaN(page)) {
    page = 1
  }
  if (isNaN(page)) {
    size = 10
  }

  return [page, size]
}
