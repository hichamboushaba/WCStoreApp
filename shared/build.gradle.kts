import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

// Remove this when https://youtrack.jetbrains.com/issue/KTIJ-19369 gets fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("kotlin-kapt")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.rickclephas.kmp.nativecoroutines") version ("0.11.3")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildkonfig)
    id("dagger.hilt.android.plugin")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
//    iosSimulatorArm64()

    cocoapods {
        summary = "WCStoreApp business logic"
        homepage = "localhost"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "WCStoreAppKmm"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)
                api(libs.ktor)
                api(libs.ktor.serialization)
                api(libs.ktor.logging)
                api(libs.multiplatform.paging)
                implementation(libs.bignum)
                api(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.datastore)
                implementation(libs.lifecycle.viewmodel)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
//            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
//        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
//            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }

    dependencies {
        configurations.get("kapt").dependencies.add(
            org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency(
                "com.google.dagger",
                "hilt-compiler",
                libs.versions.hilt.get()
            )
        )
        implementation(libs.hilt.android)
    }
}

buildkonfig {
    packageName = "com.hicham.wcstoreapp"

    defaultConfigs {
        buildConfigField(
            STRING,
            "WC_URL",
            gradleLocalProperties(rootDir).getProperty("WC_URL", "")
        )
    }
}