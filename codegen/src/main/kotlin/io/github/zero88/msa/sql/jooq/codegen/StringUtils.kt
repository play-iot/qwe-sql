package io.github.zero88.msa.sql.jooq.codegen

class StringUtils {

    companion object {

        @JvmStatic
        fun isBlank(text: String?): Boolean = text == null || "" == text.trim()

        @JvmStatic
        fun toRegexIgnoreCase(text: String?): String = "(?i:${text})"

        @JvmStatic
        fun replaceJsonSuffix(text: String?): String = text!!.replace(toRegexIgnoreCase("_JSON(_ARRAY)?|_ARRAY\$"), "")

        @JvmStatic
        fun toSnakeCase(text: String?, upper: Boolean = true): String {
            if (upper && text == text!!.toUpperCase()) {
                return text
            }
            if (!upper && text == text!!.toLowerCase()) {
                return text
            }
            val regex = if (upper) "A-Z" else "a-z"
            val t = text!!.replace("/[$regex])/", "/_$1/").replace("/^_/", "")
            return if (upper) t.toUpperCase() else t.toLowerCase()
        }
    }
}
