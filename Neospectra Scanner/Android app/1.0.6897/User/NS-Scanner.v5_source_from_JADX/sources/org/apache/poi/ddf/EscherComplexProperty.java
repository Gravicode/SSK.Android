package org.apache.poi.ddf;

import java.util.Arrays;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherComplexProperty extends EscherProperty {
    protected byte[] _complexData;

    public EscherComplexProperty(short id, byte[] complexData) {
        super(id);
        this._complexData = complexData;
    }

    public EscherComplexProperty(short propertyNumber, boolean isBlipId, byte[] complexData) {
        super(propertyNumber, true, isBlipId);
        this._complexData = complexData;
    }

    public int serializeSimplePart(byte[] data, int pos) {
        LittleEndian.putShort(data, pos, getId());
        LittleEndian.putInt(data, pos + 2, this._complexData.length);
        return 6;
    }

    public int serializeComplexPart(byte[] data, int pos) {
        System.arraycopy(this._complexData, 0, data, pos, this._complexData.length);
        return this._complexData.length;
    }

    public byte[] getComplexData() {
        return this._complexData;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EscherComplexProperty)) {
            return false;
        }
        if (!Arrays.equals(this._complexData, ((EscherComplexProperty) o)._complexData)) {
            return false;
        }
        return true;
    }

    public int getPropertySize() {
        return this._complexData.length + 6;
    }

    public int hashCode() {
        return getId() * 11;
    }

    public String toString() {
        String dataStr = HexDump.toHex(this._complexData, 32);
        StringBuilder sb = new StringBuilder();
        sb.append("propNum: ");
        sb.append(getPropertyNumber());
        sb.append(", propName: ");
        sb.append(EscherProperties.getPropertyName(getPropertyNumber()));
        sb.append(", complex: ");
        sb.append(isComplex());
        sb.append(", blipId: ");
        sb.append(isBlipId());
        sb.append(", data: ");
        sb.append(System.getProperty("line.separator"));
        sb.append(dataStr);
        return sb.toString();
    }
}
