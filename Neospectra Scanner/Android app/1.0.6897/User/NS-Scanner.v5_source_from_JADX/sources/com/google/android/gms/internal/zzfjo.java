package com.google.android.gms.internal;

public final class zzfjo implements Cloneable {
    private static final zzfjp zzpne = new zzfjp();
    private int mSize;
    private boolean zzpnf;
    private int[] zzpng;
    private zzfjp[] zzpnh;

    zzfjo() {
        this(10);
    }

    private zzfjo(int i) {
        this.zzpnf = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.zzpng = new int[idealIntArraySize];
        this.zzpnh = new zzfjp[idealIntArraySize];
        this.mSize = 0;
    }

    private static int idealIntArraySize(int i) {
        int i2 = i << 2;
        int i3 = 4;
        while (true) {
            if (i3 >= 32) {
                break;
            }
            int i4 = (1 << i3) - 12;
            if (i2 <= i4) {
                i2 = i4;
                break;
            }
            i3++;
        }
        return i2 / 4;
    }

    private final int zzml(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzpng[i4];
            if (i5 < i) {
                i3 = i4 + 1;
            } else if (i5 <= i) {
                return i4;
            } else {
                i2 = i4 - 1;
            }
        }
        return ~i3;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        int i = this.mSize;
        zzfjo zzfjo = new zzfjo(i);
        System.arraycopy(this.zzpng, 0, zzfjo.zzpng, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (this.zzpnh[i2] != null) {
                zzfjo.zzpnh[i2] = (zzfjp) this.zzpnh[i2].clone();
            }
        }
        zzfjo.mSize = i;
        return zzfjo;
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfjo)) {
            return false;
        }
        zzfjo zzfjo = (zzfjo) obj;
        if (this.mSize != zzfjo.mSize) {
            return false;
        }
        int[] iArr = this.zzpng;
        int[] iArr2 = zzfjo.zzpng;
        int i = this.mSize;
        int i2 = 0;
        while (true) {
            if (i2 >= i) {
                z = true;
                break;
            } else if (iArr[i2] != iArr2[i2]) {
                z = false;
                break;
            } else {
                i2++;
            }
        }
        if (z) {
            zzfjp[] zzfjpArr = this.zzpnh;
            zzfjp[] zzfjpArr2 = zzfjo.zzpnh;
            int i3 = this.mSize;
            int i4 = 0;
            while (true) {
                if (i4 >= i3) {
                    z2 = true;
                    break;
                } else if (!zzfjpArr[i4].equals(zzfjpArr2[i4])) {
                    z2 = false;
                    break;
                } else {
                    i4++;
                }
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzpng[i2]) * 31) + this.zzpnh[i2].hashCode();
        }
        return i;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    /* access modifiers changed from: 0000 */
    public final int size() {
        return this.mSize;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(int i, zzfjp zzfjp) {
        int zzml = zzml(i);
        if (zzml >= 0) {
            this.zzpnh[zzml] = zzfjp;
            return;
        }
        int i2 = ~zzml;
        if (i2 >= this.mSize || this.zzpnh[i2] != zzpne) {
            if (this.mSize >= this.zzpng.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                int[] iArr = new int[idealIntArraySize];
                zzfjp[] zzfjpArr = new zzfjp[idealIntArraySize];
                System.arraycopy(this.zzpng, 0, iArr, 0, this.zzpng.length);
                System.arraycopy(this.zzpnh, 0, zzfjpArr, 0, this.zzpnh.length);
                this.zzpng = iArr;
                this.zzpnh = zzfjpArr;
            }
            if (this.mSize - i2 != 0) {
                int i3 = i2 + 1;
                System.arraycopy(this.zzpng, i2, this.zzpng, i3, this.mSize - i2);
                System.arraycopy(this.zzpnh, i2, this.zzpnh, i3, this.mSize - i2);
            }
            this.zzpng[i2] = i;
            this.zzpnh[i2] = zzfjp;
            this.mSize++;
            return;
        }
        this.zzpng[i2] = i;
        this.zzpnh[i2] = zzfjp;
    }

    /* access modifiers changed from: 0000 */
    public final zzfjp zzmj(int i) {
        int zzml = zzml(i);
        if (zzml < 0 || this.zzpnh[zzml] == zzpne) {
            return null;
        }
        return this.zzpnh[zzml];
    }

    /* access modifiers changed from: 0000 */
    public final zzfjp zzmk(int i) {
        return this.zzpnh[i];
    }
}
