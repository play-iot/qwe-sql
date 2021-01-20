package io.github.zero88.qwe.sql.exceptions;

import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.ErrorCode;

public class DatabaseException extends CarlException {

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
