import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j", "log4j-api", properties["version.log4j"].toString())
    implementation("org.apache.logging.log4j", "log4j-core", properties["version.log4j"].toString())
    implementation("org.slf4j", "slf4j-simple", properties["version.slf4j"].toString())

    implementation("org.springframework", "spring-context", properties["version.spring"].toString())
    implementation("org.springframework", "spring-webflux", properties["version.spring"].toString())
    testImplementation("org.springframework", "spring-test", properties["version.spring"].toString())
    testImplementation("com.jayway.jsonpath", "json-path", properties["version.jsonpath"].toString())

    implementation("org.springframework.data", "spring-data-jpa", properties["version.spring.data"].toString())
    implementation("javax.validation", "validation-api", properties["version.javax.validation"].toString())
    implementation("javax.xml.bind", "jaxb-api", properties["version.jaxb.api"].toString())
    implementation("org.glassfish.jaxb", "jaxb-runtime", properties["version.jaxb.runtime"].toString())
    implementation("org.hibernate", "hibernate-core", properties["version.hibernate"].toString())
    implementation("org.hibernate", "hibernate-entitymanager", properties["version.hibernate"].toString())
    testImplementation("org.hibernate", "hibernate-testing", properties["version.hibernate"].toString())
    testImplementation("com.h2database", "h2", properties["version.h2"].toString())

    implementation("io.projectreactor", "reactor-core", properties["version.reactor"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())
    implementation("io.projectreactor.netty", "reactor-netty", properties["version.netty"].toString())

    implementation("com.fasterxml.jackson.core", "jackson-databind", properties["version.jackson"].toString())

    testImplementation("org.junit.jupiter", "junit-jupiter", properties["version.junit"].toString())
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configure<ApplicationPluginConvention> {
    mainClassName = "com.example.spring.Main"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
    }
}
