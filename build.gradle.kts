plugins {
    id("fpgradle-minecraft") version("0.7.5")
}

group = "mega"

minecraft_fp {
    java {
        compatibility = jabel
    }
    mod {
        modid   = "blendtronic"
        name    = "Blendtronic"
        rootPkg = "$group.blendtronic"
    }
    mixin {
        pkg          = "mixin.mixins"
        pluginClass  = "mixin.plugin.MixinPlugin"
    }
    core {
        accessTransformerFile = "blendtronic_at.cfg"
        coreModClass = "asm.CoreLoadingPlugin"
    }

    tokens {
        tokenClass = "Tags"
    }

    publish {
        maven {
            repoUrl  = "https://mvn.falsepattern.com/gtmega_releases"
            repoName = "mega"
        }
    }
}

repositories {
    cursemavenEX()
    exclusive(mavenpattern(), "com.falsepattern")
    exclusive(maven("mega_uploads", "https://mvn.falsepattern.com/gtmega_uploads"), "optifine")
    exclusive(maven("horizon", "https://mvn.falsepattern.com/horizon"), "com.gtnewhorizons.retrofuturabootstrap")
}

dependencies {
    implementationSplit("com.falsepattern:falsepatternlib-mc1.7.10:1.4.0")

    compileOnly("com.gtnewhorizons.retrofuturabootstrap:RetroFuturaBootstrap:1.0.7")

    compileOnly(deobf("optifine:optifine:1.7.10_hd_u_e7"))

    implementation("it.unimi.dsi:fastutil:8.5.13")

    // Extra Utilities 1.2.12
    compileOnly(deobfCurse("extra-utilities-225561:2264383"))

    // BuildCraft 7.1.25
    compileOnly(deobfCurse("buildcraft-61811:4055732"))

    // Thaumcraft 4.2.3.5
    compileOnly(deobfCurse("thaumcraft-223628:2227552"))

    // BiblioCraft 1.11.7
    compileOnly(deobfCurse("bibliocraft-228027:2423369"))
}