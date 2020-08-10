package com.google.android.gms.internal;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class zzfjp implements Cloneable {
    private Object value;
    private zzfjn<?, ?> zzpni;
    private List<zzfju> zzpnj = new ArrayList();

    zzfjp() {
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzq()];
        zza(zzfjk.zzbf(bArr));
        return bArr;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzdah */
    public zzfjp clone() {
        Object clone;
        zzfjp zzfjp = new zzfjp();
        try {
            zzfjp.zzpni = this.zzpni;
            if (this.zzpnj == null) {
                zzfjp.zzpnj = null;
            } else {
                zzfjp.zzpnj.addAll(this.zzpnj);
            }
            if (this.value != null) {
                if (this.value instanceof zzfjs) {
                    clone = (zzfjs) ((zzfjs) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    clone = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    if (this.value instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.value;
                        byte[][] bArr2 = new byte[bArr.length][];
                        zzfjp.value = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.value instanceof boolean[]) {
                        clone = ((boolean[]) this.value).clone();
                    } else if (this.value instanceof int[]) {
                        clone = ((int[]) this.value).clone();
                    } else if (this.value instanceof long[]) {
                        clone = ((long[]) this.value).clone();
                    } else if (this.value instanceof float[]) {
                        clone = ((float[]) this.value).clone();
                    } else if (this.value instanceof double[]) {
                        clone = ((double[]) this.value).clone();
                    } else if (this.value instanceof zzfjs[]) {
                        zzfjs[] zzfjsArr = (zzfjs[]) this.value;
                        zzfjs[] zzfjsArr2 = new zzfjs[zzfjsArr.length];
                        zzfjp.value = zzfjsArr2;
                        while (i < zzfjsArr.length) {
                            zzfjsArr2[i] = (zzfjs) zzfjsArr[i].clone();
                            i++;
                        }
                    }
                }
                zzfjp.value = clone;
                return zzfjp;
            }
            return zzfjp;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfjp)) {
            return false;
        }
        zzfjp zzfjp = (zzfjp) obj;
        if (this.value == null || zzfjp.value == null) {
            if (this.zzpnj != null && zzfjp.zzpnj != null) {
                return this.zzpnj.equals(zzfjp.zzpnj);
            }
            try {
                return Arrays.equals(toByteArray(), zzfjp.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else if (this.zzpni != zzfjp.zzpni) {
            return false;
        } else {
            return !this.zzpni.zznfk.isArray() ? this.value.equals(zzfjp.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) zzfjp.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) zzfjp.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) zzfjp.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) zzfjp.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) zzfjp.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) zzfjp.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) zzfjp.value);
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.value != null) {
            zzfjn<?, ?> zzfjn = this.zzpni;
            Object obj = this.value;
            if (zzfjn.zzpnd) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    Object obj2 = Array.get(obj, i);
                    if (obj2 != null) {
                        zzfjn.zza(obj2, zzfjk);
                    }
                }
                return;
            }
            zzfjn.zza(obj, zzfjk);
            return;
        }
        for (zzfju zzfju : this.zzpnj) {
            zzfjk.zzmi(zzfju.tag);
            zzfjk.zzbh(zzfju.zzjng);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzfju zzfju) {
        this.zzpnj.add(zzfju);
    }

    /* access modifiers changed from: 0000 */
    public final <T> T zzb(zzfjn<?, T> zzfjn) {
        if (this.value == null) {
            this.zzpni = zzfjn;
            this.value = zzfjn.zzbq(this.zzpnj);
            this.zzpnj = null;
        } else if (!this.zzpni.equals(zzfjn)) {
            throw new IllegalStateException("Tried to getExtension with a different Extension.");
        }
        return this.value;
    }

    /* access modifiers changed from: 0000 */
    public final int zzq() {
        int i;
        if (this.value != null) {
            zzfjn<?, ?> zzfjn = this.zzpni;
            Object obj = this.value;
            if (!zzfjn.zzpnd) {
                return zzfjn.zzcs(obj);
            }
            int length = Array.getLength(obj);
            i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                if (Array.get(obj, i2) != null) {
                    i += zzfjn.zzcs(Array.get(obj, i2));
                }
            }
        } else {
            i = 0;
            for (zzfju zzfju : this.zzpnj) {
                i += zzfjk.zzlp(zzfju.tag) + 0 + zzfju.zzjng.length;
            }
        }
        return i;
    }
}
