architectury {
    common((enabled_platforms).split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/$mod_id.accesswidener"))
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    implementation("net.java.dev.jna:jna:5.13.0")

    modImplementation("maven.modrinth:sodium:mc1.21.1-0.6.13-fabric") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    modImplementation("maven.modrinth:iris:1.8.8+1.21.1-fabric") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.3.7-beta.1")!!)
    modCompileOnly(fabricApi.module("fabric-renderer-api-v1", fabric_api_version))
}