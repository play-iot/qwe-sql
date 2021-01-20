plugins {
    `java-test-fixtures`
}

dependencies {
    api(project(":type"))
    api(project(":rx-jdbc"))
    api(VertxLibs.jdbc) {
        exclude(group = "com.mchange", module = "c3p0")
    }
    api(ZeroLibs.rql_jooq)
    api(DatabaseLibs.hikari)
    api(DatabaseLibs.jooq)
    api(DatabaseLibs.jooqVertxRx)

    testFixturesApi(TestLibs.junit5Api)
    testFixturesApi(TestLibs.jsonAssert)
    testFixturesApi(TestLibs.junit)
    testFixturesApi(VertxLibs.junit)
    testFixturesApi(LogLibs.logback)
    testFixturesApi(testFixtures(ZeroLibs.qwe_base))
    testFixturesCompileOnly(UtilLibs.lombok)
    testFixturesAnnotationProcessor(UtilLibs.lombok)
}
