package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfku extends zzfjm<zzfku> {
    public long zzghq;
    public String zzpri;
    public String zzprj;
    public long zzprk;
    public String zzprl;
    public long zzprm;
    public String zzprn;
    public String zzpro;
    public String zzprp;
    public String zzprq;
    public String zzprr;
    public int zzprs;
    public zzfkt[] zzprt;

    public zzfku() {
        this.zzpri = "";
        this.zzprj = "";
        this.zzprk = 0;
        this.zzprl = "";
        this.zzprm = 0;
        this.zzghq = 0;
        this.zzprn = "";
        this.zzpro = "";
        this.zzprp = "";
        this.zzprq = "";
        this.zzprr = "";
        this.zzprs = 0;
        this.zzprt = zzfkt.zzdbd();
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzfku zzbi(byte[] bArr) throws zzfjr {
        return (zzfku) zzfjs.zza(new zzfku(), bArr);
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            switch (zzcvt) {
                case 0:
                    return this;
                case 10:
                    this.zzpri = zzfjj.readString();
                    break;
                case 18:
                    this.zzprj = zzfjj.readString();
                    break;
                case 24:
                    this.zzprk = zzfjj.zzcvv();
                    break;
                case 34:
                    this.zzprl = zzfjj.readString();
                    break;
                case 40:
                    this.zzprm = zzfjj.zzcvv();
                    break;
                case 48:
                    this.zzghq = zzfjj.zzcvv();
                    break;
                case 58:
                    this.zzprn = zzfjj.readString();
                    break;
                case 66:
                    this.zzpro = zzfjj.readString();
                    break;
                case 74:
                    this.zzprp = zzfjj.readString();
                    break;
                case 82:
                    this.zzprq = zzfjj.readString();
                    break;
                case 90:
                    this.zzprr = zzfjj.readString();
                    break;
                case 96:
                    this.zzprs = zzfjj.zzcvw();
                    break;
                case 106:
                    int zzb = zzfjv.zzb(zzfjj, 106);
                    int length = this.zzprt == null ? 0 : this.zzprt.length;
                    zzfkt[] zzfktArr = new zzfkt[(zzb + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzprt, 0, zzfktArr, 0, length);
                    }
                    while (length < zzfktArr.length - 1) {
                        zzfktArr[length] = new zzfkt();
                        zzfjj.zza(zzfktArr[length]);
                        zzfjj.zzcvt();
                        length++;
                    }
                    zzfktArr[length] = new zzfkt();
                    zzfjj.zza(zzfktArr[length]);
                    this.zzprt = zzfktArr;
                    break;
                default:
                    if (super.zza(zzfjj, zzcvt)) {
                        break;
                    } else {
                        return this;
                    }
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzpri != null && !this.zzpri.equals("")) {
            zzfjk.zzn(1, this.zzpri);
        }
        if (this.zzprj != null && !this.zzprj.equals("")) {
            zzfjk.zzn(2, this.zzprj);
        }
        if (this.zzprk != 0) {
            zzfjk.zzf(3, this.zzprk);
        }
        if (this.zzprl != null && !this.zzprl.equals("")) {
            zzfjk.zzn(4, this.zzprl);
        }
        if (this.zzprm != 0) {
            zzfjk.zzf(5, this.zzprm);
        }
        if (this.zzghq != 0) {
            zzfjk.zzf(6, this.zzghq);
        }
        if (this.zzprn != null && !this.zzprn.equals("")) {
            zzfjk.zzn(7, this.zzprn);
        }
        if (this.zzpro != null && !this.zzpro.equals("")) {
            zzfjk.zzn(8, this.zzpro);
        }
        if (this.zzprp != null && !this.zzprp.equals("")) {
            zzfjk.zzn(9, this.zzprp);
        }
        if (this.zzprq != null && !this.zzprq.equals("")) {
            zzfjk.zzn(10, this.zzprq);
        }
        if (this.zzprr != null && !this.zzprr.equals("")) {
            zzfjk.zzn(11, this.zzprr);
        }
        if (this.zzprs != 0) {
            zzfjk.zzaa(12, this.zzprs);
        }
        if (this.zzprt != null && this.zzprt.length > 0) {
            for (zzfkt zzfkt : this.zzprt) {
                if (zzfkt != null) {
                    zzfjk.zza(13, (zzfjs) zzfkt);
                }
            }
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzpri != null && !this.zzpri.equals("")) {
            zzq += zzfjk.zzo(1, this.zzpri);
        }
        if (this.zzprj != null && !this.zzprj.equals("")) {
            zzq += zzfjk.zzo(2, this.zzprj);
        }
        if (this.zzprk != 0) {
            zzq += zzfjk.zzc(3, this.zzprk);
        }
        if (this.zzprl != null && !this.zzprl.equals("")) {
            zzq += zzfjk.zzo(4, this.zzprl);
        }
        if (this.zzprm != 0) {
            zzq += zzfjk.zzc(5, this.zzprm);
        }
        if (this.zzghq != 0) {
            zzq += zzfjk.zzc(6, this.zzghq);
        }
        if (this.zzprn != null && !this.zzprn.equals("")) {
            zzq += zzfjk.zzo(7, this.zzprn);
        }
        if (this.zzpro != null && !this.zzpro.equals("")) {
            zzq += zzfjk.zzo(8, this.zzpro);
        }
        if (this.zzprp != null && !this.zzprp.equals("")) {
            zzq += zzfjk.zzo(9, this.zzprp);
        }
        if (this.zzprq != null && !this.zzprq.equals("")) {
            zzq += zzfjk.zzo(10, this.zzprq);
        }
        if (this.zzprr != null && !this.zzprr.equals("")) {
            zzq += zzfjk.zzo(11, this.zzprr);
        }
        if (this.zzprs != 0) {
            zzq += zzfjk.zzad(12, this.zzprs);
        }
        if (this.zzprt != null && this.zzprt.length > 0) {
            for (zzfkt zzfkt : this.zzprt) {
                if (zzfkt != null) {
                    zzq += zzfjk.zzb(13, (zzfjs) zzfkt);
                }
            }
        }
        return zzq;
    }
}
