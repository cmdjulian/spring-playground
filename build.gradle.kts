import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Kotlin
    val kotlinVersion = "1.7.22"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    // Spring
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.graalvm.buildtools.native") version "0.9.19"
}

group = "de.cmdjulian"
version = "1.0.0"

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    // utils
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // database
    runtimeOnly("com.h2database:h2")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // JUnit
    implementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // MockK
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
}

tasks.register("bootRunWithNativeImageAgent") {
    group = "application"
    description = "Runs bootRun with GraalVM tracing agent enabled"
    doFirst {
        tasks.bootRun {
            jvmArgs!!.plusAssign("-agentlib:native-image-agent=config-output-dir=native-image")
        }
    }
    finalizedBy("bootRun")
}

tasks.register("nativeCompileStatic") {
    group = "application"
    description = "executes the graalvm native image build and produce a statically linked native image binary"
    doFirst {
        graalvmNative {
            binaries {
                named("main") {
                    buildArgs("--static", "--libc=musl")
                }
            }
        }
    }
    finalizedBy("nativeCompile")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        javaParameters = true
        freeCompilerArgs += listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations", "-java-parameters", "-Xjvm-default=all", "-Xcontext-receivers")
        jvmTarget = "${JavaVersion.VERSION_17}"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

graalvmNative {
    agent {
        defaultMode.set("standard")
    }
    toolchainDetection.set(false)
    binaries {
        all {
            resources.autodetect()
        }
    }
    metadataRepository {
        enabled.set(true)
    }
}

tasks {
    bootBuildImage {
        builder.set("paketobuildpacks/builder:tiny")
        environment.set(mapOf("BP_JVM_VERSION" to "17", "BP_NATIVE_IMAGE" to "true"))
    }
}
