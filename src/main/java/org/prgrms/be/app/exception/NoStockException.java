package org.prgrms.be.app.exception;

public class NoStockException extends RuntimeException {
    public NoStockException() {
        super("해당 상품의 재고가 주문 개수만큼 남아있지 않습니다.");
    }
}
