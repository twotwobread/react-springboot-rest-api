package org.prgrms.be.app.exception;

public class DatabaseUpdateException extends RuntimeException {
    public DatabaseUpdateException() {
        super("insert 혹은 update 과정에서 예외가 발생했습니다.");
    }

    public DatabaseUpdateException(String message) {
        super(message);
    }
}
