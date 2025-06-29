// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //id("dagger.hilt.android.plugin") version "2.48"
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven{
             url = uri( "https://jitpack.io")
        }
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}


