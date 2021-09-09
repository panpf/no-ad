// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven { setUrl("https://repo.huaweicloud.com/repository/maven/") }  // central, google, jcenter
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    }
}

allprojects {
    repositories {
        maven { setUrl("https://repo.huaweicloud.com/repository/maven/") }  // central, google, jcenter
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}