package org.apache.poi.hpsf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.apache.poi.util.LittleEndian;

public class Section {
    protected Map<Long, String> dictionary;
    protected ClassID formatID;
    protected long offset;
    protected Property[] properties;
    protected int size;
    private boolean wasNull;

    class PropertyListEntry implements Comparable<PropertyListEntry> {

        /* renamed from: id */
        int f811id;
        int length;
        int offset;

        PropertyListEntry() {
        }

        public int compareTo(PropertyListEntry o) {
            int otherOffset = o.offset;
            if (this.offset < otherOffset) {
                return -1;
            }
            if (this.offset == otherOffset) {
                return 0;
            }
            return 1;
        }

        public String toString() {
            StringBuffer b = new StringBuffer();
            b.append(getClass().getName());
            b.append("[id=");
            b.append(this.f811id);
            b.append(", offset=");
            b.append(this.offset);
            b.append(", length=");
            b.append(this.length);
            b.append(']');
            return b.toString();
        }
    }

    public ClassID getFormatID() {
        return this.formatID;
    }

    public long getOffset() {
        return this.offset;
    }

    public int getSize() {
        return this.size;
    }

    public int getPropertyCount() {
        return this.properties.length;
    }

    public Property[] getProperties() {
        return this.properties;
    }

    protected Section() {
    }

    public Section(byte[] src, int offset2) throws UnsupportedEncodingException {
        byte[] bArr = src;
        int o1 = offset2;
        this.formatID = new ClassID(bArr, o1);
        this.offset = LittleEndian.getUInt(bArr, o1 + 16);
        int o12 = (int) this.offset;
        this.size = (int) LittleEndian.getUInt(bArr, o12);
        int o13 = o12 + 4;
        int propertyCount = (int) LittleEndian.getUInt(bArr, o13);
        int o14 = o13 + 4;
        this.properties = new Property[propertyCount];
        int pass1Offset = o14;
        List<PropertyListEntry> propertyList = new ArrayList<>(propertyCount);
        int i = 0;
        int pass1Offset2 = pass1Offset;
        for (int i2 = 0; i2 < this.properties.length; i2++) {
            PropertyListEntry ple = new PropertyListEntry();
            ple.f811id = (int) LittleEndian.getUInt(bArr, pass1Offset2);
            int pass1Offset3 = pass1Offset2 + 4;
            ple.offset = (int) LittleEndian.getUInt(bArr, pass1Offset3);
            pass1Offset2 = pass1Offset3 + 4;
            propertyList.add(ple);
        }
        Collections.sort(propertyList);
        while (true) {
            int i3 = i;
            if (i3 >= propertyCount - 1) {
                break;
            }
            PropertyListEntry ple1 = (PropertyListEntry) propertyList.get(i3);
            ple1.length = ((PropertyListEntry) propertyList.get(i3 + 1)).offset - ple1.offset;
            i = i3 + 1;
        }
        if (propertyCount > 0) {
            PropertyListEntry ple2 = (PropertyListEntry) propertyList.get(propertyCount - 1);
            ple2.length = this.size - ple2.offset;
        }
        Iterator<PropertyListEntry> i4 = propertyList.iterator();
        int codepage = -1;
        while (codepage == -1 && i4.hasNext()) {
            PropertyListEntry ple3 = (PropertyListEntry) i4.next();
            if (ple3.f811id == 1) {
                int o = (int) (this.offset + ((long) ple3.offset));
                long type = LittleEndian.getUInt(bArr, o);
                int o2 = o + 4;
                if (type != 2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Value type of property ID 1 is not VT_I2 but ");
                    sb.append(type);
                    sb.append(".");
                    throw new HPSFRuntimeException(sb.toString());
                }
                codepage = LittleEndian.getUShort(bArr, o2);
            }
        }
        Iterator it = propertyList.iterator();
        int i1 = 0;
        while (true) {
            Iterator it2 = it;
            if (it2.hasNext()) {
                PropertyListEntry ple4 = (PropertyListEntry) it2.next();
                int propertyCount2 = propertyCount;
                int o15 = o14;
                PropertyListEntry propertyListEntry = ple4;
                Iterator it3 = it2;
                Property p = new Property((long) ple4.f811id, bArr, this.offset + ((long) ple4.offset), ple4.length, codepage);
                if (p.getID() == 1) {
                    Property property = new Property(p.getID(), p.getType(), Integer.valueOf(codepage));
                    p = property;
                }
                int i12 = i1 + 1;
                this.properties[i1] = p;
                i1 = i12;
                it = it3;
                propertyCount = propertyCount2;
                o14 = o15;
            } else {
                int i5 = o14;
                this.dictionary = (Map) getProperty(0);
                return;
            }
        }
    }

