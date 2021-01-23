package io.github.zero88.qwe.sql;

import io.github.zero88.qwe.component.ComponentProvider;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.sql.handler.EntityHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SqlProvider<T extends EntityHandler> implements ComponentProvider<SqlVerticle> {

    private final Class<T> entityHandlerClass;

    @Override
    public Class<SqlVerticle> componentClass() {
        return SqlVerticle.class;
    }

    @Override
    public SqlVerticle provide(SharedDataLocalProxy sharedDataLocalProxy) {
        return new SqlVerticle<>(sharedDataLocalProxy, entityHandlerClass);
    }

}
