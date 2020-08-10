package org.apache.poi.hpsf;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class Property {

    /* renamed from: id */
    protected long f810id;
    protected long type;
    protected Object value;

    public long getID() {
        return this.f810id;
    }

    public long getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }

    public Property(long id, long type2, Object value2) {
        this.f810id = id;
        this.type = type2;
        this.value = value2;
    }

    public Property(long id, byte[] src, long offset, int length, int codepage) throws UnsupportedEncodingException {
        long j = id;
        this.f810id = j;
        if (j == 0) {
            this.value = readDictionary(src, offset, length, codepage);
            return;
        }
        int o = (int) offset;
        byte[] bArr = src;
        this.type = LittleEndian.getUInt(bArr, o);
        try {
            this.value = VariantSupport.read(bArr, o + 4, length, (long) ((int) this.type), codepage);
        } catch (UnsupportedVariantTypeException e) {
            UnsupportedVariantTypeException ex = e;
            VariantSupport.writeUnsupportedTypeMessage(ex);
            this.value = ex.getValue();
        }
    }

    protected Property() {
    }

    /* access modifiers changed from: protected */
    public Map readDictionary(byte[] src, long offset, int length, int codepage) throws UnsupportedEncodingException {
        RuntimeException ex;
        long nrEntries;
        int o;
        byte[] bArr = src;
        long j = offset;
        int i = codepage;
        if (j < 0 || j > ((long) bArr.length)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal offset ");
            sb.append(offset);
            sb.append(" while HPSF stream contains ");
            sb.append(length);
            sb.append(" bytes.");
            throw new HPSFRuntimeException(sb.toString());
        }
        int o2 = (int) j;
        long nrEntries2 = LittleEndian.getUInt(bArr, o2);
        int o3 = o2 + 4;
        Map m = new HashMap((int) nrEntries2, 1.0f);
        int o4 = o3;
        int i2 = 0;
        while (((long) i2) < nrEntries2) {
            try {
                Long id = Long.valueOf(LittleEndian.getUInt(bArr, o4));
                int o5 = o4 + 4;
                long sLength = LittleEndian.getUInt(bArr, o5);
                int o6 = o5 + 4;
                StringBuffer b = new StringBuffer();
                if (i == -1) {
                    nrEntries = nrEntries2;
                    b.append(new String(bArr, o6, (int) sLength));
                } else if (i != 1200) {
                    nrEntries = nrEntries2;
                    try {
                        b.append(new String(bArr, o6, (int) sLength, VariantSupport.codepageToEncoding(codepage)));
                    } catch (RuntimeException e) {
                        ex = e;
                        POILogger l = POILogFactory.getLogger(getClass());
                        int i3 = POILogger.WARN;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("The property set's dictionary contains bogus data. All dictionary entries starting with the one with ID ");
                        sb2.append(this.f810id);
                        sb2.append(" will be ignored.");
                        l.log(i3, (Object) sb2.toString(), (Throwable) ex);
                        return m;
                    }
                } else {
                    nrEntries = nrEntries2;
                    int nrBytes = (int) (sLength * 2);
                    byte[] h = new byte[nrBytes];
                    for (int i22 = 0; i22 < nrBytes; i22 += 2) {
                        h[i22] = bArr[o6 + i22 + 1];
                        h[i22 + 1] = bArr[o6 + i22];
                    }
                    b.append(new String(h, 0, nrBytes, VariantSupport.codepageToEncoding(codepage)));
                }
                while (b.length() > 0 && b.charAt(b.length() - 1) == 0) {
                    b.setLength(b.length() - 1);
                }
                if (i == 1200) {
                    if (sLength % 2 == 1) {
                        sLength++;
                    }
                    o = (int) (((long) o6) + sLength + sLength);
                } else {
                    o = (int) (((long) o6) + sLength);
                }
                o4 = o;
                m.put(id, b.toString());
                i2++;
                nrEntries2 = nrEntries;
                long j2 = offset;
            } catch (RuntimeException e2) {
                long j3 = nrEntries2;
                ex = e2;
                POILogger l2 = POILogFactory.getLogger(getClass());
                int i32 = POILogger.WARN;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("The property set's dictionary contains bogus data. All dictionary entries starting with the one with ID ");
                sb22.append(this.f810id);
                sb22.append(" will be ignored.");
                l2.log(i32, (Object) sb22.toString(), (Throwable) ex);
                return m;
            }
        }
        return m;
    }

    /* access modifiers changed from: protected */
    public int getSize() throws WritingNotSupportedException {
        int length = VariantSupport.getVariantLength(this.type);
        if (length >= 0) {
            return length;
        }
        if (length == -2) {
            throw new WritingNotSupportedException(this.type, null);
        }
        int i = (int) this.type;
        if (i != 0) {
            if (i != 30) {
                throw new WritingNotSupportedException(this.type, this.value);
            }
            int l = ((String) this.value).length() + 1;
            int r = l % 4;
            if (r > 0) {
                l += 4 - r;
            }
            length += l;
        }
        return length;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Property)) {
            return false;
        }
        Property p = (Property) o;
        Object pValue = p.getValue();
        if (this.f810id != p.getID() || (this.f810id != 0 && !typesAreEqual(this.type, p.getType()))) {
            return false;
        }
        if (this.value == null && pValue == null) {
            return true;
        }
        if (this.value == null || pValue == null) {
            return false;
        }
        Class<?> valueClass = this.value.getClass();
        Class<?> pValueClass = pValue.getClass();
        if (!valueClass.isAssignableFrom(pValueClass) && !pValueClass.isAssignableFrom(valueClass)) {
            return false;
        }
        if (this.value instanceof byte[]) {
            return Util.equal((byte[]) this.value, (byte[]) pValue);
        }
        return this.value.equals(pValue);
    }

    private boolean typesAreEqual(long t1, long t2) {
        if (t1 == t2 || ((t1 == 30 && t2 == 31) || (t2 == 30 && t1 == 31))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long hashCode = 0 + this.f810id + this.type;
        if (this.value != null) {
            hashCode += (long) this.value.hashCode();
        }
        return (int) (4294967295L & hashCode);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(getClass().getName());
        b.append('[');
        b.append("id: ");
        b.append(getID());
        b.append(", type: ");
        b.append(getType());
        Object value2 = getValue();
        b.append(", value: ");
        b.append(value2.toString());
        if (value2 instanceof String) {
            String s = (String) value2;
            int l = s.length();
            byte[] bytes = new byte[(l * 2)];
            for (int i = 0; i < l; i++) {
                char c = s.charAt(i);
                byte low = (byte) ((c & 255) >> 0);
                bytes[i * 2] = (byte) ((65280 & c) >> 8);
                bytes[(i * 2) + 1] = low;
            }
            String hex = HexDump.dump(bytes, 0, 0);
            b.append(" [");
            b.append(hex);
            b.append("]");
        }
        b.append(']');
        return b.toString();
    }
}
