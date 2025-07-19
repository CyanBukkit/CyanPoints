// 06
plugins {
    kotlin("jvm") version "1.9.20"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}
//gradle clean --refresh-dependencies shadowJar

version = 1.3

repositories {
    maven("https://nexus.cyanbukkit.cn/repository/maven-public")
    maven("https://maven.elmakers.com/repository")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.6")// https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}


bukkit {
    main = "cn.cyanbukkit.points.CyanPoints"
    name = "CyanPoints"
    version = project.version.toString()
    description = ""
    website = "https://cyanbukkit.net"
    depend = listOf("LuckPerms","Vault")
}

kotlin {
    jvmToolchain(8)
}


tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("CyanPoints-${project.version}.jar")
    }

}
