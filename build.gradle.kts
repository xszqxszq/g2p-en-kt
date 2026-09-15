plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish") version "0.34.0"
}

java {
    withSourcesJar()
}

group = "xyz.xszq"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(22)
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("xyz.xszq", "g2p-en-kt", version as String)

    pom {
        name.set("g2p-en-kt")
        description.set("Pure Kotlin English grapheme-to-phoneme converter")
        inceptionYear.set("2026")
        url.set("https://github.com/xszqxszq/g2p-en-kt")
        licenses {
            license {
                name.set("The MIT License")
                url.set("https://opensource.org/license/mit")
            }
        }
        developers {
            developer {
                id.set("xszqxszq")
                name.set("xszqxszq")
                url.set("https://github.com/xszqxszq/")
            }
        }
        scm {
            url.set("https://github.com/xszqxszq/g2p-en-kt/")
            connection.set("scm:git:git://github.com/xszqxszq/g2p-en-kt.git")
            developerConnection.set("scm:git:ssh://git@github.com/xszqxszq/g2p-en-kt.git")
        }
    }
}
