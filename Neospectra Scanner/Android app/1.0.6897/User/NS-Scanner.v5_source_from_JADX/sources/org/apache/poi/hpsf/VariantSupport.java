package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.util.LittleEndian;

public class VariantSupport extends Variant {
    public static final int[] SUPPORTED_TYPES = {0, 2, 3, 20, 5, 64, 30, 31, 71, 11};
    private static boolean logUnsupportedTypes = false;
    protected static List unsupportedMessage;

    public static void setLogUnsupportedTypes(boolean logUnsupportedTypes2) {
        logUnsupportedTypes = logUnsupportedTypes2;
    }

    public static boolean isLogUnsupportedTypes() {
        return logUnsupportedTypes;
    }

    protected static void writeUnsupportedTypeMessage(UnsupportedVariantTypeException ex) {
        if (isLogUnsupportedTypes()) {
            if (unsupportedMessage == null) {
                unsupportedMessage = new LinkedList();
            }
            Long vt = Long.valueOf(ex.getVariantType());
            if (!unsupportedMessage.contains(vt)) {
                System.err.println(ex.getMessage());
                unsupportedMessage.add(vt);
            }
        }
    }

    public boolean isSupportedType(int variantType) {
        for (int i : SUPPORTED_TYPES) {
            if (variantType == i) {
                return true;
            }
        }
        return false;
    }

    public static Object read(byte[] src, int offset, int length, long type, int codepage) throws ReadingNotSupportedException, UnsupportedEncodingException {
        Object obj;
        Object value;
        byte[] bArr = src;
        long j = type;
        int i = codepage;
        int o1 = offset;
        int l1 = length - 4;
        long lType = j;
        if (i == 1200 && j == 30) {
            lType = 31;
        }
        int i2 = (int) lType;
        if (i2 == 0) {
            return null;
        }
        if (i2 == 5) {
            return new Double(LittleEndian.getDouble(bArr, o1));
        }
        if (i2 == 11) {
            if (LittleEndian.getUInt(bArr, o1) != 0) {
                obj = Boolean.TRUE;
            } else {
                obj = Boolean.FALSE;
            }
            return obj;
        } else if (i2 == 20) {
            return Long.valueOf(LittleEndian.getLong(bArr, o1));
        } else {
            if (i2 == 64) {
                value = Util.filetimeToDate((int) LittleEndian.getUInt(bArr, o1 + 4), (int) LittleEndian.getUInt(bArr, o1));
            } else if (i2 != 71) {
                switch (i2) {
                    case 2:
                        return Integer.valueOf(LittleEndian.getShort(bArr, o1));
                    case 3:
                        return Integer.valueOf(LittleEndian.getInt(bArr, o1));
                    default:
                        switch (i2) {
                            case 30:
                                int first = o1 + 4;
                                long last = (((long) first) + LittleEndian.getUInt(bArr, o1)) - 1;
                                int o12 = o1 + 4;
                                while (bArr[(int) last] == 0 && ((long) first) <= last) {
                                    last--;
                                }
                                int l = (int) ((last - ((long) first)) + 1);
                                if (i == -1) {
                                    value = new String(bArr, first, l);
                                    break;
                                } else {
                                    value = new String(bArr, first, l, codepageToEncoding(codepage));
                                    break;
                                }
                                break;
                            case 31:
                                int first2 = o1 + 4;
                                long last2 = (((long) first2) + LittleEndian.getUInt(bArr, o1)) - 1;
                                int o13 = o1 + 4;
                                long l2 = last2 - ((long) first2);
                                StringBuffer b = new StringBuffer((int) (last2 - ((long) first2)));
                                int i3 = 0;
                                while (true) {
                                    int i4 = i3;
                                    if (((long) i4) <= l2) {
                                        int i1 = (i4 * 2) + o13;
                                        b.append((char) ((bArr[i1 + 1] << 8) | (bArr[i1] & 255)));
                                        i3 = i4 + 1;
                                        long j2 = type;
                                    } else {
                                        while (b.length() > 0 && b.charAt(b.length() - 1) == 0) {
                                            b.setLength(b.length() - 1);
                                        }
                                        return b.toString();
                                    }
                                }
                                break;
                            default:
                                byte[] v = new byte[l1];
                                int i5 = 0;
                                while (true) {
                                    int i6 = i5;
                                    if (i6 < l1) {
                                        v[i6] = bArr[o1 + i6];
                                        i5 = i6 + 1;
                                    } else {
                                        throw new ReadingNotSupportedException(j, v);
                                    }
                                }
                        }
                        break;
                }
            } else {
                if (l1 < 0) {
                    l1 = LittleEndian.getInt(bArr, o1);
                    o1 += 4;
                }
                byte[] v2 = new byte[l1];
                System.arraycopy(bArr, o1, v2, 0, v2.length);
                byte[] bArr2 = v2;
                return v2;
            }
            return value;
        }
    }

