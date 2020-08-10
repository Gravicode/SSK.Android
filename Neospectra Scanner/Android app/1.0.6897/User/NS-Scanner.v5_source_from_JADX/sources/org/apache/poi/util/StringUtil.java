package org.apache.poi.util;

import java.io.UnsupportedEncodingException;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Iterator;

public class StringUtil {
    private static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

    public static class StringsIterator implements Iterator<String> {
        private int position = 0;
        private String[] strings;

        public StringsIterator(String[] strings2) {
            if (strings2 != null) {
                this.strings = strings2;
            } else {
                this.strings = new String[0];
            }
        }

        public boolean hasNext() {
            return this.position < this.strings.length;
        }

        public String next() {
            int ourPos = this.position;
            this.position = ourPos + 1;
            if (ourPos < this.strings.length) {
                return this.strings[ourPos];
            }
            throw new ArrayIndexOutOfBoundsException(ourPos);
        }

        public void remove() {
        }
    }

    private StringUtil() {
    }

    public static String getFromUnicodeLE(byte[] string, int offset, int len) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (offset < 0 || offset >= string.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal offset ");
            sb.append(offset);
            sb.append(" (String data is of length ");
            sb.append(string.length);
            sb.append(")");
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        } else if (len < 0 || (string.length - offset) / 2 < len) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Illegal length ");
            sb2.append(len);
            throw new IllegalArgumentException(sb2.toString());
        } else {
            try {
                return new String(string, offset, len * 2, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getFromUnicodeLE(byte[] string) {
        if (string.length == 0) {
            return "";
        }
        return getFromUnicodeLE(string, 0, string.length / 2);
    }

    public static String getFromCompressedUnicode(byte[] string, int offset, int len) {
        try {
            return new String(string, offset, Math.min(len, string.length - offset), ENCODING_ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readCompressedUnicode(LittleEndianInput in, int nChars) {
        char[] buf = new char[nChars];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (char) in.readUByte();
        }
        return new String(buf);
    }

    public static String readUnicodeString(LittleEndianInput in) {
        int nChars = in.readUShort();
        if ((in.readByte() & 1) == 0) {
            return readCompressedUnicode(in, nChars);
        }
        return readUnicodeLE(in, nChars);
    }

    public static String readUnicodeString(LittleEndianInput in, int nChars) {
        if ((in.readByte() & 1) == 0) {
            return readCompressedUnicode(in, nChars);
        }
        return readUnicodeLE(in, nChars);
    }

    public static void writeUnicodeString(LittleEndianOutput out, String value) {
        out.writeShort(value.length());
        boolean is16Bit = hasMultibyte(value);
        out.writeByte(is16Bit);
        if (is16Bit) {
            putUnicodeLE(value, out);
        } else {
            putCompressedUnicode(value, out);
        }
    }

    public static void writeUnicodeStringFlagAndData(LittleEndianOutput out, String value) {
        boolean is16Bit = hasMultibyte(value);
        out.writeByte(is16Bit);
        if (is16Bit) {
            putUnicodeLE(value, out);
        } else {
            putCompressedUnicode(value, out);
        }
    }

    public static int getEncodedSize(String value) {
        return 3 + (value.length() * (hasMultibyte(value) ? 2 : 1));
    }

    public static void putCompressedUnicode(String input, byte[] output, int offset) {
        try {
            byte[] bytes = input.getBytes(ENCODING_ISO_8859_1);
            System.arraycopy(bytes, 0, output, offset, bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putCompressedUnicode(String input, LittleEndianOutput out) {
        try {
            out.write(input.getBytes(ENCODING_ISO_8859_1));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putUnicodeLE(String input, byte[] output, int offset) {
        try {
            byte[] bytes = input.getBytes("UTF-16LE");
            System.arraycopy(bytes, 0, output, offset, bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putUnicodeLE(String input, LittleEndianOutput out) {
        try {
            out.write(input.getBytes("UTF-16LE"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readUnicodeLE(LittleEndianInput in, int nChars) {
        char[] buf = new char[nChars];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (char) in.readUShort();
        }
        return new String(buf);
    }

    public static String format(String message, Object[] params) {
        int currentParamNumber;
        int currentParamNumber2 = 0;
        StringBuffer formattedMessage = new StringBuffer();
        int i = 0;
        while (i < message.length()) {
            if (message.charAt(i) == '%') {
                if (currentParamNumber2 >= params.length) {
                    formattedMessage.append("?missing data?");
                } else {
                    if (!(params[currentParamNumber2] instanceof Number) || i + 1 >= message.length()) {
                        currentParamNumber = currentParamNumber2 + 1;
                        formattedMessage.append(params[currentParamNumber2].toString());
                    } else {
                        currentParamNumber = currentParamNumber2 + 1;
                        i += matchOptionalFormatting(params[currentParamNumber2], message.substring(i + 1), formattedMessage);
                    }
                    currentParamNumber2 = currentParamNumber;
                }
            } else if (message.charAt(i) == '\\' && i + 1 < message.length() && message.charAt(i + 1) == '%') {
                formattedMessage.append('%');
                i++;
            } else {
                formattedMessage.append(message.charAt(i));
            }
            i++;
        }
        return formattedMessage.toString();
    }

    private static int matchOptionalFormatting(Number number, String formatting, StringBuffer outputTo) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (formatting.length() > 0 && Character.isDigit(formatting.charAt(0))) {
            StringBuilder sb = new StringBuilder();
            sb.append(formatting.charAt(0));
            sb.append("");
            numberFormat.setMinimumIntegerDigits(Integer.parseInt(sb.toString()));
            if (2 >= formatting.length() || formatting.charAt(1) != '.' || !Character.isDigit(formatting.charAt(2))) {
                numberFormat.format(number, outputTo, new FieldPosition(0));
                return 1;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(formatting.charAt(2));
            sb2.append("");
            numberFormat.setMaximumFractionDigits(Integer.parseInt(sb2.toString()));
            numberFormat.format(number, outputTo, new FieldPosition(0));
            return 3;
        } else if (formatting.length() <= 0 || formatting.charAt(0) != '.' || 1 >= formatting.length() || !Character.isDigit(formatting.charAt(1))) {
            numberFormat.format(number, outputTo, new FieldPosition(0));
            return 1;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(formatting.charAt(1));
            sb3.append("");
            numberFormat.setMaximumFractionDigits(Integer.parseInt(sb3.toString()));
            numberFormat.format(number, outputTo, new FieldPosition(0));
            return 2;
        }
    }

    public static String getPreferredEncoding() {
        return ENCODING_ISO_8859_1;
    }

    public static boolean hasMultibyte(String value) {
        if (value == null) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) > 255) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUnicodeString(String value) {
        try {
            return true ^ value.equals(new String(value.getBytes(ENCODING_ISO_8859_1), ENCODING_ISO_8859_1));
        } catch (UnsupportedEncodingException e) {
            return true;
        }
    }
}
