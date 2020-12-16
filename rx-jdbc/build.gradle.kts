version = DatabaseLibs.Version.jooqVertx

dependencies {
    implementation(project(":type"))
    implementation(VertxLibs.rx2)
    implementation(DatabaseLibs.jooq)
    implementation(DatabaseLibs.jooqVertxRx)
}
