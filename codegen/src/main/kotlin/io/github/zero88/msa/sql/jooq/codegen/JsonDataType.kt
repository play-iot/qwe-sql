package io.github.zero88.msa.sql.jooq.codegen

class JsonDataType {

    /**
     * Java full qualified class name
     */
    lateinit var className: String

    /**
     * Serialize function
     */
    lateinit var converter: String

    /**
     * Deserialize function
     */
    lateinit var parser: String

    /**
     * Default value if {@code null} on deserialize
     */
    var defVal: String = "null"

    @Override
    override fun toString(): String {
        return "$className - Converter: $converter - Parser: $parser - Default: $defVal"
    }
}
