import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.google.cloud.tools.jib") version "1.7.0"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
}

group = "swapi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

ext {
    set("dekorateVersion", "0.9.9")
}

repositories {
    //maven("https://repo.spring.io/milestone")
    //mavenCentral()
    //jcenter()
    maven(url = "http://localhost:30081/repository/maven-public/")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    /*********************
     * Kubernetes
     *********************/
    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes:1.1.0.RC2")

    val dekorateVersion = project.property("dekorateVersion") //ext.get("dekorateVersion")
    //implementation("io.dekorate:kubernetes-starters:$dekorateVersion")
    implementation("io.dekorate:dekorate-spring-boot:$dekorateVersion")
    implementation("io.dekorate:kubernetes-annotations:$dekorateVersion")
    annotationProcessor("io.dekorate:kubernetes-annotations:$dekorateVersion")
    annotationProcessor("io.dekorate:dekorate-spring-boot:$dekorateVersion")
    testImplementation("io.dekorate:kubernetes-junit:$dekorateVersion")

    //implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //logging
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")


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

jib() {
    to {
        image = "docker.local/${project.name}"
        tags = setOf(project.version.toString(), "latest")
        auth {
            username = property("docker.registry.username").toString()
            password = property("docker.registry.password").toString()
        }
    }
    setAllowInsecureRegistries(true)
}
