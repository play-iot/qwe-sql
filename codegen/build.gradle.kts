plugins {
    groovy
}

dependencies {
    implementation(DatabaseLibs.jooq)
    implementation(DatabaseLibs.jooqCodegen)
    implementation(DatabaseLibs.jooqMeta)
    implementation(DatabaseLibs.jooqMetaExt)
    implementation(DatabaseLibs.jooqVertxCodegen)
    implementation("org.codehaus.groovy:groovy-all:2.4.15")
}
