pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven{ url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven{ url = uri("https://jitpack.io") }
    }
}

rootProject.name = "signtest"
include(":app")
 