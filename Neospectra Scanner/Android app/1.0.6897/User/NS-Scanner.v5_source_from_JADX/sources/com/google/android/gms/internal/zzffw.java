package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffu.zzb;
import com.google.android.gms.internal.zzffu.zzg;
import com.google.android.gms.internal.zzffu.zzh;
import java.io.IOException;

public final class zzffw extends zzffu<zzffw, zza> implements zzfhg {
    private static volatile zzfhk<zzffw> zzbbk;
    /* access modifiers changed from: private */
    public static final zzffw zzphr;
    private int zzphq;

    public static final class zza extends com.google.android.gms.internal.zzffu.zza<zzffw, zza> implements zzfhg {
        private zza() {
            super(zzffw.zzphr);
        }

        /* synthetic */ zza(zzffx zzffx) {
            this();
        }

        public final zza zzlt(int i) {
            zzcxr();
            ((zzffw) this.zzpgv).setValue(i);
            return this;
        }
    }

    static {
        zzffw zzffw = new zzffw();
        zzphr = zzffw;
        zzffw.zza(zzg.zzphh, (Object) null, (Object) null);
        zzffw.zzpgr.zzbiy();
    }

    private zzffw() {
    }

    /* access modifiers changed from: private */
    public final void setValue(int i) {
        this.zzphq = i;
    }

    public static zza zzcxw() {
        zzffw zzffw = zzphr;
        com.google.android.gms.internal.zzffu.zza zza2 = (com.google.android.gms.internal.zzffu.zza) zzffw.zza(zzg.zzphj, (Object) null, (Object) null);
        zza2.zza(zzffw);
        return (zza) zza2;
    }

    public static zzffw zzcxx() {
        return zzphr;
    }

    public final int getValue() {
        return this.zzphq;
    }

    /* access modifiers changed from: protected */
    public final Object zza(int i, Object obj, Object obj2) {
        boolean z;
        boolean z2 = true;
        switch (zzffx.zzbbi[i - 1]) {
            case 1:
                return new zzffw();
            case 2:
                return zzphr;
            case 3:
                return null;
            case 4:
                return new zza(null);
            case 5:
                zzh zzh = (zzh) obj;
                zzffw zzffw = (zzffw) obj2;
                boolean z3 = this.zzphq != 0;
                int i2 = this.zzphq;
                if (zzffw.zzphq == 0) {
                    z2 = false;
                }
                this.zzphq = zzh.zza(z3, i2, z2, zzffw.zzphq);
                return this;
            case 6:
                zzffb zzffb = (zzffb) obj;
                if (((zzffm) obj2) != null) {
                    boolean z4 = false;
                    while (!z4) {
                        try {
                            int zzcvt = zzffb.zzcvt();
                            if (zzcvt != 0) {
                                if (zzcvt != 8) {
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
                                    this.zzphq = zzffb.zzcvw();
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
                    synchronized (zzffw.class) {
                        if (zzbbk == null) {
                            zzbbk = new zzb(zzphr);
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
        return zzphr;
    }

    public final void zza(zzffg zzffg) throws IOException {
        if (this.zzphq != 0) {
            zzffg.zzaa(1, this.zzphq);
        }
        this.zzpgr.zza(zzffg);
    }

    public final int zzho() {
        int i = this.zzpgs;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        if (this.zzphq != 0) {
            i2 = 0 + zzffg.zzad(1, this.zzphq);
        }
        int zzho = i2 + this.zzpgr.zzho();
        this.zzpgs = zzho;
        return zzho;
    }
}
