plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.example.ducksample"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.ducksample"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "insert_your_proper_client_ID_here"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
