"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"

import styles from "@/app/styles/layout.module.css"

export default function Menu() {
  const pathname = usePathname()

  return (
    <nav data-testid="main-menu" className={styles.nav}>
      <Link className={`${styles.link} ${pathname === "/" ? styles.active : ""}`} href="/">
        Home
      </Link>
      <Link
        className={`${styles.link} ${pathname === "/chemistry/pub-chem" ? styles.active : ""}`}
        href="/chemistry/organic/dbs/pub-chem"
      >
        Organic chemistry - PubChem db food additives
      </Link>
    </nav>
  )
}