    public static String codepageToEncoding(int codepage) throws UnsupportedEncodingException {
        if (codepage <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Codepage number may not be ");
            sb.append(codepage);
            throw new UnsupportedEncodingException(sb.toString());
        }
        switch (codepage) {
            case 1200:
                return "UTF-16";
            case Constants.CP_UTF16_BE /*1201*/:
                return "UTF-16BE";
            default:
                switch (codepage) {
                    case Constants.CP_WINDOWS_1250 /*1250*/:
                        return "windows-1250";
                    case Constants.CP_WINDOWS_1251 /*1251*/:
                        return "windows-1251";
                    case Constants.CP_WINDOWS_1252 /*1252*/:
                        return "windows-1252";
                    case Constants.CP_WINDOWS_1253 /*1253*/:
                        return "windows-1253";
                    case Constants.CP_WINDOWS_1254 /*1254*/:
                        return "windows-1254";
                    case Constants.CP_WINDOWS_1255 /*1255*/:
                        return "windows-1255";
                    case Constants.CP_WINDOWS_1256 /*1256*/:
                        return "windows-1256";
                    case Constants.CP_WINDOWS_1257 /*1257*/:
                        return "windows-1257";
                    case Constants.CP_WINDOWS_1258 /*1258*/:
                        return "windows-1258";
                    default:
                        switch (codepage) {
                            case 10000:
                                return "MacRoman";
                            case Constants.CP_MAC_JAPAN /*10001*/:
                                return "SJIS";
                            case Constants.CP_MAC_CHINESE_TRADITIONAL /*10002*/:
                                return "Big5";
                            case Constants.CP_MAC_KOREAN /*10003*/:
                                return "EUC-KR";
                            case Constants.CP_MAC_ARABIC /*10004*/:
                                return "MacArabic";
                            case Constants.CP_MAC_HEBREW /*10005*/:
                                return "MacHebrew";
                            case Constants.CP_MAC_GREEK /*10006*/:
                                return "MacGreek";
                            case Constants.CP_MAC_CYRILLIC /*10007*/:
                                return "MacCyrillic";
                            case Constants.CP_MAC_CHINESE_SIMPLE /*10008*/:
                                return "EUC_CN";
                            default:
                                switch (codepage) {
                                    case Constants.CP_MAC_TURKISH /*10081*/:
                                        return "MacTurkish";
                                    case Constants.CP_MAC_CROATIAN /*10082*/:
                                        return "MacCroatian";
                                    default:
                                        switch (codepage) {
                                            case Constants.CP_ISO_8859_1 /*28591*/:
                                                return "ISO-8859-1";
                                            case Constants.CP_ISO_8859_2 /*28592*/:
                                                return "ISO-8859-2";
                                            case Constants.CP_ISO_8859_3 /*28593*/:
                                                return "ISO-8859-3";
                                            case Constants.CP_ISO_8859_4 /*28594*/:
                                                return "ISO-8859-4";
                                            case Constants.CP_ISO_8859_5 /*28595*/:
                                                return "ISO-8859-5";
                                            case Constants.CP_ISO_8859_6 /*28596*/:
                                                return "ISO-8859-6";
                                            case Constants.CP_ISO_8859_7 /*28597*/:
                                                return "ISO-8859-7";
                                            case Constants.CP_ISO_8859_8 /*28598*/:
                                                return "ISO-8859-8";
                                            case Constants.CP_ISO_8859_9 /*28599*/:
                                                return "ISO-8859-9";
                                            default:
                                                switch (codepage) {
                                                    case Constants.CP_ISO_2022_JP1 /*50220*/:
                                                    case Constants.CP_ISO_2022_JP2 /*50221*/:
                                                    case Constants.CP_ISO_2022_JP3 /*50222*/:
                                                        return "ISO-2022-JP";
                                                    default:
                                                        switch (codepage) {
                                                            case Constants.CP_US_ASCII2 /*65000*/:
                                                                break;
                                                            case Constants.CP_UTF8 /*65001*/:
                                                                return "UTF-8";
                                                            default:
                                                                switch (codepage) {
                                                                    case 37:
                                                                        return "cp037";
                                                                    case Constants.CP_SJIS /*932*/:
                                                                        return "SJIS";
                                                                    case Constants.CP_GBK /*936*/:
                                                                        return "GBK";
                                                                    case Constants.CP_MS949 /*949*/:
                                                                        return "ms949";
                                                                    case Constants.CP_JOHAB /*1361*/:
                                                                        return "johab";
                                                                    case Constants.CP_MAC_ROMANIA /*10010*/:
                                                                        return "MacRomania";
                                                                    case Constants.CP_MAC_UKRAINE /*10017*/:
                                                                        return "MacUkraine";
                                                                    case Constants.CP_MAC_THAI /*10021*/:
                                                                        return "MacThai";
                                                                    case Constants.CP_MAC_CENTRAL_EUROPE /*10029*/:
                                                                        return "MacCentralEurope";
                                                                    case Constants.CP_MAC_ICELAND /*10079*/:
                                                                        return "MacIceland";
                                                                    case Constants.CP_US_ACSII /*20127*/:
                                                                        break;
                                                                    case Constants.CP_KOI8_R /*20866*/:
                                                                        return "KOI8-R";
                                                                    case Constants.CP_ISO_2022_KR /*50225*/:
                                                                        return "ISO-2022-KR";
                                                                    case Constants.CP_EUC_JP /*51932*/:
                                                                        return "EUC-JP";
                                                                    case Constants.CP_EUC_KR /*51949*/:
                                                                        return "EUC-KR";
                                                                    case Constants.CP_GB2312 /*52936*/:
                                                                        return "GB2312";
                                                                    case Constants.CP_GB18030 /*54936*/:
                                                                        return "GB18030";
                                                                    default:
                                                                        StringBuilder sb2 = new StringBuilder();
                                                                        sb2.append("cp");
                                                                        sb2.append(codepage);
                                                                        return sb2.toString();
                                                                }
                                                        }
                                                        return "US-ASCII";
                                                }
                                        }
                                }
                        }
                }
        }
    }

