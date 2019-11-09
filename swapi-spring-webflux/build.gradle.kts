import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.google.cloud.tools.jib") version "1.7.0"
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

//jib.to.image = "${project.group}/${project.name}"

jib() {
    to {
        image = "localhost:5000/${project.name}"
        tags = setOf(project.version.toString(), "latest")
        auth {
            username = ""
            password = ""
        }
    }
}
/*
jib {
  from {
    image = 'openjdk:alpine'
  }
  to {
    image = 'localhost:5000/my-image/built-with-jib'
    credHelper = 'osxkeychain'
    tags = ['tag2', 'latest']
  }
  container {
    jvmFlags = ['-Xms512m', '-Xdebug', '-Xmy:flag=jib-rules']
    mainClass = 'mypackage.MyApp'
    args = ['some', 'args']
    ports = ['1000', '2000-2003/udp']
    labels = [key1:'value1', key2:'value2']
    format = 'OCI'
  }
}
 */
