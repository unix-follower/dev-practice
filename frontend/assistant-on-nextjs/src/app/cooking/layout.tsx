import React from "react"

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <>
      <div>Cooking Layout placeholder</div>
      {children}
    </>
  )
}
