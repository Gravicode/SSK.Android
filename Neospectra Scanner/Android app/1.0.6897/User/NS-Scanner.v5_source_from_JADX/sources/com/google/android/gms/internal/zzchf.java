package com.google.android.gms.internal;

public abstract class zzchf extends zzev implements zzche {
    public zzchf() {
        attachInterface(this, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00e3, code lost:
        r10.writeNoException();
        r10.writeTypedList(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00e9, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x013d, code lost:
        r10.writeNoException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0140, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTransact(int r8, android.os.Parcel r9, android.os.Parcel r10, int r11) throws android.os.RemoteException {
        /*
            r7 = this;
            boolean r11 = r7.zza(r8, r9, r10, r11)
            r0 = 1
            if (r11 == 0) goto L_0x0008
            return r0
        L_0x0008:
            switch(r8) {
                case 1: goto L_0x012a;
                case 2: goto L_0x0116;
                case 3: goto L_0x000b;
                case 4: goto L_0x010a;
                case 5: goto L_0x00f6;
                case 6: goto L_0x00ea;
                case 7: goto L_0x00d3;
                case 8: goto L_0x000b;
                case 9: goto L_0x00bc;
                case 10: goto L_0x00a6;
                case 11: goto L_0x0093;
                case 12: goto L_0x007e;
                case 13: goto L_0x0071;
                case 14: goto L_0x0058;
                case 15: goto L_0x0042;
                case 16: goto L_0x002c;
                case 17: goto L_0x001a;
                case 18: goto L_0x000d;
                default: goto L_0x000b;
            }
        L_0x000b:
            r8 = 0
            return r8
        L_0x000d:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r8 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgi r8 = (com.google.android.gms.internal.zzcgi) r8
            r7.zzd(r8)
            goto L_0x013d
        L_0x001a:
            java.lang.String r8 = r9.readString()
            java.lang.String r11 = r9.readString()
            java.lang.String r9 = r9.readString()
            java.util.List r8 = r7.zzj(r8, r11, r9)
            goto L_0x00e3
        L_0x002c:
            java.lang.String r8 = r9.readString()
            java.lang.String r11 = r9.readString()
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r1 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r9 = com.google.android.gms.internal.zzew.zza(r9, r1)
            com.google.android.gms.internal.zzcgi r9 = (com.google.android.gms.internal.zzcgi) r9
            java.util.List r8 = r7.zza(r8, r11, r9)
            goto L_0x00e3
        L_0x0042:
            java.lang.String r8 = r9.readString()
            java.lang.String r11 = r9.readString()
            java.lang.String r1 = r9.readString()
            boolean r9 = com.google.android.gms.internal.zzew.zza(r9)
            java.util.List r8 = r7.zza(r8, r11, r1, r9)
            goto L_0x00e3
        L_0x0058:
            java.lang.String r8 = r9.readString()
            java.lang.String r11 = r9.readString()
            boolean r1 = com.google.android.gms.internal.zzew.zza(r9)
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r2 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r9 = com.google.android.gms.internal.zzew.zza(r9, r2)
            com.google.android.gms.internal.zzcgi r9 = (com.google.android.gms.internal.zzcgi) r9
            java.util.List r8 = r7.zza(r8, r11, r1, r9)
            goto L_0x00e3
        L_0x0071:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgl> r8 = com.google.android.gms.internal.zzcgl.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgl r8 = (com.google.android.gms.internal.zzcgl) r8
            r7.zzb(r8)
            goto L_0x013d
        L_0x007e:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgl> r8 = com.google.android.gms.internal.zzcgl.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgl r8 = (com.google.android.gms.internal.zzcgl) r8
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r11 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r9 = com.google.android.gms.internal.zzew.zza(r9, r11)
            com.google.android.gms.internal.zzcgi r9 = (com.google.android.gms.internal.zzcgi) r9
            r7.zza(r8, r9)
            goto L_0x013d
        L_0x0093:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r8 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgi r8 = (com.google.android.gms.internal.zzcgi) r8
            java.lang.String r8 = r7.zzc(r8)
            r10.writeNoException()
            r10.writeString(r8)
            return r0
        L_0x00a6:
            long r2 = r9.readLong()
            java.lang.String r4 = r9.readString()
            java.lang.String r5 = r9.readString()
            java.lang.String r6 = r9.readString()
            r1 = r7
            r1.zza(r2, r4, r5, r6)
            goto L_0x013d
        L_0x00bc:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r8 = com.google.android.gms.internal.zzcha.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcha r8 = (com.google.android.gms.internal.zzcha) r8
            java.lang.String r9 = r9.readString()
            byte[] r8 = r7.zza(r8, r9)
            r10.writeNoException()
            r10.writeByteArray(r8)
            return r0
        L_0x00d3:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r8 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgi r8 = (com.google.android.gms.internal.zzcgi) r8
            boolean r9 = com.google.android.gms.internal.zzew.zza(r9)
            java.util.List r8 = r7.zza(r8, r9)
        L_0x00e3:
            r10.writeNoException()
            r10.writeTypedList(r8)
            return r0
        L_0x00ea:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r8 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgi r8 = (com.google.android.gms.internal.zzcgi) r8
            r7.zzb(r8)
            goto L_0x013d
        L_0x00f6:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r8 = com.google.android.gms.internal.zzcha.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcha r8 = (com.google.android.gms.internal.zzcha) r8
            java.lang.String r11 = r9.readString()
            java.lang.String r9 = r9.readString()
            r7.zza(r8, r11, r9)
            goto L_0x013d
        L_0x010a:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r8 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcgi r8 = (com.google.android.gms.internal.zzcgi) r8
            r7.zza(r8)
            goto L_0x013d
        L_0x0116:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcln> r8 = com.google.android.gms.internal.zzcln.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcln r8 = (com.google.android.gms.internal.zzcln) r8
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r11 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r9 = com.google.android.gms.internal.zzew.zza(r9, r11)
            com.google.android.gms.internal.zzcgi r9 = (com.google.android.gms.internal.zzcgi) r9
            r7.zza(r8, r9)
            goto L_0x013d
        L_0x012a:
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r8 = com.google.android.gms.internal.zzcha.CREATOR
            android.os.Parcelable r8 = com.google.android.gms.internal.zzew.zza(r9, r8)
            com.google.android.gms.internal.zzcha r8 = (com.google.android.gms.internal.zzcha) r8
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgi> r11 = com.google.android.gms.internal.zzcgi.CREATOR
            android.os.Parcelable r9 = com.google.android.gms.internal.zzew.zza(r9, r11)
            com.google.android.gms.internal.zzcgi r9 = (com.google.android.gms.internal.zzcgi) r9
            r7.zza(r8, r9)
        L_0x013d:
            r10.writeNoException()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchf.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
    }
}
