package com.google.android.gms.internal;

import java.io.IOException;

class zzfez extends zzfey {
    protected final byte[] zzjng;

    zzfez(byte[] bArr) {
        this.zzjng = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfes) || size() != ((zzfes) obj).size()) {
            return false;
        }
        if (size() == 0) {
            return true;
        }
        if (!(obj instanceof zzfez)) {
            return obj.equals(this);
        }
        zzfez zzfez = (zzfez) obj;
        int zzcvp = zzcvp();
        int zzcvp2 = zzfez.zzcvp();
        if (zzcvp == 0 || zzcvp2 == 0 || zzcvp == zzcvp2) {
            return zza(zzfez, 0, size());
        }
        return false;
    }

    public int size() {
        return this.zzjng.length;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzfer zzfer) throws IOException {
        zzfer.zzd(this.zzjng, zzcvq(), size());
    }

    /* access modifiers changed from: 0000 */
    public final boolean zza(zzfes zzfes, int i, int i2) {
        if (i2 > zzfes.size()) {
            int size = size();
            StringBuilder sb = new StringBuilder(40);
            sb.append("Length too large: ");
            sb.append(i2);
            sb.append(size);
            throw new IllegalArgumentException(sb.toString());
        }
        int i3 = i + i2;
        if (i3 > zzfes.size()) {
            int size2 = zzfes.size();
            StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: ");
            sb2.append(i);
            sb2.append(", ");
            sb2.append(i2);
            sb2.append(", ");
            sb2.append(size2);
            throw new IllegalArgumentException(sb2.toString());
        } else if (!(zzfes instanceof zzfez)) {
            return zzfes.zzx(i, i3).equals(zzx(0, i2));
        } else {
            zzfez zzfez = (zzfez) zzfes;
            byte[] bArr = this.zzjng;
            byte[] bArr2 = zzfez.zzjng;
            int zzcvq = zzcvq() + i2;
            int zzcvq2 = zzcvq();
            int zzcvq3 = zzfez.zzcvq() + i;
            while (zzcvq2 < zzcvq) {
                if (bArr[zzcvq2] != bArr2[zzcvq3]) {
                    return false;
                }
                zzcvq2++;
                zzcvq3++;
            }
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void zzb(byte[] bArr, int i, int i2, int i3) {
        System.arraycopy(this.zzjng, i, bArr, i2, i3);
    }

    public final zzffb zzcvm() {
        return zzffb.zzb(this.zzjng, zzcvq(), size(), true);
    }

    /* access modifiers changed from: protected */
    public int zzcvq() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public final int zzg(int i, int i2, int i3) {
        return zzffz.zza(i, this.zzjng, zzcvq() + i2, i3);
    }

    public byte zzkn(int i) {
        return this.zzjng[i];
    }

    public final zzfes zzx(int i, int i2) {
        int zzh = zzh(i, i2, size());
        return zzh == 0 ? zzfes.zzpfg : new zzfev(this.zzjng, zzcvq() + i, zzh);
    }
}
