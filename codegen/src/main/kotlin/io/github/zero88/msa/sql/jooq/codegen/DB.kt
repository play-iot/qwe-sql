package io.github.zero88.msa.sql.jooq.codegen

object DB {

    object TypeRegex {

        val varchar = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("N?VARCHAR"))
        val text = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("TEXT|CLOB"))
        val date = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("DATE"))
        val time = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("TIME"))
        val timestamp = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("TIMESTAMP"))
        val timestampZ = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("TIMESTAMP(\\([0-9]\\))? WITH TIME ZONE"))
        val array = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("ARRAY"))
        val uuid = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("UUID"))
    }

    object ColumnRegex {

        val json = Regex.fromLiteral(StringUtils.toRegexIgnoreCase(".+_JSON\$"))
        val jsonArray = Regex.fromLiteral(StringUtils.toRegexIgnoreCase(".+(_JSON_ARRAY|_ARRAY)\$"))
        val period = Regex.fromLiteral(StringUtils.toRegexIgnoreCase(".+(_PERIOD)\$"))
        val duration = Regex.fromLiteral(StringUtils.toRegexIgnoreCase(".+(_DURATION)\$"))
        val timeAudit = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("TIME_AUDIT"))
        val label = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("LABEL"))
        val syncAudit = Regex.fromLiteral(StringUtils.toRegexIgnoreCase("SYNC_AUDIT"))
    }
}
