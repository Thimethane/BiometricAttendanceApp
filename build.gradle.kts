// Top-level Gradle configuration shared across all modules.
// Plugins are declared here but applied individually by each module.

plugins {
    // Android Gradle Plugin
    id("com.android.application") version "8.3.2" apply false

    // Kotlin Android Plugin — MUST match Compose compiler
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // Kotlin Symbol Processing (KSP) — MUST match the Kotlin version
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}

// Optional: Global clean task
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
