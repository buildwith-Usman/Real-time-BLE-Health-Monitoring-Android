plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.hilt.android)

}

android {
    namespace = "com.app.dbraze"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.app.dbraze"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //navigation components
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    //dagger-hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.savedstate)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Firebase SDK dependencies
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)

    //SplashScreen
    implementation(libs.androidx.core.splashscreen)

    //lottie
    implementation(libs.lottie)


}