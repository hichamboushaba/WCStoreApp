// Remove this when https://youtrack.jetbrains.com/issue/KTIJ-19369 gets fixed
@Suppress(
    "DSL_SCOPE_VIOLATION"
)
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.hicham.wcstoreapp.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.material)
    implementation(libs.android.paging.runtime)
    implementation(libs.android.paging.compose)

    implementation(libs.bundles.compose)
    implementation(libs.navigation.compose)
    implementation(libs.font.tabler.icons)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.insets.ui)
    implementation(libs.accompanist.pager)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    implementation(libs.datastore)

    implementation(libs.bignum)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.compose.uitest)
    debugImplementation(libs.compose.uitooling)
}