package org.apache.poi.p009ss.usermodel;

/* renamed from: org.apache.poi.ss.usermodel.PageOrder */
public enum PageOrder {
    DOWN_THEN_OVER(1),
    OVER_THEN_DOWN(2);
    
    private static PageOrder[] _table;
    private int order;

    static {
        int i$;
        PageOrder[] arr$;
        _table = new PageOrder[3];
        for (PageOrder c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private PageOrder(int order2) {
        this.order = order2;
    }

    public int getValue() {
        return this.order;
    }

    public static PageOrder valueOf(int value) {
        return _table[value];
    }
}
