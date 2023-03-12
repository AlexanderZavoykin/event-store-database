plugins {
    id("org.flywaydb.flyway") version Versions.FLYWAY
    id("nu.studer.jooq") version Versions.JOOQ_PLUGIN
}

dependencies {
    implementation(project(":api"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jooq:jooq:${Versions.JOOQ}")
    implementation("org.jooq:jooq-meta:${Versions.JOOQ}")

    implementation("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    jooqGenerator("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    implementation("com.zaxxer:HikariCP:${Versions.HIKARI}")
    implementation("org.flywaydb:flyway-core:${Versions.FLYWAY}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
    testImplementation("org.assertj:assertj-core:${Versions.ASSERTJ}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

flyway {
    url = "jdbc:postgresql://localhost:5432/event-store"
    user = "event-store"
    password = "event-store"
}

val genDir = "src/jooq/kotlin"

jooq {
    version.set(Versions.JOOQ)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/event-store"
                    username = "event-store"
                    password = "event-store"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        schemata.addAll(arrayOf(
                            org.jooq.meta.jaxb.SchemaMappingType().apply { inputSchema = "public" }
                        ))
                        excludes = "flyway_.*"
                    }
                    generate.apply {
                        isRoutines = false
                        isRecords = true
                        isDaos = false
                        isPojosAsKotlinDataClasses = true
                        isSpringAnnotations = false
                        isPojos = false
                    }
                    target.apply {
                        directory = genDir
                        packageName = "practice.event.store.impl.jooq"
                    }
                }
            }
        }
    }
}

sourceSets["main"].java.srcDir(file(genDir))