package com.google.android.gms.common.images;

import org.apache.poi.p009ss.usermodel.ShapeTypes;

public final class Size {
    private final int zzalv;
    private final int zzalw;

    public Size(int i, int i2) {
        this.zzalv = i;
        this.zzalw = i2;
    }

    public static Size parseSize(String str) throws NumberFormatException {
        if (str == null) {
            throw new IllegalArgumentException("string must not be null");
        }
        int indexOf = str.indexOf(42);
        if (indexOf < 0) {
            indexOf = str.indexOf(ShapeTypes.CLOUD_CALLOUT);
        }
        if (indexOf < 0) {
            throw zzgd(str);
        }
        try {
            return new Size(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1)));
        } catch (NumberFormatException e) {
            throw zzgd(str);
        }
    }

    private static NumberFormatException zzgd(String str) {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 16);
        sb.append("Invalid Size: \"");
        sb.append(str);
        sb.append("\"");
        throw new NumberFormatException(sb.toString());
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Size) {
            Size size = (Size) obj;
            return this.zzalv == size.zzalv && this.zzalw == size.zzalw;
        }
    }

    public final int getHeight() {
        return this.zzalw;
    }

    public final int getWidth() {
        return this.zzalv;
    }

    public final int hashCode() {
        return this.zzalw ^ ((this.zzalv << 16) | (this.zzalv >>> 16));
    }

    public final String toString() {
        int i = this.zzalv;
        int i2 = this.zzalw;
        StringBuilder sb = new StringBuilder(23);
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        return sb.toString();
    }
}
