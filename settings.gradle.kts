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

// PrismalAGSL is built from the pinned upstream source fetched by CI.
includeBuild("third_party/PrismalAGSL")
includeBuild("third_party/Glasmio")

rootProject.name = "Graf"
include(":app")
