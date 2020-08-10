package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfks extends zzfjm<zzfks> implements Cloneable {
    private int zzprf;
    private int zzprg;

    public zzfks() {
        this.zzprf = -1;
        this.zzprg = 0;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzas */
    public final zzfks zza(zzfjj zzfjj) throws IOException {
        int i;
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 8) {
                i = zzfjj.getPosition();
                int zzcvw = zzfjj.zzcvw();
                switch (zzcvw) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        this.zzprf = zzcvw;
                        break;
                    default:
                        StringBuilder sb = new StringBuilder(43);
                        sb.append(zzcvw);
                        sb.append(" is not a valid enum NetworkType");
                        throw new IllegalArgumentException(sb.toString());
                }
            } else if (zzcvt == 16) {
                i = zzfjj.getPosition();
                try {
                    int zzcvw2 = zzfjj.zzcvw();
                    if (zzcvw2 != 100) {
                        switch (zzcvw2) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                                break;
                            default:
                                StringBuilder sb2 = new StringBuilder(45);
                                sb2.append(zzcvw2);
                                sb2.append(" is not a valid enum MobileSubtype");
                                throw new IllegalArgumentException(sb2.toString());
                        }
                    }
                    this.zzprg = zzcvw2;
                } catch (IllegalArgumentException e) {
                    zzfjj.zzmg(i);
                    zza(zzfjj, zzcvt);
                }
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: zzdbc */
    public zzfks clone() {
        try {
            return (zzfks) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfks)) {
            return false;
        }
        zzfks zzfks = (zzfks) obj;
        if (this.zzprf == zzfks.zzprf && this.zzprg == zzfks.zzprg) {
            return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzfks.zzpnc == null || zzfks.zzpnc.isEmpty() : this.zzpnc.equals(zzfks.zzpnc);
        }
        return false;
    }

    public final int hashCode() {
        return ((((((getClass().getName().hashCode() + 527) * 31) + this.zzprf) * 31) + this.zzprg) * 31) + ((this.zzpnc == null || this.zzpnc.isEmpty()) ? 0 : this.zzpnc.hashCode());
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzprf != -1) {
            zzfjk.zzaa(1, this.zzprf);
        }
        if (this.zzprg != 0) {
            zzfjk.zzaa(2, this.zzprg);
        }
        super.zza(zzfjk);
    }

    public final /* synthetic */ zzfjm zzdaf() throws CloneNotSupportedException {
        return (zzfks) clone();
    }

    public final /* synthetic */ zzfjs zzdag() throws CloneNotSupportedException {
        return (zzfks) clone();
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzprf != -1) {
            zzq += zzfjk.zzad(1, this.zzprf);
        }
        return this.zzprg != 0 ? zzq + zzfjk.zzad(2, this.zzprg) : zzq;
    }
}
