plugins {
    kotlin("jvm") version "1.4.20"
}

group = "ping"
version = "1.0-SNAPSHOT"

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/google") }
    maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("scripting-jsr223"))

    implementation("org.dom4j:dom4j:2.1.3")
    implementation("commons-dbutils:commons-dbutils:1.7")
    implementation("mysql:mysql-connector-java:8.0.27")
}