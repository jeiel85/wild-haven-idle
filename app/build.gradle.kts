plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.jeiel85.wildhavenidle"
    compileSdk = 36

    val releaseKeystorePath = providers.environmentVariable("ANDROID_RELEASE_KEYSTORE_PATH")
    val releaseKeystorePassword = providers.environmentVariable("ANDROID_RELEASE_KEYSTORE_PASSWORD")
    val releaseKeyAlias = providers.environmentVariable("ANDROID_RELEASE_KEY_ALIAS")
    val releaseKeyPassword = providers.environmentVariable("ANDROID_RELEASE_KEY_PASSWORD")

    defaultConfig {
        applicationId = "com.jeiel85.wildhavenidle"
        minSdk = 26
        targetSdk = 36
        versionCode = 9
        versionName = "0.6.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (
                releaseKeystorePath.isPresent &&
                releaseKeystorePassword.isPresent &&
                releaseKeyAlias.isPresent &&
                releaseKeyPassword.isPresent
            ) {
                storeFile = file(releaseKeystorePath.get())
                storePassword = releaseKeystorePassword.get()
                keyAlias = releaseKeyAlias.get()
                keyPassword = releaseKeyPassword.get()
            }
        }
    }

    buildTypes {
        release {
            // R8 코드 축소·난독화·최적화. AAB의 리소스도 함께 축소.
            // mapping.txt는 AGP가 자동으로 AAB 메타데이터에 포함하므로 Play Console이
            // 별도 업로드 없이 디오브퓨스케이션에 사용한다.
            isMinifyEnabled = true
            isShrinkResources = true
            if (
                releaseKeystorePath.isPresent &&
                releaseKeystorePassword.isPresent &&
                releaseKeyAlias.isPresent &&
                releaseKeyPassword.isPresent
            ) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
}
