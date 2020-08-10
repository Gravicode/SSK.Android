package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherSimpleProperty extends EscherProperty {
    protected int propertyValue;

    public EscherSimpleProperty(short id, int propertyValue2) {
        super(id);
        this.propertyValue = propertyValue2;
    }

    public EscherSimpleProperty(short propertyNumber, boolean isComplex, boolean isBlipId, int propertyValue2) {
        super(propertyNumber, isComplex, isBlipId);
        this.propertyValue = propertyValue2;
    }

    public int serializeSimplePart(byte[] data, int offset) {
        LittleEndian.putShort(data, offset, getId());
        LittleEndian.putInt(data, offset + 2, this.propertyValue);
        return 6;
    }

    public int serializeComplexPart(byte[] data, int pos) {
        return 0;
    }

    public int getPropertyValue() {
        return this.propertyValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EscherSimpleProperty)) {
            return false;
        }
        EscherSimpleProperty escherSimpleProperty = (EscherSimpleProperty) o;
        if (this.propertyValue == escherSimpleProperty.propertyValue && getId() == escherSimpleProperty.getId()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.propertyValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("propNum: ");
        sb.append(getPropertyNumber());
        sb.append(", RAW: 0x");
        sb.append(HexDump.toHex(getId()));
        sb.append(", propName: ");
        sb.append(EscherProperties.getPropertyName(getPropertyNumber()));
        sb.append(", complex: ");
        sb.append(isComplex());
        sb.append(", blipId: ");
        sb.append(isBlipId());
        sb.append(", value: ");
        sb.append(this.propertyValue);
        sb.append(" (0x");
        sb.append(HexDump.toHex(this.propertyValue));
        sb.append(")");
        return sb.toString();
    }
}
