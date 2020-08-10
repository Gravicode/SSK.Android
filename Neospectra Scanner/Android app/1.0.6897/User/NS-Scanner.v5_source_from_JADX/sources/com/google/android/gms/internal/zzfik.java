package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffu.zzb;
import com.google.android.gms.internal.zzffu.zzg;
import com.google.android.gms.internal.zzffu.zzh;
import java.io.IOException;

public final class zzfik extends zzffu<zzfik, zza> implements zzfhg {
    private static volatile zzfhk<zzfik> zzbbk;
    /* access modifiers changed from: private */
    public static final zzfik zzpkn;
    private long zzpkl;
    private int zzpkm;

    public static final class zza extends com.google.android.gms.internal.zzffu.zza<zzfik, zza> implements zzfhg {
        private zza() {
            super(zzfik.zzpkn);
        }

        /* synthetic */ zza(zzfil zzfil) {
            this();
        }

        public final zza zzdg(long j) {
            zzcxr();
            ((zzfik) this.zzpgv).zzdf(j);
            return this;
        }

        public final zza zzmd(int i) {
            zzcxr();
            ((zzfik) this.zzpgv).setNanos(i);
            return this;
        }
    }

    static {
        zzfik zzfik = new zzfik();
        zzpkn = zzfik;
        zzfik.zza(zzg.zzphh, (Object) null, (Object) null);
        zzfik.zzpgr.zzbiy();
    }

    private zzfik() {
    }

    /* access modifiers changed from: private */
    public final void setNanos(int i) {
        this.zzpkm = i;
    }

    public static zza zzczq() {
        zzfik zzfik = zzpkn;
        com.google.android.gms.internal.zzffu.zza zza2 = (com.google.android.gms.internal.zzffu.zza) zzfik.zza(zzg.zzphj, (Object) null, (Object) null);
        zza2.zza(zzfik);
        return (zza) zza2;
    }

    public static zzfik zzczr() {
        return zzpkn;
    }

    /* access modifiers changed from: private */
    public final void zzdf(long j) {
        this.zzpkl = j;
    }

    public final int getNanos() {
        return this.zzpkm;
    }

    public final long getSeconds() {
        return this.zzpkl;
    }

    /* access modifiers changed from: protected */
    public final Object zza(int i, Object obj, Object obj2) {
        boolean z;
        boolean z2 = true;
        switch (zzfil.zzbbi[i - 1]) {
            case 1:
                return new zzfik();
            case 2:
                return zzpkn;
            case 3:
                return null;
            case 4:
                return new zza(null);
            case 5:
                zzh zzh = (zzh) obj;
                zzfik zzfik = (zzfik) obj2;
                this.zzpkl = zzh.zza(this.zzpkl != 0, this.zzpkl, zzfik.zzpkl != 0, zzfik.zzpkl);
                boolean z3 = this.zzpkm != 0;
                int i2 = this.zzpkm;
                if (zzfik.zzpkm == 0) {
                    z2 = false;
                }
                this.zzpkm = zzh.zza(z3, i2, z2, zzfik.zzpkm);
                return this;
            case 6:
                zzffb zzffb = (zzffb) obj;
                if (((zzffm) obj2) != null) {
                    boolean z4 = false;
                    while (!z4) {
                        try {
                            int zzcvt = zzffb.zzcvt();
                            if (zzcvt != 0) {
                                if (zzcvt == 8) {
                                    this.zzpkl = zzffb.zzcvv();
                                } else if (zzcvt != 16) {
                                    if ((zzcvt & 7) == 4) {
                                        z = false;
                                    } else {
                                        if (this.zzpgr == zzfio.zzczu()) {
                                            this.zzpgr = zzfio.zzczv();
                                        }
                                        z = this.zzpgr.zzb(zzcvt, zzffb);
                                    }
                                    if (!z) {
                                    }
                                } else {
                                    this.zzpkm = zzffb.zzcvw();
                                }
                            }
                            z4 = true;
                        } catch (zzfge e) {
                            throw new RuntimeException(e.zzi(this));
                        } catch (IOException e2) {
                            throw new RuntimeException(new zzfge(e2.getMessage()).zzi(this));
                        }
                    }
                    break;
                } else {
                    throw new NullPointerException();
                }
            case 7:
                break;
            case 8:
                if (zzbbk == null) {
                    synchronized (zzfik.class) {
                        if (zzbbk == null) {
                            zzbbk = new zzb(zzpkn);
                        }
                    }
                }
                return zzbbk;
            case 9:
                return Byte.valueOf(1);
            case 10:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
        return zzpkn;
    }

    public final void zza(zzffg zzffg) throws IOException {
        if (this.zzpkl != 0) {
            zzffg.zza(1, this.zzpkl);
        }
        if (this.zzpkm != 0) {
            zzffg.zzaa(2, this.zzpkm);
        }
        this.zzpgr.zza(zzffg);
    }

    public final int zzho() {
        int i = this.zzpgs;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        if (this.zzpkl != 0) {
            i2 = 0 + zzffg.zzc(1, this.zzpkl);
        }
        if (this.zzpkm != 0) {
            i2 += zzffg.zzad(2, this.zzpkm);
        }
        int zzho = i2 + this.zzpgr.zzho();
        this.zzpgs = zzho;
        return zzho;
    }
}
