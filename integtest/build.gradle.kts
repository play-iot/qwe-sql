plugins {
    id(PluginLibs.jooq)
}

dependencies {
    jooqGenerator(DatabaseLibs.h2)
    jooqGenerator(project(":codegen"))
    testImplementation(project(":service"))
    testImplementation(testFixtures(project(":core")))
}

sourceSets.test {
    java.srcDirs("src/test/java", "generated/test/java")
}

jooq {
    version.set(DatabaseLibs.Version.jooq)

    configurations {
        create("testH2") {

        }
    }
}
