import sbt.*

object Dependencies {
    // https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api
    val persistenceApi = "jakarta.persistence" % "jakarta.persistence-api" % "3.2.0"
    val munit = "org.scalameta" %% "munit" % "0.7.29"
}
