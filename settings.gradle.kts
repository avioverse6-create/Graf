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
        // JitPack hosts GitHub-hosted artifacts; do not query Maven Central for them.
        maven(url = "https://jitpack.io") {
            content {
                includeGroup("com.github.styropyr0.PrismalAGSL")
            }
        }
        mavenCentral {
            content {
                excludeGroup("com.github.styropyr0.PrismalAGSL")
            }
        }
    }
}

rootProject.name = "Graf"
include(":app")
