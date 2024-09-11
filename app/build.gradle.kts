import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

android {
    if (keystorePropertiesFile.exists()) {
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        signingConfigs {
            getByName("debug") {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"].toString()
            }
        }
    }

    compileSdk = 34

    defaultConfig {
        applicationId = "com.jb.morser"
        minSdk = 21
        targetSdk = 34
        versionCode = 2

        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    applicationVariants.all {
        outputs.all {
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "MorSer-${defaultConfig.versionName}-${name}.apk"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            if (keystorePropertiesFile.exists()) signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            if (keystorePropertiesFile.exists()) signingConfig = signingConfigs.getByName("debug")
        }
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    namespace = "com.jb.morser"
}

kotlin {
    jvmToolchain(21)
}

dependencies {

    //Core libs for the app
    implementation(libs.bundles.core)

    //Lifecycle support for Jetpack Compose
    implementation(libs.androidx.lifecycle.runtimeCompose)

    //Material UI, Accompanist...
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidxCompose)

    //Unit testing libraries
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

    //UI debugging library for Jetpack Compose
    implementation(libs.androidx.compose.ui.tooling)
}