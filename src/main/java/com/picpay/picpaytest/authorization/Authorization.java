package com.picpay.picpaytest.authorization;

public record Authorization(
        String message) {
    public boolean isAuthorized() {
        return message.equals("autorizado");
    }
}
