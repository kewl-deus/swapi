import com.palantir.gradle.docker.DockerExtension
import com.palantir.gradle.docker.DockerRunExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.palantir.docker") version "0.22.1"
    id("com.palantir.docker-run") version "0.22.1"
    //id("com.palantir.docker-compose") version "0.22.1"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
}

group = "swapi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8


repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    //implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //implementation("org.springframework.hateoas:spring-hateoas:1.0.0.RELEASE")

    implementation("com.beust:klaxon:5.1")
    //implementation("de.undercouch:actson:1.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("com.squareup.retrofit2:retrofit:2.6.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

//https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#quickstart

configure<DockerExtension> {
    name = "swapi/${project.name}"
    //setDockerfile(project.file("Dockerfile"))
    copy {
        from("$buildDir/libs/${project.name}-${version}.jar")
        into("$buildDir/docker")
    }
    buildArgs(mapOf("JAR_FILE" to "${project.name}-${version}.jar"))
    /*
    copy {
        from(zipTree("$buildDir/libs/${project.name}-${version}.jar"))
        into("$buildDir/docker/dependency")
    }
    buildArgs(mapOf("DEPENDENCY" to "dependency"))
    */
}

configure<DockerRunExtension> {
    name = "swapi-${project.name}-container"
    image = "swapi/${project.name}"
    ports("8080:8080")
    daemonize = true
}
