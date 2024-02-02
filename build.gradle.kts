plugins {
	java
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.testing.base"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	//runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
    //Primary dependency for writing spring boot tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	//Dependencies required for testcontainers
	testImplementation("org.testcontainers:testcontainers:1.19.4")
	testImplementation("org.testcontainers:junit-jupiter:1.19.4")
	//Don't remove the mysql-connector-j, because the testcontainers dependency below depends on that
	testImplementation("org.testcontainers:mysql:1.19.4")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
