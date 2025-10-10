plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:com.diffplug.spotless.gradle.plugin:7.1.0")
    implementation("com.vanniktech.maven.publish:com.vanniktech.maven.publish.gradle.plugin:0.34.0")
}
