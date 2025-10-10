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
        description.set("A Java library for data seeding, making data preparation for integration testing easier. ")
        url.set("https://github.com/chehsunliu/seeder.java")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
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
            url.set("https://github.com/chehsunliu/seeder.java")
            connection.set("scm:git:git://github.com/chehsunliu/seeder.java.git")
            developerConnection.set("scm:git:ssh://git@github.com/chehsunliu/seeder.java.git")
        }
    }
}
