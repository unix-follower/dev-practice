plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
}

dependencies {
    implementation(project(":stock-market-db"))

    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:${providers.gradleProperty("postgresqlVersion").get()}")
    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
    implementation("org.hibernate.orm:hibernate-core:${providers.gradleProperty("hibernateCoreVersion").get()}")
}
