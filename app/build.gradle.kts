plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

tasks.register("downloadCertificate") {
    val destFile = file("$rootDir/duck-sample.keystore")
    if (!destFile.exists()) {
        val sourceUrl = "https://ya.cc/5y6KsF"
        ant.invokeMethod("get", mapOf("src" to sourceUrl, "dest" to destFile))
    }
}
tasks.preBuild {
    dependsOn("downloadCertificate")
}

android {
    namespace = "com.example.ducksample"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    signingConfigs {
        create("duckSampleKey") {
            storeFile = file("$rootDir/duck-sample.keystore")
            storePassword = "duck_android"
            keyAlias = "duck-sample"
            keyPassword = "duck_android"
        }
    }

    defaultConfig {
        applicationId = "com.example.ducksample"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "1628af994c6a4be8803c4600215a7f71"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("duckSampleKey")
        }
        release {
            signingConfig = signingConfigs.getByName("duckSampleKey")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // android core and ui:
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recycler)
    implementation(libs.androidx.constraint)
    implementation(libs.androidx.cardview)
    // network processing:
    implementation(libs.squareup.okhttp)
    implementation(libs.kotlin.serialization)
    // yandex pay and ui:
    implementation(libs.yandexpay)
    implementation(libs.yandexwidgets)
    implementation(libs.yandexbadges)
}
