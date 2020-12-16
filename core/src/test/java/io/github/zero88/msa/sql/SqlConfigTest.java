package io.github.zero88.msa.sql;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.github.zero88.msa.bp.IConfig;
import io.github.zero88.msa.bp.utils.Configs;

class SqlConfigTest {

    @Test
    public void test_default() throws JSONException {
        SqlConfig sqlConfig = new SqlConfig();
        SqlConfig from = IConfig.from(Configs.loadJsonConfig("sql.json"), SqlConfig.class);
        System.out.println("DEFAULT: " + sqlConfig.toJson());
        System.out.println("FROM: " + from.toJson());
        JSONAssert.assertEquals(sqlConfig.toJson().encode(), from.toJson().encode(), JSONCompareMode.STRICT);
    }

    @Test
    public void test_hikari() {
        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.getHikariConfig().setJdbcUrl("abc");
        System.out.println(sqlConfig.toJson());
        SqlConfig from = IConfig.from(sqlConfig.toJson(), SqlConfig.class);
        Assertions.assertEquals("abc", from.getHikariConfig().getJdbcUrl());
    }

}
