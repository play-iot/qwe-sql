package io.github.zero88.msa.sql;

import io.github.zero88.msa.bp.exceptions.BlueprintException;
import io.github.zero88.msa.bp.exceptions.ErrorCode;

public class DatabaseException extends BlueprintException {

    public static ErrorCode DATABASE_ERROR = new ErrorCode("DATABASE_ERROR");

    protected DatabaseException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }

    public DatabaseException(String message, Throwable e) {
        this(DATABASE_ERROR, message, e);
    }

    public DatabaseException(String message) { this(message, null); }

    public DatabaseException(Throwable e)    { this(null, e); }

}
