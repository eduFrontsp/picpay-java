package com.picpay.picpaytest.authorization;

public class UnauthorizeTransactionException extends RuntimeException {

    public UnauthorizeTransactionException(String message) {
        super(message);
    }
}