    public Object getProperty(long id) {
        this.wasNull = false;
        for (int i = 0; i < this.properties.length; i++) {
            if (id == this.properties[i].getID()) {
                return this.properties[i].getValue();
            }
        }
        this.wasNull = true;
        return null;
    }

    /* access modifiers changed from: protected */
    public int getPropertyIntValue(long id) {
        Object o = getProperty(id);
        if (o == null) {
            return 0;
        }
        if ((o instanceof Long) || (o instanceof Integer)) {
            return ((Number) o).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("This property is not an integer type, but ");
        sb.append(o.getClass().getName());
        sb.append(".");
        throw new HPSFRuntimeException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public boolean getPropertyBooleanValue(int id) {
        Boolean b = (Boolean) getProperty((long) id);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    public boolean wasNull() {
        return this.wasNull;
    }

    public String getPIDString(long pid) {
        String s = null;
        if (this.dictionary != null) {
            s = (String) this.dictionary.get(Long.valueOf(pid));
        }
        if (s == null) {
            s = SectionIDMap.getPIDString(getFormatID().getBytes(), pid);
        }
        if (s == null) {
            return SectionIDMap.UNDEFINED;
        }
        return s;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Section)) {
            return false;
        }
        Section s = (Section) o;
        if (!s.getFormatID().equals(getFormatID())) {
            return false;
        }
        Property[] pa1 = new Property[getProperties().length];
        Property[] pa2 = new Property[s.getProperties().length];
        System.arraycopy(getProperties(), 0, pa1, 0, pa1.length);
        System.arraycopy(s.getProperties(), 0, pa2, 0, pa2.length);
        Property p20 = null;
        Property p10 = null;
        Property[] pa12 = pa1;
        int i = 0;
        while (i < pa12.length) {
            long id = pa12[i].getID();
            if (id == 0) {
                p10 = pa12[i];
                pa12 = remove(pa12, i);
                i--;
            }
            if (id == 1) {
                pa12 = remove(pa12, i);
                i--;
            }
            i++;
        }
        int i2 = 0;
        while (i2 < pa2.length) {
            long id2 = pa2[i2].getID();
            if (id2 == 0) {
                p20 = pa2[i2];
                pa2 = remove(pa2, i2);
                i2--;
            }
            if (id2 == 1) {
                pa2 = remove(pa2, i2);
                i2--;
            }
            i2++;
        }
        if (pa12.length != pa2.length) {
            return false;
        }
        boolean dictionaryEqual = true;
        if (p10 != null && p20 != null) {
            dictionaryEqual = p10.getValue().equals(p20.getValue());
        } else if (!(p10 == null && p20 == null)) {
            dictionaryEqual = false;
        }
        if (dictionaryEqual) {
            return Util.equals((Object[]) pa12, (Object[]) pa2);
        }
        return false;
    }

    private Property[] remove(Property[] pa, int i) {
        Property[] h = new Property[(pa.length - 1)];
        if (i > 0) {
            System.arraycopy(pa, 0, h, 0, i);
        }
        System.arraycopy(pa, i + 1, h, i, h.length - i);
        return h;
    }

    public int hashCode() {
        long hashCode = 0 + ((long) getFormatID().hashCode());
        for (Property hashCode2 : getProperties()) {
            hashCode += (long) hashCode2.hashCode();
        }
        return (int) (4294967295L & hashCode);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        Property[] pa = getProperties();
        b.append(getClass().getName());
        b.append('[');
        b.append("formatID: ");
        b.append(getFormatID());
        b.append(", offset: ");
        b.append(getOffset());
        b.append(", propertyCount: ");
        b.append(getPropertyCount());
        b.append(", size: ");
        b.append(getSize());
        b.append(", properties: [\n");
        for (Property property : pa) {
            b.append(property.toString());
            b.append(",\n");
        }
        b.append(']');
        b.append(']');
        return b.toString();
    }

    public Map<Long, String> getDictionary() {
        return this.dictionary;
    }

    public int getCodepage() {
        Integer codepage = (Integer) getProperty(1);
        if (codepage == null) {
            return -1;
        }
        return codepage.intValue();
    }
}
