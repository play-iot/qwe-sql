package io.github.zero88.qwe.sql.query;

import java.util.Optional;

import org.jooq.UpdatableRecord;

import io.github.jklingsporn.vertx.jooq.rx.VertxDAO;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.sql.EntityMetadata;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.github.zero88.qwe.sql.marker.ReferencingEntityMarker;
import io.reactivex.Single;

import lombok.NonNull;

/**
 * Represents for a {@code SQL executor} do {@code DML} or {@code DQL} on the {@code database entity} has the
 * relationship to another entity.
 *
 * @param <P> Type of {@code VertxPojo}
 * @since 1.0.0
 */
public interface ReferencingQueryExecutor<P extends VertxPojo> extends SimpleQueryExecutor<P> {

    /**
     * Create reference query executor.
     *
     * @param <K>      Type of {@code primary key}
     * @param <P>      Type of {@code VertxPojo}
     * @param <R>      Type of {@code UpdatableRecord}
     * @param <D>      Type of {@code VertxDAO}
     * @param handler  the entity handler
     * @param metadata the entity metadata
     * @param marker   the reference entity marker
     * @return the reference query executor
     * @see EntityHandler
     * @see EntityMetadata
     * @see ReferencingEntityMarker
     * @since 1.0.0
     */
    static <K, P extends VertxPojo, R extends UpdatableRecord<R>, D extends VertxDAO<R, P, K>> ReferencingQueryExecutor create(
        @NonNull EntityHandler handler, @NonNull EntityMetadata<K, P, R, D> metadata,
        @NonNull ReferencingEntityMarker marker) {
        return new ReferencingDaoQueryExecutor<>(handler, metadata, marker);
    }

    /**
     * Defines {@code entity marker}.
     *
     * @return the has reference marker
     * @see ReferencingEntityMarker
     * @since 1.0.0
     */
    @NonNull ReferencingEntityMarker marker();

    /**
     * Verify the {@code referenced entities} by {@link ReferencingEntityMarker#referencedEntities()} whether exists or
     * not.
     *
     * @param reqData the request data
     * @return error single if not found any {@code reference entity}, otherwise {@code true} single
     * @since 1.0.0
     */
    default Single<Boolean> checkReferenceExistence(@NonNull RequestData reqData) {
        return marker().referencedEntities()
                       .toObservable()
                       .flatMapSingle(e -> this.findReferenceKey(reqData, e.getKey(), e.getValue())
                                               .map(rk -> this.fetchExists(queryBuilder().exist(e.getKey(), rk))
                                                              .switchIfEmpty(Single.error(e.getKey().notFound(rk))))
                                               .orElseGet(() -> Single.just(true)))
                       .all(aBoolean -> aBoolean);
    }

    default Optional<?> findReferenceKey(@NonNull RequestData reqData, @NonNull EntityMetadata metadata,
                                         @NonNull String refField) {
        final Optional<?> key = metadata.getKey(reqData);
        if (key.isPresent()) {
            return key;
        }
        return Optional.ofNullable(reqData.body().getValue(refField)).map(k -> metadata.parseKey(k.toString()));
    }

}
