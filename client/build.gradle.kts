plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    signing
    id("kotlin-parcelize")
}

android {
    namespace = "app.kula.onlaunch.client"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        proguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "app.kula"
            artifactId = "onlaunch-android-client"
            version = "0.0.5"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("onlaunch-android-client")
                description.set("Official OnLaunch Android client")
                url.set("https://github.com/kula-app/OnLaunch-Android-Client")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/kula-app/OnLaunch-Android-Client/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("DamianJaeger")
                        name.set("Damian Jäger")
                        email.set("damian@kula.app")
                    }
                }
                scm {
                    connection.set("scm:git:github.com:kula-app/OnLaunch-Android-Client.git")
                    developerConnection.set("scm:git:github.com:kula-app/OnLaunch-Android-Client.git")
                    url.set("https://github.com/kula-app/OnLaunch-Android-Client")
                }
            }
        }
    }
}

ext["signing.secretKeyRingFile"] = rootProject.ext["signing.secretKeyRingFile"]
ext["signing.keyId"] = rootProject.ext["signing.keyId"]
ext["signing.password"] = rootProject.ext["signing.password"]

signing {
    sign(publishing.publications)
}

dependencies {
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.compose.material:material:1.4.2")
    implementation("androidx.compose.ui:ui:${rootProject.extra["composeUiVersion"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["composeUiVersion"]}")

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
