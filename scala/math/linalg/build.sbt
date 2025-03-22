import sbt.Keys.libraryDependencies

val slf4jVersion = "2.0.16"
val logbackVersion = "1.5.9"
val commonsMath3Version = "3.6.1"

lazy val root = project
    .in(file("."))
    .settings(
        name := "linear_systems",
        version := "1.0.0",

        scalaVersion := "3.5.1",

        libraryDependencies ++= Seq(
            "org.slf4j" % "slf4j-api" % slf4jVersion,
            "ch.qos.logback" % "logback-core" % logbackVersion,
            "ch.qos.logback" % "logback-classic" % logbackVersion,
            "org.apache.commons" % "commons-math3" % commonsMath3Version,
        ),
    )
