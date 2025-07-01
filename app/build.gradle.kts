import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

val baseVersionName = currentVersion.name

android {
    compileSdk = 36

    if (keystorePropertiesFile.exists()) {
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        signingConfigs {
            create("githubPublish") {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"].toString()
            }
        }
    }

    buildFeatures { buildConfig = true }

    defaultConfig {
        applicationId = "com.jb.morser"
        minSdk = 21
        targetSdk = 36
        versionCode = 5

        versionName = baseVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("githubPublish")
            }
        }
        debug {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("githubPublish")
            }
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "Morser Debug")
        }
    }

    applicationVariants.all {
        outputs.all {
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "MorSer-${defaultConfig.versionName}-${name}.apk"
        }
    }

    kotlin {
        compilerOptions {
            optIn.add("kotlin.RequiresOptIn")
        }
    }

    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }

    namespace = "com.jb.morser"
}

kotlin { jvmToolchain(21) }

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