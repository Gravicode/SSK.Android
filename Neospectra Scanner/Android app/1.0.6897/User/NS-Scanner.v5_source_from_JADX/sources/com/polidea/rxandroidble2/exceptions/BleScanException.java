package com.polidea.rxandroidble2.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class BleScanException extends BleException {
    public static final int BLUETOOTH_CANNOT_START = 0;
    public static final int BLUETOOTH_DISABLED = 1;
    public static final int BLUETOOTH_NOT_AVAILABLE = 2;
    public static final int LOCATION_PERMISSION_MISSING = 3;
    public static final int LOCATION_SERVICES_DISABLED = 4;
    public static final int SCAN_FAILED_ALREADY_STARTED = 5;
    public static final int SCAN_FAILED_APPLICATION_REGISTRATION_FAILED = 6;
    public static final int SCAN_FAILED_FEATURE_UNSUPPORTED = 8;
    public static final int SCAN_FAILED_INTERNAL_ERROR = 7;
    public static final int SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 9;
    public static final int UNDOCUMENTED_SCAN_THROTTLE = 2147483646;
    public static final int UNKNOWN_ERROR_CODE = Integer.MAX_VALUE;
    private final int reason;
    private final Date retryDateSuggestion;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {
    }

    public BleScanException(int reason2) {
        super(createMessage(reason2, null));
        this.reason = reason2;
        this.retryDateSuggestion = null;
    }

    public BleScanException(int reason2, @NonNull Date retryDateSuggestion2) {
        super(createMessage(reason2, retryDateSuggestion2));
        this.reason = reason2;
        this.retryDateSuggestion = retryDateSuggestion2;
    }

    public BleScanException(int reason2, Throwable causeException) {
        super(createMessage(reason2, null), causeException);
        this.reason = reason2;
        this.retryDateSuggestion = null;
    }

    public int getReason() {
        return this.reason;
    }

    @Nullable
    public Date getRetryDateSuggestion() {
        return this.retryDateSuggestion;
    }

    private static String createMessage(int reason2, Date retryDateSuggestion2) {
        StringBuilder sb = new StringBuilder();
        sb.append(reasonDescription(reason2));
        sb.append(" (code ");
        sb.append(reason2);
        sb.append(")");
        sb.append(retryDateSuggestionIfExists(retryDateSuggestion2));
        return sb.toString();
    }

    private static String reasonDescription(int reason2) {
        if (reason2 == 2147483646) {
            return "Undocumented scan throttle";
        }
        switch (reason2) {
            case 0:
                return "Bluetooth cannot start";
            case 1:
                return "Bluetooth disabled";
            case 2:
                return "Bluetooth not available";
            case 3:
                return "Location Permission missing";
            case 4:
                return "Location Services disabled";
            case 5:
                return "Scan failed because it has already started";
            case 6:
                return "Scan failed because application registration failed";
            case 7:
                return "Scan failed because of an internal error";
            case 8:
                return "Scan failed because feature unsupported";
            case 9:
                return "Scan failed because out of hardware resources";
            default:
                return "Unknown error";
        }
    }

    private static String retryDateSuggestionIfExists(Date retryDateSuggestion2) {
        if (retryDateSuggestion2 == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(", suggested retry date is ");
        sb.append(retryDateSuggestion2);
        return sb.toString();
    }
}
