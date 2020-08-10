package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public final class zzfkq extends zzfjm<zzfkq> implements Cloneable {
    private String tag;
    private int zzala;
    private boolean zzmsn;
    private zzfks zzofi;
    public long zzpql;
    public long zzpqm;
    private long zzpqn;
    private int zzpqo;
    private zzfkr[] zzpqp;
    private byte[] zzpqq;
    private zzfko zzpqr;
    public byte[] zzpqs;
    private String zzpqt;
    private String zzpqu;
    private zzfkn zzpqv;
    private String zzpqw;
    public long zzpqx;
    private zzfkp zzpqy;
    public byte[] zzpqz;
    private String zzpra;
    private int zzprb;
    private int[] zzprc;
    private long zzprd;

    public zzfkq() {
        this.zzpql = 0;
        this.zzpqm = 0;
        this.zzpqn = 0;
        this.tag = "";
        this.zzpqo = 0;
        this.zzala = 0;
        this.zzmsn = false;
        this.zzpqp = zzfkr.zzdba();
        this.zzpqq = zzfjv.zzpnv;
        this.zzpqr = null;
        this.zzpqs = zzfjv.zzpnv;
        this.zzpqt = "";
        this.zzpqu = "";
        this.zzpqv = null;
        this.zzpqw = "";
        this.zzpqx = 180000;
        this.zzpqy = null;
        this.zzpqz = zzfjv.zzpnv;
        this.zzpra = "";
        this.zzprb = 0;
        this.zzprc = zzfjv.zzpnp;
        this.zzprd = 0;
        this.zzofi = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzar */
    public final zzfkq zza(zzfjj zzfjj) throws IOException {
        zzfjs zzfjs;
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            switch (zzcvt) {
                case 0:
                    return this;
                case 8:
                    this.zzpql = zzfjj.zzcvv();
                    continue;
                case 18:
                    this.tag = zzfjj.readString();
                    continue;
                case 26:
                    int zzb = zzfjv.zzb(zzfjj, 26);
                    int length = this.zzpqp == null ? 0 : this.zzpqp.length;
                    zzfkr[] zzfkrArr = new zzfkr[(zzb + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzpqp, 0, zzfkrArr, 0, length);
                    }
                    while (length < zzfkrArr.length - 1) {
                        zzfkrArr[length] = new zzfkr();
                        zzfjj.zza(zzfkrArr[length]);
                        zzfjj.zzcvt();
                        length++;
                    }
                    zzfkrArr[length] = new zzfkr();
                    zzfjj.zza(zzfkrArr[length]);
                    this.zzpqp = zzfkrArr;
                    continue;
                case 34:
                    this.zzpqq = zzfjj.readBytes();
                    continue;
                case 50:
                    this.zzpqs = zzfjj.readBytes();
                    continue;
                case 58:
                    if (this.zzpqv == null) {
                        this.zzpqv = new zzfkn();
                    }
                    zzfjs = this.zzpqv;
                    break;
                case 66:
                    this.zzpqt = zzfjj.readString();
                    continue;
                case 74:
                    if (this.zzpqr == null) {
                        this.zzpqr = new zzfko();
                    }
                    zzfjs = this.zzpqr;
                    break;
                case 80:
                    this.zzmsn = zzfjj.zzcvz();
                    continue;
                case 88:
                    this.zzpqo = zzfjj.zzcvw();
                    continue;
                case 96:
                    this.zzala = zzfjj.zzcvw();
                    continue;
                case 106:
                    this.zzpqu = zzfjj.readString();
                    continue;
                case 114:
                    this.zzpqw = zzfjj.readString();
                    continue;
                case ShapeTypes.CLOUD_CALLOUT /*120*/:
                    this.zzpqx = zzfjj.zzcwh();
                    continue;
                case 130:
                    if (this.zzpqy == null) {
                        this.zzpqy = new zzfkp();
                    }
                    zzfjs = this.zzpqy;
                    break;
                case ShapeTypes.FLOW_CHART_INTERNAL_STORAGE /*136*/:
                    this.zzpqm = zzfjj.zzcvv();
                    continue;
                case ShapeTypes.FLOW_CHART_SUMMING_JUNCTION /*146*/:
                    this.zzpqz = zzfjj.readBytes();
                    continue;
                case ShapeTypes.FLOW_CHART_OFFLINE_STORAGE /*152*/:
                    try {
                        int zzcvw = zzfjj.zzcvw();
                        switch (zzcvw) {
                            case 0:
                            case 1:
                            case 2:
                                this.zzprb = zzcvw;
                                continue;
                            default:
                                StringBuilder sb = new StringBuilder(45);
                                sb.append(zzcvw);
                                sb.append(" is not a valid enum InternalEvent");
                                throw new IllegalArgumentException(sb.toString());
                        }
                    } catch (IllegalArgumentException e) {
                        zzfjj.zzmg(zzfjj.getPosition());
                        zza(zzfjj, zzcvt);
                        break;
                    }
                case 160:
                    int zzb2 = zzfjv.zzb(zzfjj, 160);
                    int length2 = this.zzprc == null ? 0 : this.zzprc.length;
                    int[] iArr = new int[(zzb2 + length2)];
                    if (length2 != 0) {
                        System.arraycopy(this.zzprc, 0, iArr, 0, length2);
                    }
                    while (length2 < iArr.length - 1) {
                        iArr[length2] = zzfjj.zzcvw();
                        zzfjj.zzcvt();
                        length2++;
                    }
                    iArr[length2] = zzfjj.zzcvw();
                    this.zzprc = iArr;
                    continue;
                case ShapeTypes.ACTION_BUTTON_HOME /*162*/:
                    int zzks = zzfjj.zzks(zzfjj.zzcwi());
                    int position = zzfjj.getPosition();
                    int i = 0;
                    while (zzfjj.zzcwk() > 0) {
                        zzfjj.zzcvw();
                        i++;
                    }
                    zzfjj.zzmg(position);
                    int length3 = this.zzprc == null ? 0 : this.zzprc.length;
                    int[] iArr2 = new int[(i + length3)];
                    if (length3 != 0) {
                        System.arraycopy(this.zzprc, 0, iArr2, 0, length3);
                    }
                    while (length3 < iArr2.length) {
                        iArr2[length3] = zzfjj.zzcvw();
                        length3++;
                    }
                    this.zzprc = iArr2;
                    zzfjj.zzkt(zzks);
                    continue;
                case ShapeTypes.ACTION_BUTTON_BEGINNING /*168*/:
                    this.zzpqn = zzfjj.zzcvv();
                    continue;
                case ShapeTypes.MATH_PLUS /*176*/:
                    this.zzprd = zzfjj.zzcvv();
                    continue;
                case ShapeTypes.CHART_STAR /*186*/:
                    if (this.zzofi == null) {
                        this.zzofi = new zzfks();
                    }
                    zzfjs = this.zzofi;
                    break;
                case 194:
                    this.zzpra = zzfjj.readString();
                    continue;
                default:
                    if (!super.zza(zzfjj, zzcvt)) {
                        return this;
                    }
                    continue;
            }
            zzfjj.zza(zzfjs);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: zzdaz */
    public final zzfkq clone() {
        try {
            zzfkq zzfkq = (zzfkq) super.clone();
            if (this.zzpqp != null && this.zzpqp.length > 0) {
                zzfkq.zzpqp = new zzfkr[this.zzpqp.length];
                for (int i = 0; i < this.zzpqp.length; i++) {
                    if (this.zzpqp[i] != null) {
                        zzfkq.zzpqp[i] = (zzfkr) this.zzpqp[i].clone();
                    }
                }
            }
            if (this.zzpqr != null) {
                zzfkq.zzpqr = (zzfko) this.zzpqr.clone();
            }
            if (this.zzpqv != null) {
                zzfkq.zzpqv = (zzfkn) this.zzpqv.clone();
            }
            if (this.zzpqy != null) {
                zzfkq.zzpqy = (zzfkp) this.zzpqy.clone();
            }
            if (this.zzprc != null && this.zzprc.length > 0) {
                zzfkq.zzprc = (int[]) this.zzprc.clone();
            }
            if (this.zzofi != null) {
                zzfkq.zzofi = (zzfks) this.zzofi.clone();
            }
            return zzfkq;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfkq)) {
            return false;
        }
        zzfkq zzfkq = (zzfkq) obj;
        if (this.zzpql != zzfkq.zzpql || this.zzpqm != zzfkq.zzpqm || this.zzpqn != zzfkq.zzpqn) {
            return false;
        }
        if (this.tag == null) {
            if (zzfkq.tag != null) {
                return false;
            }
        } else if (!this.tag.equals(zzfkq.tag)) {
            return false;
        }
        if (this.zzpqo != zzfkq.zzpqo || this.zzala != zzfkq.zzala || this.zzmsn != zzfkq.zzmsn || !zzfjq.equals((Object[]) this.zzpqp, (Object[]) zzfkq.zzpqp) || !Arrays.equals(this.zzpqq, zzfkq.zzpqq)) {
            return false;
        }
        if (this.zzpqr == null) {
            if (zzfkq.zzpqr != null) {
                return false;
            }
        } else if (!this.zzpqr.equals(zzfkq.zzpqr)) {
            return false;
        }
        if (!Arrays.equals(this.zzpqs, zzfkq.zzpqs)) {
            return false;
        }
        if (this.zzpqt == null) {
            if (zzfkq.zzpqt != null) {
                return false;
            }
        } else if (!this.zzpqt.equals(zzfkq.zzpqt)) {
            return false;
        }
        if (this.zzpqu == null) {
            if (zzfkq.zzpqu != null) {
                return false;
            }
        } else if (!this.zzpqu.equals(zzfkq.zzpqu)) {
            return false;
        }
        if (this.zzpqv == null) {
            if (zzfkq.zzpqv != null) {
                return false;
            }
        } else if (!this.zzpqv.equals(zzfkq.zzpqv)) {
            return false;
        }
        if (this.zzpqw == null) {
            if (zzfkq.zzpqw != null) {
                return false;
            }
        } else if (!this.zzpqw.equals(zzfkq.zzpqw)) {
            return false;
        }
        if (this.zzpqx != zzfkq.zzpqx) {
            return false;
        }
        if (this.zzpqy == null) {
            if (zzfkq.zzpqy != null) {
                return false;
            }
        } else if (!this.zzpqy.equals(zzfkq.zzpqy)) {
            return false;
        }
        if (!Arrays.equals(this.zzpqz, zzfkq.zzpqz)) {
            return false;
        }
        if (this.zzpra == null) {
            if (zzfkq.zzpra != null) {
                return false;
            }
        } else if (!this.zzpra.equals(zzfkq.zzpra)) {
            return false;
        }
        if (this.zzprb != zzfkq.zzprb || !zzfjq.equals(this.zzprc, zzfkq.zzprc) || this.zzprd != zzfkq.zzprd) {
            return false;
        }
        if (this.zzofi == null) {
            if (zzfkq.zzofi != null) {
                return false;
            }
        } else if (!this.zzofi.equals(zzfkq.zzofi)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzfkq.zzpnc == null || zzfkq.zzpnc.isEmpty() : this.zzpnc.equals(zzfkq.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((((((((((((getClass().getName().hashCode() + 527) * 31) + ((int) (this.zzpql ^ (this.zzpql >>> 32)))) * 31) + ((int) (this.zzpqm ^ (this.zzpqm >>> 32)))) * 31) + ((int) (this.zzpqn ^ (this.zzpqn >>> 32)))) * 31) + (this.tag == null ? 0 : this.tag.hashCode())) * 31) + this.zzpqo) * 31) + this.zzala) * 31) + (this.zzmsn ? 1231 : 1237)) * 31) + zzfjq.hashCode((Object[]) this.zzpqp)) * 31) + Arrays.hashCode(this.zzpqq);
        zzfko zzfko = this.zzpqr;
        int hashCode2 = (((((((hashCode * 31) + (zzfko == null ? 0 : zzfko.hashCode())) * 31) + Arrays.hashCode(this.zzpqs)) * 31) + (this.zzpqt == null ? 0 : this.zzpqt.hashCode())) * 31) + (this.zzpqu == null ? 0 : this.zzpqu.hashCode());
        zzfkn zzfkn = this.zzpqv;
        int hashCode3 = (((((hashCode2 * 31) + (zzfkn == null ? 0 : zzfkn.hashCode())) * 31) + (this.zzpqw == null ? 0 : this.zzpqw.hashCode())) * 31) + ((int) (this.zzpqx ^ (this.zzpqx >>> 32)));
        zzfkp zzfkp = this.zzpqy;
        int hashCode4 = (((((((((((hashCode3 * 31) + (zzfkp == null ? 0 : zzfkp.hashCode())) * 31) + Arrays.hashCode(this.zzpqz)) * 31) + (this.zzpra == null ? 0 : this.zzpra.hashCode())) * 31) + this.zzprb) * 31) + zzfjq.hashCode(this.zzprc)) * 31) + ((int) (this.zzprd ^ (this.zzprd >>> 32)));
        zzfks zzfks = this.zzofi;
        int hashCode5 = ((hashCode4 * 31) + (zzfks == null ? 0 : zzfks.hashCode())) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode5 + i;
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzpql != 0) {
            zzfjk.zzf(1, this.zzpql);
        }
        if (this.tag != null && !this.tag.equals("")) {
            zzfjk.zzn(2, this.tag);
        }
        if (this.zzpqp != null && this.zzpqp.length > 0) {
            for (zzfkr zzfkr : this.zzpqp) {
                if (zzfkr != null) {
                    zzfjk.zza(3, (zzfjs) zzfkr);
                }
            }
        }
        if (!Arrays.equals(this.zzpqq, zzfjv.zzpnv)) {
            zzfjk.zzc(4, this.zzpqq);
        }
        if (!Arrays.equals(this.zzpqs, zzfjv.zzpnv)) {
            zzfjk.zzc(6, this.zzpqs);
        }
        if (this.zzpqv != null) {
            zzfjk.zza(7, (zzfjs) this.zzpqv);
        }
        if (this.zzpqt != null && !this.zzpqt.equals("")) {
            zzfjk.zzn(8, this.zzpqt);
        }
        if (this.zzpqr != null) {
            zzfjk.zza(9, (zzfjs) this.zzpqr);
        }
        if (this.zzmsn) {
            zzfjk.zzl(10, this.zzmsn);
        }
        if (this.zzpqo != 0) {
            zzfjk.zzaa(11, this.zzpqo);
        }
        if (this.zzala != 0) {
            zzfjk.zzaa(12, this.zzala);
        }
        if (this.zzpqu != null && !this.zzpqu.equals("")) {
            zzfjk.zzn(13, this.zzpqu);
        }
        if (this.zzpqw != null && !this.zzpqw.equals("")) {
            zzfjk.zzn(14, this.zzpqw);
        }
        if (this.zzpqx != 180000) {
            zzfjk.zzg(15, this.zzpqx);
        }
        if (this.zzpqy != null) {
            zzfjk.zza(16, (zzfjs) this.zzpqy);
        }
        if (this.zzpqm != 0) {
            zzfjk.zzf(17, this.zzpqm);
        }
        if (!Arrays.equals(this.zzpqz, zzfjv.zzpnv)) {
            zzfjk.zzc(18, this.zzpqz);
        }
        if (this.zzprb != 0) {
            zzfjk.zzaa(19, this.zzprb);
        }
        if (this.zzprc != null && this.zzprc.length > 0) {
            for (int zzaa : this.zzprc) {
                zzfjk.zzaa(20, zzaa);
            }
        }
        if (this.zzpqn != 0) {
            zzfjk.zzf(21, this.zzpqn);
        }
        if (this.zzprd != 0) {
            zzfjk.zzf(22, this.zzprd);
        }
        if (this.zzofi != null) {
            zzfjk.zza(23, (zzfjs) this.zzofi);
        }
        if (this.zzpra != null && !this.zzpra.equals("")) {
            zzfjk.zzn(24, this.zzpra);
        }
        super.zza(zzfjk);
    }

    public final /* synthetic */ zzfjm zzdaf() throws CloneNotSupportedException {
        return (zzfkq) clone();
    }

    public final /* synthetic */ zzfjs zzdag() throws CloneNotSupportedException {
        return (zzfkq) clone();
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzpql != 0) {
            zzq += zzfjk.zzc(1, this.zzpql);
        }
        if (this.tag != null && !this.tag.equals("")) {
            zzq += zzfjk.zzo(2, this.tag);
        }
        if (this.zzpqp != null && this.zzpqp.length > 0) {
            int i = zzq;
            for (zzfkr zzfkr : this.zzpqp) {
                if (zzfkr != null) {
                    i += zzfjk.zzb(3, (zzfjs) zzfkr);
                }
            }
            zzq = i;
        }
        if (!Arrays.equals(this.zzpqq, zzfjv.zzpnv)) {
            zzq += zzfjk.zzd(4, this.zzpqq);
        }
        if (!Arrays.equals(this.zzpqs, zzfjv.zzpnv)) {
            zzq += zzfjk.zzd(6, this.zzpqs);
        }
        if (this.zzpqv != null) {
            zzq += zzfjk.zzb(7, (zzfjs) this.zzpqv);
        }
        if (this.zzpqt != null && !this.zzpqt.equals("")) {
            zzq += zzfjk.zzo(8, this.zzpqt);
        }
        if (this.zzpqr != null) {
            zzq += zzfjk.zzb(9, (zzfjs) this.zzpqr);
        }
        if (this.zzmsn) {
            zzq += zzfjk.zzlg(10) + 1;
        }
        if (this.zzpqo != 0) {
            zzq += zzfjk.zzad(11, this.zzpqo);
        }
        if (this.zzala != 0) {
            zzq += zzfjk.zzad(12, this.zzala);
        }
        if (this.zzpqu != null && !this.zzpqu.equals("")) {
            zzq += zzfjk.zzo(13, this.zzpqu);
        }
        if (this.zzpqw != null && !this.zzpqw.equals("")) {
            zzq += zzfjk.zzo(14, this.zzpqw);
        }
        if (this.zzpqx != 180000) {
            zzq += zzfjk.zzh(15, this.zzpqx);
        }
        if (this.zzpqy != null) {
            zzq += zzfjk.zzb(16, (zzfjs) this.zzpqy);
        }
        if (this.zzpqm != 0) {
            zzq += zzfjk.zzc(17, this.zzpqm);
        }
        if (!Arrays.equals(this.zzpqz, zzfjv.zzpnv)) {
            zzq += zzfjk.zzd(18, this.zzpqz);
        }
        if (this.zzprb != 0) {
            zzq += zzfjk.zzad(19, this.zzprb);
        }
        if (this.zzprc != null && this.zzprc.length > 0) {
            int i2 = 0;
            for (int zzlh : this.zzprc) {
                i2 += zzfjk.zzlh(zzlh);
            }
            zzq = zzq + i2 + (this.zzprc.length * 2);
        }
        if (this.zzpqn != 0) {
            zzq += zzfjk.zzc(21, this.zzpqn);
        }
        if (this.zzprd != 0) {
            zzq += zzfjk.zzc(22, this.zzprd);
        }
        if (this.zzofi != null) {
            zzq += zzfjk.zzb(23, (zzfjs) this.zzofi);
        }
        return (this.zzpra == null || this.zzpra.equals("")) ? zzq : zzq + zzfjk.zzo(24, this.zzpra);
    }
}
