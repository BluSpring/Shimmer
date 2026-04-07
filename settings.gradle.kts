pluginManagement {
    repositories {
        mavenCentral()
        maven (  "https://maven.fabricmc.net/" )
        maven (  "https://maven.architectury.dev/" )
        maven (  "https://maven.minecraftforge.net/" )
        gradlePluginPortal()
    }
    plugins{
        id("architectury-plugin") version ("3.4-SNAPSHOT") apply false
        id("dev.architectury.loom") version ("1.11-SNAPSHOT") apply false
        id("com.gradleup.shadow").version("9.4.1").apply(false)
    }
}

include("Common")
include("Fabric")
include("NeoForge")

rootProject.name = "Shimmer"
