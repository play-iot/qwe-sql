package io.github.zero88.qwe.sql.service;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.sql.EntityMetadata;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.github.zero88.qwe.sql.query.SimpleQueryExecutor;
import io.github.zero88.qwe.sql.service.decorator.RequestDecorator;
import io.github.zero88.qwe.sql.service.transformer.EntityTransformer;
import io.github.zero88.qwe.sql.validation.EntityValidation;
import io.github.zero88.qwe.sql.validation.OperationValidator;
import io.github.zero88.qwe.sql.workflow.DefaultDMLWorkflow;
import io.github.zero88.qwe.sql.workflow.DefaultDQLBatchWorkflow;
import io.github.zero88.qwe.sql.workflow.DefaultDQLWorkflow;
import io.github.zero88.qwe.sql.workflow.DefaultDeletionWorkflow;
import io.github.zero88.qwe.sql.workflow.step.CreationStep;
import io.github.zero88.qwe.sql.workflow.step.DeletionStep;
import io.github.zero88.qwe.sql.workflow.step.GetManyStep;
import io.github.zero88.qwe.sql.workflow.step.GetOneStep;
import io.github.zero88.qwe.sql.workflow.step.ModificationStep;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Abstract service to implement {@code CRUD} listeners for {@code database entity}
 *
 * @param <P> Type of {@code VertxPojo}
 * @param <M> Type of {@code EntityMetadata}
 * @see EntityService
 * @see RequestDecorator
 * @see EntityTransformer
 * @since 1.0.0
 */
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public abstract class AbstractEntityService<P extends VertxPojo, M extends EntityMetadata>
    implements SimpleEntityService<P, M>, RequestDecorator, EntityTransformer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EntityHandler entityHandler;

    @Override
    public EntityHandler entityHandler() { return entityHandler; }

    @Override
    public @NonNull SimpleQueryExecutor<P> queryExecutor() {
        return SimpleQueryExecutor.create(entityHandler(), context());
    }

    @Override
    public @NonNull RequestDecorator requestDecorator() { return this; }

    @Override
    public @NonNull EntityValidation validation() { return context(); }

    @Override
    public @NonNull EntityTransformer transformer() { return this; }

    @EventContractor(action = "GET_LIST", returnType = Single.class)
    public Single<JsonObject> list(RequestData requestData) {
        return DefaultDQLBatchWorkflow.builder()
                                      .action(EventAction.GET_LIST)
                                      .metadata(context())
                                      .normalize(requestDecorator()::onReadingManyResource)
                                      .validator(initGetOneValidator())
                                      .taskManager(taskManager())
                                      .sqlStep(initGetManyStep())
                                      .transformer((req, res) -> transformer().afterList(res))
                                      .build()
                                      .run(requestData);
    }

    @EventContractor(action = "GET_ONE", returnType = Single.class)
    public Single<JsonObject> get(RequestData requestData) {
        return DefaultDQLWorkflow.<P>builder().action(EventAction.GET_ONE)
                                              .metadata(context())
                                              .normalize(requestDecorator()::onReadingOneResource)
                                              .validator(initGetOneValidator())
                                              .taskManager(taskManager())
                                              .sqlStep(initGetOneStep())
                                              .transformer((req, res) -> transformer().afterGet(res, req))
                                              .build()
                                              .run(requestData);
    }

    @EventContractor(action = "CREATE", returnType = Single.class)
    public Single<JsonObject> create(RequestData requestData) {
        return DefaultDMLWorkflow.builder()
                                 .action(EventAction.CREATE)
                                 .metadata(context())
                                 .normalize(requestDecorator()::onCreatingOneResource)
                                 .validator(initCreationValidator())
                                 .taskManager(taskManager())
                                 .sqlStep(initCreationStep())
                                 .transformer((r, p) -> transformer().afterCreate(p.primaryKey(), p.dbEntity(), r))
                                 .build()
                                 .run(requestData);
    }

    @EventContractor(action = "UPDATE", returnType = Single.class)
    public Single<JsonObject> update(RequestData requestData) {
        return DefaultDMLWorkflow.builder()
                                 .action(EventAction.UPDATE)
                                 .metadata(context())
                                 .normalize(requestDecorator()::onModifyingOneResource)
                                 .validator(initUpdateValidator())
                                 .taskManager(taskManager())
                                 .sqlStep(initModificationStep(EventAction.UPDATE))
                                 .transformer((r, p) -> transformer().afterUpdate(p.primaryKey(), p.dbEntity(), r))
                                 .build()
                                 .run(requestData);
    }

    @EventContractor(action = "PATCH", returnType = Single.class)
    public Single<JsonObject> patch(RequestData requestData) {
        return DefaultDMLWorkflow.builder()
                                 .action(EventAction.PATCH)
                                 .metadata(context())
                                 .normalize(requestDecorator()::onModifyingOneResource)
                                 .validator(initPatchValidator())
                                 .taskManager(taskManager())
                                 .sqlStep(initModificationStep(EventAction.PATCH))
                                 .transformer((r, p) -> transformer().afterPatch(p.primaryKey(), p.dbEntity(), r))
                                 .build()
                                 .run(requestData);
    }

    @EventContractor(action = "REMOVE", returnType = Single.class)
    public Single<JsonObject> delete(RequestData requestData) {
        return DefaultDeletionWorkflow.builder()
                                      .action(EventAction.REMOVE)
                                      .metadata(context())
                                      .supportForceDeletion(supportForceDeletion())
                                      .normalize(requestDecorator()::onDeletingOneResource)
                                      .validator(initDeletionValidator())
                                      .taskManager(taskManager())
                                      .sqlStep(initDeletionStep())
                                      .transformer((req, re) -> transformer().afterDelete(re.dbEntity(), req))
                                      .build()
                                      .run(requestData);
    }

    @Override
    public @NonNull EntityMetadata resourceMetadata() {
        return context();
    }

    @NonNull
    protected GetManyStep initGetManyStep() {
        return GetManyStep.builder()
                          .action(EventAction.GET_LIST)
                          .queryExecutor(queryExecutor())
                          .onEach(transformer()::afterEach)
                          .build();
    }

    @NonNull
    protected OperationValidator initGetOneValidator() {
        return OperationValidator.create((req, pojo) -> Single.just(pojo));
    }

    @NonNull
    protected <PP extends P> GetOneStep<PP> initGetOneStep() {
        return GetOneStep.<PP>builder().action(EventAction.GET_ONE).queryExecutor(queryExecutor()).build();
    }

    @NonNull
    protected OperationValidator initCreationValidator() {
        return OperationValidator.create((req, dbEntity) -> Single.just(validation().onCreating(req)));
    }

    @NonNull
    protected CreationStep initCreationStep() {
        return CreationStep.builder().action(EventAction.CREATE).queryExecutor(queryExecutor()).build();
    }

    @NonNull
    protected OperationValidator initPatchValidator() {
        return OperationValidator.create((req, dbEntity) -> Single.just(validation().onPatching(dbEntity, req)));
    }

    @NonNull
    protected OperationValidator initUpdateValidator() {
        return OperationValidator.create((req, dbEntity) -> Single.just(validation().onUpdating(dbEntity, req)));
    }

    @NonNull
    protected ModificationStep initModificationStep(@NonNull EventAction action) {
        return ModificationStep.builder().action(action).queryExecutor(queryExecutor()).build();
    }

    @NonNull
    protected OperationValidator initDeletionValidator() {
        return OperationValidator.create((req, dbEntity) -> Single.just(validation().onDeleting(dbEntity, req)));
    }

    @NonNull
    protected DeletionStep initDeletionStep() {
        return DeletionStep.builder().action(EventAction.REMOVE).queryExecutor(queryExecutor()).build();
    }

}
