repositories {
    jcenter()
    maven {
        url = uri("https://www.atilika.org/nexus/content/repositories/atilika/")
    }
}

plugins {
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    // Gradle, why is this not yet the default?!
    options.encoding = "UTF-8"
}

application {
    mainClass.set("org.trypticon.talker.TalkerMain")
}

dependencies {
    val icu4jVersion = "68.2"
    implementation("com.ibm.icu:icu4j:${icu4jVersion}")

    val gsonVersion = "2.8.6"
    implementation("com.google.code.gson:gson:${gsonVersion}")

//    val jnaVersion = "5.7.0"
//    implementation("net.java.dev.jna:jna:${jnaVersion}")
//    implementation("net.java.dev.jna:jna-platform:${jnaVersion}")

    val kuromojiVersion = "0.7.7"
    implementation("org.atilika.kuromoji:kuromoji:${kuromojiVersion}")

    val maryttsVersion = "5.2"
    implementation("de.dfki.mary:marytts-runtime:${maryttsVersion}")
    implementation("de.dfki.mary:marytts-client:${maryttsVersion}")
    implementation("de.dfki.mary:marytts-lang-en:${maryttsVersion}")
    implementation("de.dfki.mary:voice-cmu-slt-hsmm:${maryttsVersion}")

    val junitVersion = "5.7.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    val hamcrestVersion = "2.2"
    testImplementation("org.hamcrest:hamcrest:${hamcrestVersion}")
}
