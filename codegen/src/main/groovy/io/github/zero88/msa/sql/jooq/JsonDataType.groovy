package io.github.zero88.msa.sql.jooq

class JsonDataType {
    /**
     * Java full qualified class name
     */
    String className
    /**
     * Serialize function
     */
    String converter
    /**
     * Deserialize function
     */
    String parser
    /**
     * Default value if {@code null} on deserialize
     */
    String defVal = "null"

    @Override
    String toString() {
        return "${className} - Converter: ${converter} - Parser: ${parser} - Default: ${defVal}"
    }
}
