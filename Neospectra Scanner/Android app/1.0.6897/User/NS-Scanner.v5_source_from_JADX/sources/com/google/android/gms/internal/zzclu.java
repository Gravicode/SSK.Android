package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclu extends zzfjm<zzclu> {
    public Integer zzjkg;
    public Boolean zzjkh;
    public String zzjki;
    public String zzjkj;
    public String zzjkk;

    public zzclu() {
        this.zzjkg = null;
        this.zzjkh = null;
        this.zzjki = null;
        this.zzjkj = null;
        this.zzjkk = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzh */
    public final zzclu zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 8) {
                try {
                    int zzcwi = zzfjj.zzcwi();
                    switch (zzcwi) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            this.zzjkg = Integer.valueOf(zzcwi);
                            break;
                        default:
                            StringBuilder sb = new StringBuilder(46);
                            sb.append(zzcwi);
                            sb.append(" is not a valid enum ComparisonType");
                            throw new IllegalArgumentException(sb.toString());
                    }
                } catch (IllegalArgumentException e) {
                    zzfjj.zzmg(zzfjj.getPosition());
                    zza(zzfjj, zzcvt);
                }
            } else if (zzcvt == 16) {
                this.zzjkh = Boolean.valueOf(zzfjj.zzcvz());
            } else if (zzcvt == 26) {
                this.zzjki = zzfjj.readString();
            } else if (zzcvt == 34) {
                this.zzjkj = zzfjj.readString();
            } else if (zzcvt == 42) {
                this.zzjkk = zzfjj.readString();
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclu)) {
            return false;
        }
        zzclu zzclu = (zzclu) obj;
        if (this.zzjkg == null) {
            if (zzclu.zzjkg != null) {
                return false;
            }
        } else if (!this.zzjkg.equals(zzclu.zzjkg)) {
            return false;
        }
        if (this.zzjkh == null) {
            if (zzclu.zzjkh != null) {
                return false;
            }
        } else if (!this.zzjkh.equals(zzclu.zzjkh)) {
            return false;
        }
        if (this.zzjki == null) {
            if (zzclu.zzjki != null) {
                return false;
            }
        } else if (!this.zzjki.equals(zzclu.zzjki)) {
            return false;
        }
        if (this.zzjkj == null) {
            if (zzclu.zzjkj != null) {
                return false;
            }
        } else if (!this.zzjkj.equals(zzclu.zzjkj)) {
            return false;
        }
        if (this.zzjkk == null) {
            if (zzclu.zzjkk != null) {
                return false;
            }
        } else if (!this.zzjkk.equals(zzclu.zzjkk)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclu.zzpnc == null || zzclu.zzpnc.isEmpty() : this.zzpnc.equals(zzclu.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzjkg == null ? 0 : this.zzjkg.intValue())) * 31) + (this.zzjkh == null ? 0 : this.zzjkh.hashCode())) * 31) + (this.zzjki == null ? 0 : this.zzjki.hashCode())) * 31) + (this.zzjkj == null ? 0 : this.zzjkj.hashCode())) * 31) + (this.zzjkk == null ? 0 : this.zzjkk.hashCode())) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjkg != null) {
            zzfjk.zzaa(1, this.zzjkg.intValue());
        }
        if (this.zzjkh != null) {
            zzfjk.zzl(2, this.zzjkh.booleanValue());
        }
        if (this.zzjki != null) {
            zzfjk.zzn(3, this.zzjki);
        }
        if (this.zzjkj != null) {
            zzfjk.zzn(4, this.zzjkj);
        }
        if (this.zzjkk != null) {
            zzfjk.zzn(5, this.zzjkk);
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjkg != null) {
            zzq += zzfjk.zzad(1, this.zzjkg.intValue());
        }
        if (this.zzjkh != null) {
            this.zzjkh.booleanValue();
            zzq += zzfjk.zzlg(2) + 1;
        }
        if (this.zzjki != null) {
            zzq += zzfjk.zzo(3, this.zzjki);
        }
        if (this.zzjkj != null) {
            zzq += zzfjk.zzo(4, this.zzjkj);
        }
        return this.zzjkk != null ? zzq + zzfjk.zzo(5, this.zzjkk) : zzq;
    }
}