    public static int write(OutputStream out, long type, Object value, int codepage) throws IOException, WritingNotSupportedException {
        int i = (int) type;
        if (i == 0) {
            TypeWriter.writeUIntToStream(out, 0);
            return 4;
        } else if (i == 5) {
            return 0 + TypeWriter.writeToStream(out, ((Double) value).doubleValue());
        } else {
            int trueOrFalse = 0;
            if (i == 11) {
                if (((Boolean) value).booleanValue()) {
                    trueOrFalse = 1;
                }
                return TypeWriter.writeUIntToStream(out, (long) trueOrFalse);
            } else if (i == 20) {
                TypeWriter.writeToStream(out, ((Long) value).longValue());
                return 8;
            } else if (i == 64) {
                long filetime = Util.dateToFileTime((Date) value);
                return 0 + TypeWriter.writeUIntToStream(out, ((long) ((int) (filetime & 4294967295L))) & 4294967295L) + TypeWriter.writeUIntToStream(out, 4294967295L & ((long) ((int) ((filetime >> 32) & 4294967295L))));
            } else if (i != 71) {
                switch (i) {
                    case 2:
                        TypeWriter.writeToStream(out, ((Integer) value).shortValue());
                        return 2;
                    case 3:
                        if (value instanceof Integer) {
                            return 0 + TypeWriter.writeToStream(out, ((Integer) value).intValue());
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Could not cast an object to ");
                        sb.append(Integer.class.toString());
                        sb.append(": ");
                        sb.append(value.getClass().toString());
                        sb.append(", ");
                        sb.append(value.toString());
                        throw new ClassCastException(sb.toString());
                    default:
                        switch (i) {
                            case 30:
                                byte[] bytes = codepage == -1 ? ((String) value).getBytes() : ((String) value).getBytes(codepageToEncoding(codepage));
                                int length = TypeWriter.writeUIntToStream(out, (long) (bytes.length + 1));
                                byte[] b = new byte[(bytes.length + 1)];
                                System.arraycopy(bytes, 0, b, 0, bytes.length);
                                b[b.length - 1] = 0;
                                out.write(b);
                                return length + b.length;
                            case 31:
                                int length2 = 0 + TypeWriter.writeUIntToStream(out, (long) (((String) value).length() + 1));
                                char[] s = Util.pad4((String) value);
                                int length3 = length2;
                                for (int i2 = 0; i2 < s.length; i2++) {
                                    byte highb = (byte) ((s[i2] & 65280) >> 8);
                                    out.write((byte) (s[i2] & 255));
                                    out.write(highb);
                                    length3 += 2;
                                }
                                out.write(0);
                                out.write(0);
                                return length3 + 2;
                            default:
                                if (value instanceof byte[]) {
                                    byte[] b2 = (byte[]) value;
                                    out.write(b2);
                                    int length4 = b2.length;
                                    writeUnsupportedTypeMessage(new WritingNotSupportedException(type, value));
                                    return length4;
                                }
                                throw new WritingNotSupportedException(type, value);
                        }
                }
            } else {
                byte[] b3 = (byte[]) value;
                out.write(b3);
                return b3.length;
            }
        }
    }
}
