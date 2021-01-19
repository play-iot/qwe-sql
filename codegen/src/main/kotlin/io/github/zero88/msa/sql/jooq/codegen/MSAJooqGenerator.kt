package io.github.zero88.msa.sql.jooq.codegen

import io.github.jklingsporn.vertx.jooq.generate.builder.DelegatingVertxGenerator
import io.github.jklingsporn.vertx.jooq.generate.builder.FinalStep
import org.jooq.codegen.JavaWriter
import org.jooq.meta.ColumnDefinition
import org.jooq.meta.TableDefinition
import org.jooq.meta.TypedElementDefinition
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

@Suppress("INACCESSIBLE_TYPE")
abstract class MSAJooqGenerator(step: FinalStep) : DelegatingVertxGenerator(step.build()) {

    override fun handleCustomTypeFromJson(
        column: TypedElementDefinition<*>?,
        setter: String?,
        columnType: String?,
        javaMemberName: String?,
        out: JavaWriter?
    ): Boolean {
        return super.handleCustomTypeFromJson(column, setter, columnType, javaMemberName, out)
    }

    override fun handleCustomTypeToJson(
        column: TypedElementDefinition<*>?,
        getter: String?,
        columnType: String?,
        javaMemberName: String?,
        out: JavaWriter?
    ): Boolean {
        return super.handleCustomTypeToJson(column, getter, columnType, javaMemberName, out)
    }

    override fun generateInterface(table: TableDefinition?) {
        super.generateInterface(table)
    }

    override fun generateTableClassFooter(table: TableDefinition, out: JavaWriter) {
        out.println()
        out.tab(1)
            .println("private final Map<String, String> jsonFields = Collections.unmodifiableMap(initFields());")
        out.println()
        out.tab(1).println("private Map<String, String> initFields() {")
        out.tab(2).println("Map<String, String> map = new HashMap();")
        table.columns.forEach(Consumer { c: ColumnDefinition ->
//            val jsonField: String = StringUtils.toSnakeCase(CacheDataType.instance().fieldName(c.name), false)
            val jsonField: String = StringUtils.toSnakeCase(c.name, false)
            out.tab(2).println("map.put(\"" + jsonField + "\", \"" + c.name + "\");")
        })
        out.tab(2).println("return map;")
        out.tab(1).println("}")
        out.println()
        out.tab(1).override()
        out.tab(1).println("public Map<String, String> jsonFields() {")
        out.tab(2).println("return jsonFields;")
        out.tab(1).println("}")
        out.println()
        out.ref(Map::class.java)
        out.ref(HashMap::class.java)
        out.ref(Collections::class.java)
    }
}
