plugins {
    kotlin("jvm") version PluginLibs.Version.kotlinJvm
}

dependencies {
    implementation(project(":type"))
    implementation(DatabaseLibs.jooq)
    implementation(DatabaseLibs.jooqCodegen)
    implementation(DatabaseLibs.jooqMeta)
    implementation(DatabaseLibs.jooqMetaExt)
    implementation(DatabaseLibs.jooqVertxCodegen)
}
