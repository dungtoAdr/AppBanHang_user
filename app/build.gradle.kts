plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.appbanhang1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.appbanhang1"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation("com.google.firebase:firebase-firestore:24.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //glider
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.0")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")


    implementation ("androidx.core:core-ktx:latest_version")
    //badge
    implementation ("com.nex3z:notification-badge:1.0.4")

    //event bus
    implementation("org.greenrobot:eventbus:3.3.1")

    //paper
    implementation("io.github.pilgr:paperdb:2.7.2")

    //Gson
    implementation("com.google.code.gson:gson:2.10.1")

    //lotte
    implementation("com.airbnb.android:lottie:6.3.0")
    //momo
//    implementation("com.github.momo-wallet:mobile-sdk:1.0.7")
    //youtube
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    //slider image
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    //live
    implementation ("live.videosdk:rtc-android-sdk:0.1.21")

    // library to perform Network call to generate a meeting id
    implementation ("com.amitshekhar.android:android-networking:1.0.2")

    implementation ("com.google.android.exoplayer:exoplayer:2.18.5")
}