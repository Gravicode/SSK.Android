package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclw extends zzfjm<zzclw> {
    public Integer zzjko;
    public String zzjkp;
    public Boolean zzjkq;
    public String[] zzjkr;

    public zzclw() {
        this.zzjko = null;
        this.zzjkp = null;
        this.zzjkq = null;
        this.zzjkr = zzfjv.EMPTY_STRING_ARRAY;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzi */
    public final zzclw zza(zzfjj zzfjj) throws IOException {
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
                        case 5:
                        case 6:
                            this.zzjko = Integer.valueOf(zzcwi);
                            break;
                        default:
                            StringBuilder sb = new StringBuilder(41);
                            sb.append(zzcwi);
                            sb.append(" is not a valid enum MatchType");
                            throw new IllegalArgumentException(sb.toString());
                    }
                } catch (IllegalArgumentException e) {
                    zzfjj.zzmg(zzfjj.getPosition());
                    zza(zzfjj, zzcvt);
                }
            } else if (zzcvt == 18) {
                this.zzjkp = zzfjj.readString();
            } else if (zzcvt == 24) {
                this.zzjkq = Boolean.valueOf(zzfjj.zzcvz());
            } else if (zzcvt == 34) {
                int zzb = zzfjv.zzb(zzfjj, 34);
                int length = this.zzjkr == null ? 0 : this.zzjkr.length;
                String[] strArr = new String[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzjkr, 0, strArr, 0, length);
                }
                while (length < strArr.length - 1) {
                    strArr[length] = zzfjj.readString();
                    zzfjj.zzcvt();
                    length++;
                }
                strArr[length] = zzfjj.readString();
                this.zzjkr = strArr;
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclw)) {
            return false;
        }
        zzclw zzclw = (zzclw) obj;
        if (this.zzjko == null) {
            if (zzclw.zzjko != null) {
                return false;
            }
        } else if (!this.zzjko.equals(zzclw.zzjko)) {
            return false;
        }
        if (this.zzjkp == null) {
            if (zzclw.zzjkp != null) {
                return false;
            }
        } else if (!this.zzjkp.equals(zzclw.zzjkp)) {
            return false;
        }
        if (this.zzjkq == null) {
            if (zzclw.zzjkq != null) {
                return false;
            }
        } else if (!this.zzjkq.equals(zzclw.zzjkq)) {
            return false;
        }
        if (!zzfjq.equals((Object[]) this.zzjkr, (Object[]) zzclw.zzjkr)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclw.zzpnc == null || zzclw.zzpnc.isEmpty() : this.zzpnc.equals(zzclw.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzjko == null ? 0 : this.zzjko.intValue())) * 31) + (this.zzjkp == null ? 0 : this.zzjkp.hashCode())) * 31) + (this.zzjkq == null ? 0 : this.zzjkq.hashCode())) * 31) + zzfjq.hashCode((Object[]) this.zzjkr)) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjko != null) {
            zzfjk.zzaa(1, this.zzjko.intValue());
        }
        if (this.zzjkp != null) {
            zzfjk.zzn(2, this.zzjkp);
        }
        if (this.zzjkq != null) {
            zzfjk.zzl(3, this.zzjkq.booleanValue());
        }
        if (this.zzjkr != null && this.zzjkr.length > 0) {
            for (String str : this.zzjkr) {
                if (str != null) {
                    zzfjk.zzn(4, str);
                }
            }
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjko != null) {
            zzq += zzfjk.zzad(1, this.zzjko.intValue());
        }
        if (this.zzjkp != null) {
            zzq += zzfjk.zzo(2, this.zzjkp);
        }
        if (this.zzjkq != null) {
            this.zzjkq.booleanValue();
            zzq += zzfjk.zzlg(3) + 1;
        }
        if (this.zzjkr == null || this.zzjkr.length <= 0) {
            return zzq;
        }
        int i = 0;
        int i2 = 0;
        for (String str : this.zzjkr) {
            if (str != null) {
                i2++;
                i += zzfjk.zztt(str);
            }
        }
        return zzq + i + (i2 * 1);
    }
}
