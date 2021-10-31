import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins { kotlin("jvm") version "1.4.21" apply false }

allprojects {
    apply(plugin = "java")
    group = "hu.dysaido"
    version = "1.0-SNAPSHOT"

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}
subprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public") }
//        maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
    }
    dependencies {
        "implementation"(kotlin("stdlib-jdk8"))
        "implementation"(kotlin("reflect"))
    }
}
