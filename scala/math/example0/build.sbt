import sbt.Keys.libraryDependencies

val slf4jVersion = "2.0.16"
val logbackVersion = "1.5.9"
val jmhVersion = "1.37"
val junitJupiterVersion = "5.10.1"

lazy val root = project
    .in(file("."))
    .settings(
        name := "example0",
        version := "1.0.0",

        scalaVersion := "3.5.1",

        libraryDependencies ++= Seq(
            "org.slf4j" % "slf4j-api" % slf4jVersion,
            "ch.qos.logback" % "logback-core" % logbackVersion,
            "ch.qos.logback" % "logback-classic" % logbackVersion,

            "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
            "org.junit.jupiter" % "junit-jupiter-engine" % junitJupiterVersion % Test
        ),
    )
