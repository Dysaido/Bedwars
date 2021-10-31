plugins { kotlin("jvm") }

dependencies {
//    api("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") { exclude("junit", "junit") }
    api("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT") { exclude("junit", "junit") }
//    api("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT") { exclude("junit", "junit") }
    implementation("com.google.code.gson:gson:2.8.5")
//    api("com.comphenix.protocol:ProtocolLib:4.5.1") { exclude("junit", "junit") }
    testImplementation("junit:junit:4.13.1")
}