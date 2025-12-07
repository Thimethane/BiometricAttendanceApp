// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Plugins are declared but not applied to allow each module to apply independently.

plugins {
    // Android Gradle Plugin
    id("com.android.application") version "8.13.1" apply false

    // Kotlin Android Plugin
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false

    // Kotlin Symbol Processing (KSP) Plugin
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

// Optional: Task to clean all modules
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
