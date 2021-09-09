plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.github.panpf.noad"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility=JavaVersion.VERSION_1_8
        targetCompatibility= JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("io.github.panpf.tools4a:tools4a-service-ktx:1.0.0-rc03")
    implementation("io.github.panpf.tools4a:tools4a-toast-ktx:1.0.0-rc03")
}