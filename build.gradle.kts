plugins {
    application
}

application {
    mainClass.set("App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {gradle
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<JavaExec> {gradle
    jvmArguments.add("--enable-preview")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.playwright:playwright:1.41.0")
}
