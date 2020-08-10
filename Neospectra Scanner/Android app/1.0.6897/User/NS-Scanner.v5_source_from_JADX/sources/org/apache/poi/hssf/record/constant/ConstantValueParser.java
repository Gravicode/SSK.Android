package org.apache.poi.hssf.record.constant;

import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class ConstantValueParser {
    private static final Object EMPTY_REPRESENTATION = null;
    private static final int FALSE_ENCODING = 0;
    private static final int TRUE_ENCODING = 1;
    private static final int TYPE_BOOLEAN = 4;
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_ERROR_CODE = 16;
    private static final int TYPE_NUMBER = 1;
    private static final int TYPE_STRING = 2;

    private ConstantValueParser() {
    }

    public static Object[] parse(LittleEndianInput in, int nValues) {
        Object[] result = new Object[nValues];
        for (int i = 0; i < result.length; i++) {
            result[i] = readAConstantValue(in);
        }
        return result;
    }

    private static Object readAConstantValue(LittleEndianInput in) {
        byte grbit = in.readByte();
        if (grbit == 4) {
            return readBoolean(in);
        }
        if (grbit != 16) {
            switch (grbit) {
                case 0:
                    in.readLong();
                    return EMPTY_REPRESENTATION;
                case 1:
                    return new Double(in.readDouble());
                case 2:
                    return StringUtil.readUnicodeString(in);
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown grbit value (");
                    sb.append(grbit);
                    sb.append(")");
                    throw new RuntimeException(sb.toString());
            }
        } else {
            int errCode = in.readUShort();
            in.readUShort();
            in.readInt();
            return ErrorConstant.valueOf(errCode);
        }
    }

    private static Object readBoolean(LittleEndianInput in) {
        byte val = (byte) ((int) in.readLong());
        switch (val) {
            case 0:
                return Boolean.FALSE;
            case 1:
                return Boolean.TRUE;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("unexpected boolean encoding (");
                sb.append(val);
                sb.append(")");
                throw new RuntimeException(sb.toString());
        }
    }

    public static int getEncodedSize(Object[] values) {
        int result = values.length * 1;
        for (Object encodedSize : values) {
            result += getEncodedSize(encodedSize);
        }
        return result;
    }

    private static int getEncodedSize(Object object) {
        if (object == EMPTY_REPRESENTATION) {
            return 8;
        }
        Class cls = object.getClass();
        if (cls == Boolean.class || cls == Double.class || cls == ErrorConstant.class) {
            return 8;
        }
        return StringUtil.getEncodedSize((String) object);
    }

    public static void encode(LittleEndianOutput out, Object[] values) {
        for (Object encodeSingleValue : values) {
            encodeSingleValue(out, encodeSingleValue);
        }
    }

    private static void encodeSingleValue(LittleEndianOutput out, Object value) {
        long longVal = 0;
        if (value == EMPTY_REPRESENTATION) {
            out.writeByte(0);
            out.writeLong(0);
        } else if (value instanceof Boolean) {
            Boolean bVal = (Boolean) value;
            out.writeByte(4);
            if (bVal.booleanValue()) {
                longVal = 1;
            }
            out.writeLong(longVal);
        } else if (value instanceof Double) {
            Double dVal = (Double) value;
            out.writeByte(1);
            out.writeDouble(dVal.doubleValue());
        } else if (value instanceof String) {
            String val = (String) value;
            out.writeByte(2);
            StringUtil.writeUnicodeString(out, val);
        } else if (value instanceof ErrorConstant) {
            ErrorConstant ecVal = (ErrorConstant) value;
            out.writeByte(16);
            out.writeLong((long) ecVal.getErrorCode());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value type (");
            sb.append(value.getClass().getName());
            sb.append("'");
            throw new IllegalStateException(sb.toString());
        }
    }
}
