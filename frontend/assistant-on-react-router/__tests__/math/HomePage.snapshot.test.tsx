import React from "react"
import "@testing-library/jest-dom"
import { render } from "@testing-library/react"
import Page from "~/math/Home"

describe("Math Home page snapshot", () => {
  test("renders home page", () => {
    const { container } = render(<Page />)
    expect(container).toMatchSnapshot()
  })
})
