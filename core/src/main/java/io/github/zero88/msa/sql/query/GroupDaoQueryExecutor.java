package io.github.zero88.msa.sql.query;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.UpdatableRecord;

import io.github.jklingsporn.vertx.jooq.rx.VertxDAO;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.msa.bp.dto.jpa.Pagination;
import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.sql.CompositeMetadata;
import io.github.zero88.msa.sql.handler.EntityHandler;
import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.marker.GroupReferencingEntityMarker;
import io.github.zero88.msa.sql.pojos.CompositePojo;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

final class GroupDaoQueryExecutor<K, P extends VertxPojo, R extends UpdatableRecord<R>, D extends VertxDAO<R, P, K>,
                                     CP extends CompositePojo<P, CP>>
    extends io.github.zero88.msa.sql.query.BaseDaoQueryExecutor<CP>
    implements io.github.zero88.msa.sql.query.GroupQueryExecutor<CP> {

    private final CompositeMetadata<K, P, R, D, CP> groupMetadata;
    private final GroupReferencingEntityMarker marker;

    GroupDaoQueryExecutor(@NonNull EntityHandler handler, @NonNull EntityMetadata<K, P, R, D> metadata,
                          @NonNull CompositeMetadata<K, P, R, D, CP> groupMetadata,
                          @NonNull GroupReferencingEntityMarker marker) {
        super(handler, metadata);
        this.groupMetadata = groupMetadata;
        this.marker = marker;
    }

    @Override
    public io.github.zero88.msa.sql.query.QueryBuilder queryBuilder() {
        return new io.github.zero88.msa.sql.query.QueryBuilder(groupMetadata).references(groupMetadata.subItems());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CP> findMany(RequestData reqData) {
        final Pagination paging = Optional.ofNullable(reqData.pagination()).orElse(Pagination.builder().build());
        final Single<List> many = (Single<List>) dao(metadata()).queryExecutor()
                                                                .findMany(queryBuilder().view(reqData.filter(),
                                                                                              reqData.sort(), paging));
        return many.flattenAsObservable(rs -> rs)
                   .map(pojo -> CompositePojo.create(pojo, groupMetadata.rawClass(), groupMetadata.modelClass()));
    }

    @Override
    public @NonNull GroupReferencingEntityMarker marker() {
        return marker;
    }

    @Override
    public Single<CP> findOneByKey(RequestData reqData) {
        final Function<DSLContext, ? extends ResultQuery<? extends Record>> f = queryBuilder().viewOne(reqData.filter(),
                                                                                                       reqData.sort());
        return executeAny(f).map(r -> Optional.ofNullable(r.fetchOne(groupMetadata.mapper())))
                            .filter(Optional::isPresent)
                            .switchIfEmpty(Single.error(metadata().notFound(metadata().parseKey(reqData))))
                            .map(Optional::get)
                            .onErrorResumeNext(EntityQueryExecutor::sneakyThrowDBError);
    }

    @Override
    public Single<CP> lookupByPrimaryKey(@NonNull Object primaryKey) {
        final JsonObject filter = new JsonObject().put(metadata().requestKeyName(), primaryKey.toString())
                                                  .put(metadata().jsonKeyName(), primaryKey.toString());
        return findOneByKey(RequestData.builder().body(filter).filter(filter).build());
    }

}
