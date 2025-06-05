plugins {
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

val asmVersion: String by project

dependencies {
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-tree:$asmVersion")
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "de.hysky.skyblocker.annotation-processor"
            implementationClass = "com.nekiplay.hypixelcry.Processor"
        }
    }
}