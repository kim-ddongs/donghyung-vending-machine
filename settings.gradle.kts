plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "donghyung-vending-machine"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.toml"))
        }
    }
}