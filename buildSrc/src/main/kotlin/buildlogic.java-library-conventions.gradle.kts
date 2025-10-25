plugins {
    id("buildlogic.java-common-conventions")
    `java-library`

    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set(project.name)
        description.set("Integrations between the Swagger Request Validator and the Spring Framework WebTestClient.")
        url.set("https://github.com/chehsunliu/swagger-request-validator-spring-webflux")
        licenses {
            license {
                name.set("Apache License")
                url.set("https://opensource.org/license/apache-2-0")
            }
        }
        developers {
            developer {
                id.set("chehsunliu")
                name.set("Che-Hsun Liu")
                url.set("https://github.com/chehsunliu")
            }
        }
        scm {
            url.set("https://github.com/chehsunliu/swagger-request-validator-spring-webflux")
            connection.set("scm:git:git://github.com/chehsunliu/swagger-request-validator-spring-webflux.git")
            developerConnection.set("scm:git:ssh://git@github.com/chehsunliu/swagger-request-validator-spring-webflux.git")
        }
    }
}
