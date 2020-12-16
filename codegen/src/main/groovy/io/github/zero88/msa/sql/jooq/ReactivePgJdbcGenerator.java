package io.github.zero88.msa.sql.jooq;

import io.github.jklingsporn.vertx.jooq.generate.builder.VertxGeneratorBuilder;

public final class ReactivePgJdbcGenerator extends MSAJdbcGenerator {

    public ReactivePgJdbcGenerator() {
        super(VertxGeneratorBuilder.init().withRXAPI().withJDBCDriver());
    }

}
