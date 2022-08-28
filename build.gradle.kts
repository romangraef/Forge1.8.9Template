plugins {
    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.example.archloomtemplate"
version = "1.0.0"

// Toolchains:
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

// Minecraft configuration:
loom {
    log4jConfigs.from(file("log4j2.xml"))
    launchConfigs {
        "client" {
            // If you don't want mixins, remove these lines
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            arg("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
            arg("--mixin", "mixins.examplemod.json")
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        // If you don't want mixins, remove this lines
        mixinConfig("mixins.examplemod.json")
    }
    // If you don't want mixins, remove these lines
    mixin {
        defaultRefmapName.set("mixins.examplemod.refmap.json")
    }
}

sourceSets.main {
    output.setResourcesDir(file("$buildDir/classes/java/main"))
}

// Dependencies:

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    // If you don't want to log in with your real minecraft account, remove this line
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:1.12.2")
    mappings("de.oceanlabs.mcp:mcp_stable:39-1.12")
    forge("net.minecraftforge:forge:1.12.2-14.23.5.2847") // For some reason it cant find a version newer than 2847

    // If you don't want mixins, remove these lines
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:0.8.4-SNAPSHOT")

    // If you don't want to log in with your real minecraft account, remove this line
    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.1.0")

}

// Tasks:

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(Jar::class) {
    archiveBaseName.set("examplemod")
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"

        // If you don't want mixins, remove these lines
        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "mixins.examplemod.json"
    }
}


val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("all")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}

tasks.shadowJar {
    archiveClassifier.set("all-dev")
    configurations = listOf(shadowImpl)
    doLast {
        configurations.forEach {
            println("Config: ${it.files}")
        }
    }

    // If you want to include other dependencies and shadow them, you can relocate them in here
    fun relocate(name: String) = relocate(name, "com.examplemod.deps.$name")
}

tasks.assemble.get().dependsOn(tasks.remapJar)

