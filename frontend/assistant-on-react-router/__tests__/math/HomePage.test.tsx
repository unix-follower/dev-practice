import React from "react"
import "@testing-library/jest-dom"
import { render, screen } from "@testing-library/react"
import Page from "~/math/Home"

describe("Math Home page", () => {
  test("renders home page", () => {
    // given
    render(<Page />)

    // then
    const menu = screen.getByText("Home placeholder")
    expect(menu).toBeInTheDocument()
  })
})
