package org.prgrms.be.app.exception;

public class ImpossibleDeleteRecordException extends RuntimeException {
    public ImpossibleDeleteRecordException(String message) {
        super(message);
    }
}
