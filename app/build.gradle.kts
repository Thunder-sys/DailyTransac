plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.example.dailytransac"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dailytransac"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
    viewBinding{
        enable = true
    }
    buildFeatures{
        viewBinding = true
    }
    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Chart
    implementation("com.github.Philjay:MPAndroidChart:v3.1.0")
    implementation("com.github.AnyChart:AnyChart-Android:1.1.2")
    implementation("androidx.multidex:multidex:2.0.1")


    //sign in google
    implementation("com.google.firebase:firebase-bom:33.1.1")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //calender
    implementation("androidx.work:work-runtime-ktx:2.7.0")

    //fragment
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.viewpager:viewpager:1.0.0")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1") // Check for the latest version
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1") // Optional, if you're using LiveData
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")



    //Simmer Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //Circular Image
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.sun.mail:android-mail:1.6.6")
    implementation("com.sun.mail:android-activation:1.6.7")


}