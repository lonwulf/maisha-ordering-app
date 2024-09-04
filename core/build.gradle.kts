plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.parcelize.plugin)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("io.objectbox")
}

android {
    namespace = "com.lonwulf.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    api(libs.androidx.core.ktx)
    implementation(libs.multidex)
    implementation(libs.paging)
    implementation(libs.shared.prefs.encryption)
    implementation(libs.lifecycle.extensions)
    implementation(libs.work.manager)

    api(libs.hilt.android)
    api(libs.compose.viewmodel)
    api(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler.android)
    api(libs.retrofit)
    api(libs.timber)
    implementation(libs.moshi.converter)
    implementation(libs.logging.interceptor)
    api(libs.moshi)
    debugImplementation(libs.objectbox.browser)
    releaseImplementation(libs.objectbox.android)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}