plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
}

dependencies {
    // https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api
    implementation("jakarta.persistence:jakarta.persistence-api:${providers.gradleProperty("persistenceApiVersion").get()}")
}
