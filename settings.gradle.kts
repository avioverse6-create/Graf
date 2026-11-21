pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Build PrismalAGSL from its tagged source instead of relying on its JitPack binary.
includeBuild("third_party/PrismalAGSL")

rootProject.name = "Graf"
include(":app")
