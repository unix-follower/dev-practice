import React from "react"

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <>
      <div>weapons-and-armor Layout placeholder</div>
      {children}
    </>
  )
}
