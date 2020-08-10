package com.polidea.rxandroidble2.helpers;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.internal.RxBleLog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ValueInterpreter {
    public static final int FORMAT_FLOAT = 52;
    public static final int FORMAT_SFLOAT = 50;
    public static final int FORMAT_SINT16 = 34;
    public static final int FORMAT_SINT32 = 36;
    public static final int FORMAT_SINT8 = 33;
    public static final int FORMAT_UINT16 = 18;
    public static final int FORMAT_UINT32 = 20;
    public static final int FORMAT_UINT8 = 17;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FloatFormatType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IntFormatType {
    }

    private ValueInterpreter() {
    }

    public static Integer getIntValue(@NonNull byte[] value, int formatType, @IntRange(from = 0) int offset) {
        if (getTypeLen(formatType) + offset > value.length) {
            RxBleLog.m25w("Int formatType (0x%x) is longer than remaining bytes (%d) - returning null", Integer.valueOf(formatType), Integer.valueOf(value.length - offset));
            return null;
        }
        switch (formatType) {
            case 17:
                return Integer.valueOf(unsignedByteToInt(value[offset]));
            case 18:
                return Integer.valueOf(unsignedBytesToInt(value[offset], value[offset + 1]));
            case 20:
                return Integer.valueOf(unsignedBytesToInt(value[offset], value[offset + 1], value[offset + 2], value[offset + 3]));
            case 33:
                return Integer.valueOf(unsignedToSigned(unsignedByteToInt(value[offset]), 8));
            case 34:
                return Integer.valueOf(unsignedToSigned(unsignedBytesToInt(value[offset], value[offset + 1]), 16));
            case 36:
                return Integer.valueOf(unsignedToSigned(unsignedBytesToInt(value[offset], value[offset + 1], value[offset + 2], value[offset + 3]), 32));
            default:
                RxBleLog.m25w("Passed an invalid integer formatType (0x%x) - returning null", Integer.valueOf(formatType));
                return null;
        }
    }

    public static Float getFloatValue(@NonNull byte[] value, int formatType, @IntRange(from = 0) int offset) {
        if (getTypeLen(formatType) + offset > value.length) {
            RxBleLog.m25w("Float formatType (0x%x) is longer than remaining bytes (%d) - returning null", Integer.valueOf(formatType), Integer.valueOf(value.length - offset));
            return null;
        } else if (formatType == 50) {
            return Float.valueOf(bytesToFloat(value[offset], value[offset + 1]));
        } else {
            if (formatType == 52) {
                return Float.valueOf(bytesToFloat(value[offset], value[offset + 1], value[offset + 2], value[offset + 3]));
            }
            RxBleLog.m25w("Passed an invalid float formatType (0x%x) - returning null", Integer.valueOf(formatType));
            return null;
        }
    }

    public static String getStringValue(@NonNull byte[] value, @IntRange(from = 0) int offset) {
        if (offset > value.length) {
            RxBleLog.m25w("Passed offset that exceeds the length of the byte array - returning null", new Object[0]);
            return null;
        }
        byte[] strBytes = new byte[(value.length - offset)];
        for (int i = 0; i != value.length - offset; i++) {
            strBytes[i] = value[offset + i];
        }
        return new String(strBytes);
    }

    private static int getTypeLen(int formatType) {
        return formatType & 15;
    }

    private static int unsignedByteToInt(byte b) {
        return b & 255;
    }

    private static int unsignedBytesToInt(byte b0, byte b1) {
        return unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8);
    }

    private static int unsignedBytesToInt(byte b0, byte b1, byte b2, byte b3) {
        return unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8) + (unsignedByteToInt(b2) << 16) + (unsignedByteToInt(b3) << 24);
    }

    private static float bytesToFloat(byte b0, byte b1) {
        return (float) (((double) unsignedToSigned(unsignedByteToInt(b0) + ((unsignedByteToInt(b1) & 15) << 8), 12)) * Math.pow(10.0d, (double) unsignedToSigned(unsignedByteToInt(b1) >> 4, 4)));
    }

    private static float bytesToFloat(byte b0, byte b1, byte b2, byte b3) {
        return (float) (((double) unsignedToSigned(unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8) + (unsignedByteToInt(b2) << 16), 24)) * Math.pow(10.0d, (double) b3));
    }

    private static int unsignedToSigned(int unsigned, int size) {
        if (((1 << (size - 1)) & unsigned) != 0) {
            return ((1 << (size - 1)) - (unsigned & ((1 << (size - 1)) - 1))) * -1;
        }
        return unsigned;
    }
}
