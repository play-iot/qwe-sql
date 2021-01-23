package io.github.zero88.qwe.sql;

import java.nio.file.Path;
import java.util.Objects;

import io.github.zero88.exceptions.HiddenException;
import io.github.zero88.qwe.component.Component;
import io.github.zero88.qwe.component.ComponentContext;
import io.github.zero88.qwe.sql.exceptions.DatabaseException;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.github.zero88.utils.Functions.Silencer;

import lombok.Getter;

public final class SqlContext<T extends EntityHandler> extends ComponentContext {

    @Getter
    private final T entityHandler;

    SqlContext(Class<? extends Component> componentClz, Path dataDir, String sharedKey, String deployId,
               T entityHandler) {
        super(componentClz, dataDir, sharedKey, deployId);
        this.entityHandler = entityHandler;
    }

    static class CreationHandler<E extends EntityHandler> extends Silencer<E> {

        @Override
        public void accept(E obj, HiddenException e) {
            if (Objects.nonNull(e)) {
                throw new DatabaseException("Error when creating entity handler", e);
            }
            object = obj;
        }

    }

}
