package org.apache.poi.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;

public class HexDump {
    public static final String EOL = System.getProperty("line.separator");
    private static final char[] _hexcodes = "0123456789ABCDEF".toCharArray();
    private static final int[] _shifts = {60, 56, 52, 48, 44, 40, 36, 32, 28, 24, 20, 16, 12, 8, 4, 0};

    private HexDump() {
    }

    public static void dump(byte[] data, long offset, OutputStream stream, int index, int length) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        byte[] bArr = data;
        OutputStream outputStream = stream;
        int i = index;
        if (bArr.length == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("No Data");
            sb.append(System.getProperty("line.separator"));
            outputStream.write(sb.toString().getBytes());
            stream.flush();
        } else if (i < 0 || i >= bArr.length) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal index: ");
            sb2.append(i);
            sb2.append(" into array of length ");
            sb2.append(bArr.length);
            throw new ArrayIndexOutOfBoundsException(sb2.toString());
        } else if (outputStream == null) {
            throw new IllegalArgumentException("cannot write to nullstream");
        } else {
            long display_offset = offset + ((long) i);
            StringBuffer buffer = new StringBuffer(74);
            int data_length = Math.min(bArr.length, i + length);
            long display_offset2 = display_offset;
            for (int j = i; j < data_length; j += 16) {
                int chars_read = data_length - j;
                if (chars_read > 16) {
                    chars_read = 16;
                }
                buffer.append(dump(display_offset2));
                byte b = 32;
                buffer.append(' ');
                int k = 0;
                for (int i2 = 16; k < i2; i2 = 16) {
                    if (k < chars_read) {
                        buffer.append(dump(bArr[k + j]));
                    } else {
                        buffer.append("  ");
                    }
                    buffer.append(' ');
                    k++;
                }
                int k2 = 0;
                while (k2 < chars_read) {
                    if (bArr[k2 + j] < b || bArr[k2 + j] >= Byte.MAX_VALUE) {
                        buffer.append('.');
                    } else {
                        buffer.append((char) bArr[k2 + j]);
                    }
                    k2++;
                    b = 32;
                }
                buffer.append(EOL);
                outputStream.write(buffer.toString().getBytes());
                stream.flush();
                buffer.setLength(0);
                display_offset2 += (long) chars_read;
            }
        }
    }

    public static synchronized void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        synchronized (HexDump.class) {
            dump(data, offset, stream, index, data.length - index);
        }
    }

    public static String dump(byte[] data, long offset, int index) {
        if (index < 0 || index >= data.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("illegal index: ");
            sb.append(index);
            sb.append(" into array of length ");
            sb.append(data.length);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        long display_offset = ((long) index) + offset;
        StringBuffer buffer = new StringBuffer(74);
        long display_offset2 = display_offset;
        for (int j = index; j < data.length; j += 16) {
            int chars_read = data.length - j;
            if (chars_read > 16) {
                chars_read = 16;
            }
            buffer.append(dump(display_offset2));
            buffer.append(' ');
            int k = 0;
            for (int k2 = 0; k2 < 16; k2++) {
                if (k2 < chars_read) {
                    buffer.append(dump(data[k2 + j]));
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            while (true) {
                int k3 = k;
                if (k3 >= chars_read) {
                    break;
                }
                if (data[k3 + j] < 32 || data[k3 + j] >= Byte.MAX_VALUE) {
                    buffer.append('.');
                } else {
                    buffer.append((char) data[k3 + j]);
                }
                k = k3 + 1;
            }
            buffer.append(EOL);
            display_offset2 += (long) chars_read;
        }
        return buffer.toString();
    }

    private static String dump(long value) {
        StringBuffer buf = new StringBuffer();
        buf.setLength(0);
        for (int j = 0; j < 8; j++) {
            buf.append(_hexcodes[((int) (value >> _shifts[(_shifts.length + j) - 8])) & 15]);
        }
        return buf.toString();
    }

    private static String dump(byte value) {
        StringBuffer buf = new StringBuffer();
        buf.setLength(0);
        for (int j = 0; j < 2; j++) {
            buf.append(_hexcodes[(value >> _shifts[j + 6]) & 15]);
        }
        return buf.toString();
    }

    public static String toHex(byte[] value) {
        StringBuffer retVal = new StringBuffer();
        retVal.append('[');
        for (int x = 0; x < value.length; x++) {
            if (x > 0) {
                retVal.append(", ");
            }
            retVal.append(toHex(value[x]));
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(short[] value) {
        StringBuffer retVal = new StringBuffer();
        retVal.append('[');
        for (short hex : value) {
            retVal.append(toHex(hex));
            retVal.append(", ");
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(byte[] value, int bytesPerLine) {
        int digits = (int) Math.round((Math.log((double) value.length) / Math.log(10.0d)) + 0.5d);
        StringBuffer formatString = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            formatString.append('0');
        }
        formatString.append(": ");
        DecimalFormat format = new DecimalFormat(formatString.toString());
        StringBuffer retVal = new StringBuffer();
        retVal.append(format.format(0));
        int i2 = -1;
        for (int x = 0; x < value.length; x++) {
            i2++;
            if (i2 == bytesPerLine) {
                retVal.append(10);
                retVal.append(format.format((long) x));
                i2 = 0;
            }
            retVal.append(toHex(value[x]));
            retVal.append(", ");
        }
        return retVal.toString();
    }

    public static String toHex(short value) {
        return toHex((long) value, 4);
    }

    public static String toHex(byte value) {
        return toHex((long) value, 2);
    }

    public static String toHex(int value) {
        return toHex((long) value, 8);
    }

    public static String toHex(long value) {
        return toHex(value, 16);
    }

    private static String toHex(long value, int digits) {
        StringBuffer result = new StringBuffer(digits);
        for (int j = 0; j < digits; j++) {
            result.append(_hexcodes[(int) ((value >> _shifts[(16 - digits) + j]) & 15)]);
        }
        return result.toString();
    }

    public static void dump(InputStream in, PrintStream out, int start, int bytesToDump) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        if (bytesToDump != -1) {
            int bytesRemaining = bytesToDump;
            while (true) {
                int bytesRemaining2 = bytesRemaining - 1;
                if (bytesRemaining <= 0) {
                    break;
                }
                int bytesRemaining3 = in.read();
                if (bytesRemaining3 == -1) {
                    break;
                }
                buf.write(bytesRemaining3);
                bytesRemaining = bytesRemaining2;
            }
        } else {
            int c = in.read();
            while (c != -1) {
                buf.write(c);
                c = in.read();
            }
        }
        byte[] data = buf.toByteArray();
        dump(data, 0, out, start, data.length);
    }

    private static char[] toHexChars(long pValue, int nBytes) {
        int charPos = (nBytes * 2) + 2;
        char[] result = new char[charPos];
        long value = pValue;
        do {
            charPos--;
            result[charPos] = _hexcodes[(int) (15 & value)];
            value >>>= 4;
        } while (charPos > 1);
        result[0] = '0';
        result[1] = 'x';
        return result;
    }

    public static char[] longToHex(long value) {
        return toHexChars(value, 8);
    }

    public static char[] intToHex(int value) {
        return toHexChars((long) value, 4);
    }

    public static char[] shortToHex(int value) {
        return toHexChars((long) value, 2);
    }

    public static char[] byteToHex(int value) {
        return toHexChars((long) value, 1);
    }

    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] b = new byte[((int) file.length())];
        in.read(b);
        System.out.println(dump(b, 0, 0));
        in.close();
    }
}
