package com.polidea.rxandroidble2.exceptions;

public class BleAlreadyConnectedException extends BleException {
    public BleAlreadyConnectedException(String macAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append("Already connected to device with MAC address ");
        sb.append(macAddress);
        super(sb.toString());
    }
}
