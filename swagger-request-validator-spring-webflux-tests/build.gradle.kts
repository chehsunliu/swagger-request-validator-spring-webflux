plugins {
    id("buildlogic.java-app-conventions")
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.jetbrains:annotations:26.0.2")

    testImplementation(project(":swagger-request-validator-spring-webflux"))
}
