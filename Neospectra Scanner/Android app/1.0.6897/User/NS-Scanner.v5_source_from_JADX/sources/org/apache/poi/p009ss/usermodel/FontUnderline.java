package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.FontUnderline */
public enum FontUnderline {
    SINGLE(1),
    DOUBLE(2),
    SINGLE_ACCOUNTING(3),
    DOUBLE_ACCOUNTING(4),
    NONE(5);
    
    private static FontUnderline[] _table;
    private int value;

    static {
        int i$;
        FontUnderline[] arr$;
        _table = new FontUnderline[6];
        for (FontUnderline c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private FontUnderline(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public byte getByteValue() {
        switch (this) {
            case DOUBLE:
                return 2;
            case DOUBLE_ACCOUNTING:
                return 34;
            case SINGLE_ACCOUNTING:
                return 33;
            case NONE:
                return 0;
            case SINGLE:
                return 1;
            default:
                return 1;
        }
    }

    public static FontUnderline valueOf(int value2) {
        return _table[value2];
    }

    public static FontUnderline valueOf(byte value2) {
        switch (value2) {
            case 1:
                return SINGLE;
            case 2:
                return DOUBLE;
            case 33:
                return SINGLE_ACCOUNTING;
            case 34:
                return DOUBLE_ACCOUNTING;
            default:
                return NONE;
        }
    }
}
