package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;

final class zzfhq extends zzfes {
    /* access modifiers changed from: private */
    public static final int[] zzpjg;
    private final int zzpjh;
    /* access modifiers changed from: private */
    public final zzfes zzpji;
    /* access modifiers changed from: private */
    public final zzfes zzpjj;
    private final int zzpjk;
    private final int zzpjl;

    static {
        ArrayList arrayList = new ArrayList();
        int i = 1;
        int i2 = 1;
        while (i > 0) {
            arrayList.add(Integer.valueOf(i));
            int i3 = i2 + i;
            i2 = i;
            i = i3;
        }
        arrayList.add(Integer.valueOf(Integer.MAX_VALUE));
        zzpjg = new int[arrayList.size()];
        for (int i4 = 0; i4 < zzpjg.length; i4++) {
            zzpjg[i4] = ((Integer) arrayList.get(i4)).intValue();
        }
    }

    private zzfhq(zzfes zzfes, zzfes zzfes2) {
        this.zzpji = zzfes;
        this.zzpjj = zzfes2;
        this.zzpjk = zzfes.size();
        this.zzpjh = this.zzpjk + zzfes2.size();
        this.zzpjl = Math.max(zzfes.zzcvn(), zzfes2.zzcvn()) + 1;
    }

    static zzfes zza(zzfes zzfes, zzfes zzfes2) {
        if (zzfes2.size() == 0) {
            return zzfes;
        }
        if (zzfes.size() == 0) {
            return zzfes2;
        }
        int size = zzfes.size() + zzfes2.size();
        if (size < 128) {
            return zzb(zzfes, zzfes2);
        }
        if (zzfes instanceof zzfhq) {
            zzfhq zzfhq = (zzfhq) zzfes;
            if (zzfhq.zzpjj.size() + zzfes2.size() < 128) {
                return new zzfhq(zzfhq.zzpji, zzb(zzfhq.zzpjj, zzfes2));
            } else if (zzfhq.zzpji.zzcvn() > zzfhq.zzpjj.zzcvn() && zzfhq.zzcvn() > zzfes2.zzcvn()) {
                return new zzfhq(zzfhq.zzpji, new zzfhq(zzfhq.zzpjj, zzfes2));
            }
        }
        return size >= zzpjg[Math.max(zzfes.zzcvn(), zzfes2.zzcvn()) + 1] ? new zzfhq(zzfes, zzfes2) : new zzfhs().zzc(zzfes, zzfes2);
    }

    private static zzfes zzb(zzfes zzfes, zzfes zzfes2) {
        int size = zzfes.size();
        int size2 = zzfes2.size();
        byte[] bArr = new byte[(size + size2)];
        zzfes.zza(bArr, 0, 0, size);
        zzfes2.zza(bArr, 0, size, size2);
        return zzfes.zzba(bArr);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfes)) {
            return false;
        }
        zzfes zzfes = (zzfes) obj;
        if (this.zzpjh != zzfes.size()) {
            return false;
        }
        if (this.zzpjh == 0) {
            return true;
        }
        int zzcvp = zzcvp();
        int zzcvp2 = zzfes.zzcvp();
        if (zzcvp != 0 && zzcvp2 != 0 && zzcvp != zzcvp2) {
            return false;
        }
        zzfht zzfht = new zzfht(this);
        zzfey zzfey = (zzfey) zzfht.next();
        zzfht zzfht2 = new zzfht(zzfes);
        zzfey zzfey2 = (zzfey) zzfht2.next();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int size = zzfey.size() - i;
            int size2 = zzfey2.size() - i2;
            int min = Math.min(size, size2);
            if (!(i == 0 ? zzfey.zza(zzfey2, i2, min) : zzfey2.zza(zzfey, i, min))) {
                return false;
            }
            i3 += min;
            if (i3 < this.zzpjh) {
                if (min == size) {
                    zzfey = (zzfey) zzfht.next();
                    i = 0;
                } else {
                    i += min;
                }
                if (min == size2) {
                    zzfey2 = (zzfey) zzfht2.next();
                    i2 = 0;
                } else {
                    i2 += min;
                }
            } else if (i3 == this.zzpjh) {
                return true;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    public final int size() {
        return this.zzpjh;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzfer zzfer) throws IOException {
        this.zzpji.zza(zzfer);
        this.zzpjj.zza(zzfer);
    }

    /* access modifiers changed from: protected */
    public final void zzb(byte[] bArr, int i, int i2, int i3) {
        if (i + i3 <= this.zzpjk) {
            this.zzpji.zzb(bArr, i, i2, i3);
        } else if (i >= this.zzpjk) {
            this.zzpjj.zzb(bArr, i - this.zzpjk, i2, i3);
        } else {
            int i4 = this.zzpjk - i;
            this.zzpji.zzb(bArr, i, i2, i4);
            this.zzpjj.zzb(bArr, 0, i2 + i4, i3 - i4);
        }
    }

    public final zzffb zzcvm() {
        return zzffb.zzi(new zzfhu(this));
    }

    /* access modifiers changed from: protected */
    public final int zzcvn() {
        return this.zzpjl;
    }

    /* access modifiers changed from: protected */
    public final boolean zzcvo() {
        return this.zzpjh >= zzpjg[this.zzpjl];
    }

    /* access modifiers changed from: protected */
    public final int zzg(int i, int i2, int i3) {
        if (i2 + i3 <= this.zzpjk) {
            return this.zzpji.zzg(i, i2, i3);
        }
        if (i2 >= this.zzpjk) {
            return this.zzpjj.zzg(i, i2 - this.zzpjk, i3);
        }
        int i4 = this.zzpjk - i2;
        return this.zzpjj.zzg(this.zzpji.zzg(i, i2, i4), 0, i3 - i4);
    }

    public final byte zzkn(int i) {
        zzfes zzfes;
        zzy(i, this.zzpjh);
        if (i < this.zzpjk) {
            zzfes = this.zzpji;
        } else {
            zzfes = this.zzpjj;
            i -= this.zzpjk;
        }
        return zzfes.zzkn(i);
    }

    public final zzfes zzx(int i, int i2) {
        zzfes zzfes;
        int zzh = zzh(i, i2, this.zzpjh);
        if (zzh == 0) {
            return zzfes.zzpfg;
        }
        if (zzh == this.zzpjh) {
            return this;
        }
        if (i2 <= this.zzpjk) {
            zzfes = this.zzpji;
        } else if (i >= this.zzpjk) {
            zzfes = this.zzpjj;
            i -= this.zzpjk;
            i2 -= this.zzpjk;
        } else {
            zzfes zzfes2 = this.zzpji;
            return new zzfhq(zzfes2.zzx(i, zzfes2.size()), this.zzpjj.zzx(0, i2 - this.zzpjk));
        }
        return zzfes.zzx(i, i2);
    }
}
