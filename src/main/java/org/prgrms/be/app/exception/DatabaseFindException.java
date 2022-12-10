package org.prgrms.be.app.exception;

public class DatabaseFindException extends RuntimeException {
    public DatabaseFindException() {
        super("조회에 실패했습니다.");
    }

    public DatabaseFindException(String message) {
        super(message);
    }

    public DatabaseFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
