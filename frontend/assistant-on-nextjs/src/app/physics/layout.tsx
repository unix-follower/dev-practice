import React from "react"

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <>
      <div>Physics Layout placeholder</div>
      {children}
    </>
  )
}
