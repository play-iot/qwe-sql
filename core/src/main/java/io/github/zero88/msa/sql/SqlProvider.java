package io.github.zero88.msa.sql;

import io.github.zero88.msa.bp.component.UnitProvider;
import io.github.zero88.msa.sql.handler.EntityHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SqlProvider<T extends EntityHandler> implements UnitProvider<SqlVerticle> {

    private final Class<T> entityHandlerClass;

    @Override
    public SqlVerticle<T> get() { return new SqlVerticle<>(entityHandlerClass); }

    @Override
    public Class<SqlVerticle> unitClass() { return SqlVerticle.class; }

}
