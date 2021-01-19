package io.github.zero88.msa.sql.jooq.codegen

import io.github.jklingsporn.vertx.jooq.generate.VertxGeneratorStrategy
import io.github.zero88.msa.sql.pojos.HasLabel
import io.github.zero88.msa.sql.pojos.HasSyncAudit
import io.github.zero88.msa.sql.pojos.HasTimeAudit
import io.github.zero88.msa.sql.tables.JsonTable
import org.jooq.codegen.GeneratorStrategy
import org.jooq.codegen.GeneratorStrategy.Mode
import org.jooq.meta.Definition
import org.jooq.meta.TableDefinition
import org.jooq.tools.StringUtils

class MSAGeneratorStrategy(delegate: GeneratorStrategy?) : VertxGeneratorStrategy(delegate) {

    override fun getJavaClassImplements(definition: Definition, mode: Mode): MutableList<String> {
        val javaClassImplements = super.getJavaClassImplements(definition, mode)
        if (mode == Mode.INTERFACE || mode == Mode.POJO || mode == Mode.RECORD) {
            val table = definition.database.getTable(definition.schema, definition.name)
            if (table.columns.any { it.name.matches(DB.ColumnRegex.timeAudit) }) {
                javaClassImplements.add(HasTimeAudit::class.qualifiedName)
            }
            if (table.columns.any { it.name.matches(DB.ColumnRegex.syncAudit) }) {
                javaClassImplements.add(HasSyncAudit::class.qualifiedName)
            }
            if (table.columns.any { it.name.matches(DB.ColumnRegex.label) }) {
                javaClassImplements.add(HasLabel::class.qualifiedName)
            }
        }
        if (mode == Mode.DEFAULT && definition is TableDefinition) {
            //TODO very hacky to add RECORD
            val recordClass = StringUtils.toCamelCase(definition.name) + "Record"
            javaClassImplements.add("${JsonTable::class.qualifiedName}<${recordClass}>")
        }
        return javaClassImplements
    }
}
