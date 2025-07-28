import Dependencies.{persistenceApi, munit}
import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "3.7.1"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "org.example"
ThisBuild / organizationName := "experimental_hibernate"

lazy val stockMarketDb = (project in file("stock-market-db"))
  .settings(
      libraryDependencies ++= Seq(
          persistenceApi
      )
  )

lazy val app = (project in file("app"))
  .dependsOn(stockMarketDb)
  .settings(
      libraryDependencies ++= Seq(
          // https://mvnrepository.com/artifact/org.postgresql/postgresql
          "org.postgresql" % "postgresql" % "42.7.7",
          // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
          "org.hibernate.orm" % "hibernate-core" % "7.0.8.Final",
          munit % Test
      )
  )

lazy val root = (project in file("."))
  .aggregate(stockMarketDb, app)
  .settings(
      name := "experimental-hibernate",
  )
