plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    signing
    id("kotlin-parcelize")
}

android {
    namespace = "app.kula.onlaunch.client"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34

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
            version = "0.0.7"

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
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.compose.material:material:1.6.8")
    implementation("androidx.compose.ui:ui:${rootProject.extra["composeUiVersion"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["composeUiVersion"]}")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // In-app updates
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
