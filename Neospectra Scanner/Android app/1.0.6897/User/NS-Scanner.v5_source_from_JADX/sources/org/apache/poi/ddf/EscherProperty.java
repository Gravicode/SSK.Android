package org.apache.poi.ddf;

public abstract class EscherProperty {
    private short _id;

    public abstract int serializeComplexPart(byte[] bArr, int i);

    public abstract int serializeSimplePart(byte[] bArr, int i);

    public EscherProperty(short id) {
        this._id = id;
    }

    public EscherProperty(short propertyNumber, boolean isComplex, boolean isBlipId) {
        int i = 0;
        int i2 = (isComplex ? (short) 32768 : 0) + propertyNumber;
        if (isBlipId) {
            i = 16384;
        }
        this._id = (short) (i2 + i);
    }

    public short getId() {
        return this._id;
    }

    public short getPropertyNumber() {
        return (short) (this._id & 16383);
    }

    public boolean isComplex() {
        return (this._id & Short.MIN_VALUE) != 0;
    }

    public boolean isBlipId() {
        return (this._id & 16384) != 0;
    }

    public String getName() {
        return EscherProperties.getPropertyName(this._id);
    }

    public int getPropertySize() {
        return 6;
    }
}
