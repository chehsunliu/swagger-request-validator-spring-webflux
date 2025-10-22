plugins {
    id("buildlogic.java-library-conventions")
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
    compileOnly("org.springframework.boot:spring-boot-starter-test")
    api("com.atlassian.oai:swagger-request-validator-core:2.46.0")
}
