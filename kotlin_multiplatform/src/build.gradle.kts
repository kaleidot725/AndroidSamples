buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("com.squareup.sqldelight:gradle-plugin:1.2.0")
    }
}
group = "jp.kaleidot725.sample"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
