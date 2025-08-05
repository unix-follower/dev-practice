import React from "react"

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <>
      <div>Vehicles Layout placeholder</div>
      {children}
    </>
  )
}
