plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

hilt {
    enableAggregatingTask = false
}

android {
    namespace = "edu.unikom.lupaminum"
    compileSdk = 35

    defaultConfig {
        applicationId = "edu.unikom.lupaminum"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    configurations.all {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
        }
    }
    //ViewModel & Livedata
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    implementation("androidx.fragment:fragment-ktx:1.8.7")

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation("com.google.code.gson:gson:2.10")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.loopj.android:android-async-http:1.4.9")

    implementation("com.google.android.gms:play-services-location:21.0.1")
}