package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffu.zzb;
import com.google.android.gms.internal.zzffu.zzf;
import com.google.android.gms.internal.zzffu.zzg;
import com.google.android.gms.internal.zzffu.zzh;
import java.io.IOException;

public final class zzfkg extends zzffu<zzfkg, zza> implements zzfhg {
    private static volatile zzfhk<zzfkg> zzbbk;
    /* access modifiers changed from: private */
    public static final zzfkg zzppn;
    private int zzlwy;
    private int zzppk;
    private String zzppl = "";
    private zzfgd<zzfep> zzppm = zzcxo();

    public static final class zza extends com.google.android.gms.internal.zzffu.zza<zzfkg, zza> implements zzfhg {
        private zza() {
            super(zzfkg.zzppn);
        }

        /* synthetic */ zza(zzfkh zzfkh) {
            this();
        }
    }

    static {
        zzfkg zzfkg = new zzfkg();
        zzppn = zzfkg;
        zzfkg.zza(zzg.zzphh, (Object) null, (Object) null);
        zzfkg.zzpgr.zzbiy();
    }

    private zzfkg() {
    }

    public static zzfkg zzdap() {
        return zzppn;
    }

    public final int getCode() {
        return this.zzppk;
    }

    public final String getMessage() {
        return this.zzppl;
    }

    /* access modifiers changed from: protected */
    public final Object zza(int i, Object obj, Object obj2) {
        boolean z = false;
        switch (zzfkh.zzbbi[i - 1]) {
            case 1:
                return new zzfkg();
            case 2:
                return zzppn;
            case 3:
                this.zzppm.zzbiy();
                return null;
            case 4:
                return new zza(null);
            case 5:
                zzh zzh = (zzh) obj;
                zzfkg zzfkg = (zzfkg) obj2;
                boolean z2 = this.zzppk != 0;
                int i2 = this.zzppk;
                if (zzfkg.zzppk != 0) {
                    z = true;
                }
                this.zzppk = zzh.zza(z2, i2, z, zzfkg.zzppk);
                this.zzppl = zzh.zza(!this.zzppl.isEmpty(), this.zzppl, true ^ zzfkg.zzppl.isEmpty(), zzfkg.zzppl);
                this.zzppm = zzh.zza(this.zzppm, zzfkg.zzppm);
                if (zzh == zzf.zzphb) {
                    this.zzlwy |= zzfkg.zzlwy;
                }
                return this;
            case 6:
                zzffb zzffb = (zzffb) obj;
                zzffm zzffm = (zzffm) obj2;
                if (zzffm != null) {
                    while (!z) {
                        try {
                            int zzcvt = zzffb.zzcvt();
                            if (zzcvt != 0) {
                                if (zzcvt == 8) {
                                    this.zzppk = zzffb.zzcvw();
                                } else if (zzcvt == 18) {
                                    this.zzppl = zzffb.zzcwa();
                                } else if (zzcvt == 26) {
                                    if (!this.zzppm.zzcvi()) {
                                        zzfgd<zzfep> zzfgd = this.zzppm;
                                        int size = zzfgd.size();
                                        this.zzppm = zzfgd.zzly(size == 0 ? 10 : size << 1);
                                    }
                                    this.zzppm.add((zzfep) zzffb.zza(zzfep.zzcvk(), zzffm));
                                } else if (!zza(zzcvt, zzffb)) {
                                }
                            }
                            z = true;
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
                    synchronized (zzfkg.class) {
                        if (zzbbk == null) {
                            zzbbk = new zzb(zzppn);
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
        return zzppn;
    }

    public final void zza(zzffg zzffg) throws IOException {
        if (this.zzppk != 0) {
            zzffg.zzaa(1, this.zzppk);
        }
        if (!this.zzppl.isEmpty()) {
            zzffg.zzn(2, this.zzppl);
        }
        for (int i = 0; i < this.zzppm.size(); i++) {
            zzffg.zza(3, (zzfhe) this.zzppm.get(i));
        }
        this.zzpgr.zza(zzffg);
    }

    public final int zzho() {
        int i = this.zzpgs;
        if (i != -1) {
            return i;
        }
        int zzad = this.zzppk != 0 ? zzffg.zzad(1, this.zzppk) + 0 : 0;
        if (!this.zzppl.isEmpty()) {
            zzad += zzffg.zzo(2, this.zzppl);
        }
        for (int i2 = 0; i2 < this.zzppm.size(); i2++) {
            zzad += zzffg.zzc(3, (zzfhe) this.zzppm.get(i2));
        }
        int zzho = zzad + this.zzpgr.zzho();
        this.zzpgs = zzho;
        return zzho;
    }
}
