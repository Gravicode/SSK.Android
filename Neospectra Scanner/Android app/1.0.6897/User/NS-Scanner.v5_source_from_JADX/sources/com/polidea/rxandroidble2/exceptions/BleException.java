package com.polidea.rxandroidble2.exceptions;

public class BleException extends RuntimeException {
    public BleException() {
    }

    public BleException(String message) {
        super(message);
    }

    public BleException(Throwable throwable) {
        super(throwable);
    }

    public BleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
