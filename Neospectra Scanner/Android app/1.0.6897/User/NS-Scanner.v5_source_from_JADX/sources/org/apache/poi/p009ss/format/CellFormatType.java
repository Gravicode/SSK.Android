package org.apache.poi.p009ss.format;

/* renamed from: org.apache.poi.ss.format.CellFormatType */
public enum CellFormatType {
    GENERAL {
        /* access modifiers changed from: 0000 */
        public CellFormatter formatter(String pattern) {
            return new CellGeneralFormatter();
        }

        /* access modifiers changed from: 0000 */
        public boolean isSpecial(char ch) {
            return false;
        }
    },
    NUMBER {
        /* access modifiers changed from: 0000 */
        public boolean isSpecial(char ch) {
            return false;
        }

        /* access modifiers changed from: 0000 */
        public CellFormatter formatter(String pattern) {
            return new CellNumberFormatter(pattern);
        }
    },
    DATE {
        /* access modifiers changed from: 0000 */
        public boolean isSpecial(char ch) {
            return ch == '\'' || (ch <= 127 && Character.isLetter(ch));
        }

        /* access modifiers changed from: 0000 */
        public CellFormatter formatter(String pattern) {
            return new CellDateFormatter(pattern);
        }
    },
    ELAPSED {
        /* access modifiers changed from: 0000 */
        public boolean isSpecial(char ch) {
            return false;
        }

        /* access modifiers changed from: 0000 */
        public CellFormatter formatter(String pattern) {
            return new CellElapsedFormatter(pattern);
        }
    },
    TEXT {
        /* access modifiers changed from: 0000 */
        public boolean isSpecial(char ch) {
            return false;
        }

        /* access modifiers changed from: 0000 */
        public CellFormatter formatter(String pattern) {
            return new CellTextFormatter(pattern);
        }
    };

    /* access modifiers changed from: 0000 */
    public abstract CellFormatter formatter(String str);

    /* access modifiers changed from: 0000 */
    public abstract boolean isSpecial(char c);
}
