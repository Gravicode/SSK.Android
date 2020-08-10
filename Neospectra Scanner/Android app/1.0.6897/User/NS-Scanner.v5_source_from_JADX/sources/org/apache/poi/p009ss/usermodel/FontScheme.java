package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.FontScheme */
public enum FontScheme {
    NONE(1),
    MAJOR(2),
    MINOR(3);
    
    private static FontScheme[] _table;
    private int value;

    static {
        int i$;
        FontScheme[] arr$;
        _table = new FontScheme[4];
        for (FontScheme c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private FontScheme(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public static FontScheme valueOf(int value2) {
        return _table[value2];
    }
}
