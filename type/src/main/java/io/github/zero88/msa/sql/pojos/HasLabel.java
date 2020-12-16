package io.github.zero88.msa.sql.pojos;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import io.github.zero88.msa.sql.type.Label;

public interface HasLabel extends VertxPojo {

    Label getLabel();

    <T extends HasLabel> T setLabel(Label value);

}
