import java.io.FileInputStream
import java.util.*

buildscript {
    val composeUiVersion by extra("1.6.8")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.1" apply false
    id("com.android.library") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

ext["ossrhUsername"] = ""
ext["ossrhPassword"] = ""
ext["sonatypeStagingProfileId"] = ""
ext["signing.secretKeyRingFile"] = ""
ext["signing.keyId"] = ""
ext["signing.password"] = ""

val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    // Use local.properties if exists
    Properties().apply {
        load(FileInputStream(secretPropsFile))
    }.forEach { name, value ->
        ext[name.toString()] = value
    }
} else {
    // Use system environment variables otherwise
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    ext["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
    ext["signing.secretKeyRingFile"] = System.getenv("SECRET_KEY_RING_FILE")
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["ossrhUsername"].toString())
            password.set(rootProject.ext["ossrhPassword"].toString())
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
