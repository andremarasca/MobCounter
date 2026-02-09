rootProject.name = "MobCounter"

plugins {
    // See documentation on https://scaffoldit.dev
    id("dev.scaffoldit") version "0.2.+"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {
        // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
    }

    dependencies {
        // Any external dependency you also want to include
    }

    manifest {
        Group = "Marasca"
        Name = "MobCounter"
        Version = "2026.1.20-5708"
        Main = "com.marasca.mobcounter.MobCounter"
        IncludesAssetPack = true
        Website = "https://www.curseforge.com/hytale/mods/mobcounter"
        Dependencies = mapOf(
            "Hytale:EntityModule" to "*",
            "Hytale:AssetModule" to "*"
        )
    }
}