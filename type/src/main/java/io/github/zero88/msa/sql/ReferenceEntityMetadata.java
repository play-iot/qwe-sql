package io.github.zero88.msa.sql;

import java.util.Objects;

import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;

import io.github.zero88.msa.bp.exceptions.ErrorCode;
import io.github.zero88.msa.bp.exceptions.ImplementationError;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(builderClassName = "Builder")
public final class ReferenceEntityMetadata {

    @NonNull
    private final ForeignKey foreignKey;
    private final Table table;

    public boolean isValid() {
        return this.foreignKey.getFields().size() == 1 && Objects.nonNull(table);
    }

    public TableField getField() {
        return (TableField) this.foreignKey.getFields().get(0);
    }

    public @NonNull EntityMetadata findByTable(@NonNull io.github.zero88.msa.sql.MetadataIndex index) {
        return index.findByTable(table)
                    .orElseThrow(() -> new ImplementationError(ErrorCode.SERVICE_NOT_FOUND,
                                                               "Not found Entity Metadata by table " +
                                                               table.getName()));
    }

    public static final class Builder {

        public ReferenceEntityMetadata build() {
            return new ReferenceEntityMetadata(foreignKey, Objects.isNull(table) ? foreignKey.getTable() : table);
        }

    }

}
