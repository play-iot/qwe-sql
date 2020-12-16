package io.github.zero88.msa.sql.exceptions;

import io.github.zero88.msa.bp.exceptions.BlueprintException;
import io.github.zero88.msa.bp.exceptions.ErrorCode;

public class DatabaseException extends BlueprintException {

    public static final ErrorCode CODE = ErrorCode.parse("DATABASE_ERROR");

    protected DatabaseException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }

    public DatabaseException(String message, Throwable e) {
        this(CODE, message, e);
    }

    public DatabaseException(String message) { this(message, null); }

    public DatabaseException(Throwable e)    { this(null, e); }

}
