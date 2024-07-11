plugins {
    id("fpgradle-minecraft") version("0.4.0")
}

group = "mega"

minecraft_fp {
    java {
        compatibility = modern
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
        coreModClass = "asm.CoreLoadingPlugin"
    }

    tokens {
        tokenClass = "Tags"
    }

    publish {
        maven {
            repoUrl  = "https://mvn.falsepattern.com/gtmega_releases"
            repoName = "mega"
            group = "gtmega"
        }
    }
}

repositories {
    cursemavenEX()
    exclusive(mavenpattern(), "com.falsepattern")
}

dependencies {
    implementationSplit("com.falsepattern:falsepatternlib-mc1.7.10:1.2.5")

    // Extra Utilities 1.2.12
    compileOnly(deobfCurse("extra-utilities-225561:2264383"))

    // BuildCraft 7.1.25
    compileOnly(deobfCurse("buildcraft-61811:4055732"))

    // Thaumcraft 4.2.3.5
    compileOnly(deobfCurse("thaumcraft-223628:2227552"))
}