plugins {
    `java-gradle-plugin`
    `java-library`
}

repositories {
    mavenCentral()
}

val asmVersion: "9.7"

dependencies {
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-tree:$asmVersion")

    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "de.hysky.skyblocker.annotation-processor"
            implementationClass = "com.nekiplay.hypixelcry.Processor"
        }
    }
}