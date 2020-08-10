package com.google.android.gms.dynamic;

import com.google.android.gms.internal.zzev;

public abstract class zzl extends zzev implements zzk {
    public zzl() {
        attachInterface(this, "com.google.android.gms.dynamic.IFragmentWrapper");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0067, code lost:
        r4.writeNoException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006a, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00cf, code lost:
        r4.writeNoException();
        com.google.android.gms.internal.zzew.zza(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00d5, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00e4, code lost:
        r4.writeNoException();
        r4.writeInt(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00ea, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00fa, code lost:
        r4.writeNoException();
        com.google.android.gms.internal.zzew.zza(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0100, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTransact(int r2, android.os.Parcel r3, android.os.Parcel r4, int r5) throws android.os.RemoteException {
        /*
            r1 = this;
            boolean r5 = r1.zza(r2, r3, r4, r5)
            r0 = 1
            if (r5 == 0) goto L_0x0008
            return r0
        L_0x0008:
            r5 = 0
            switch(r2) {
                case 2: goto L_0x00f6;
                case 3: goto L_0x00eb;
                case 4: goto L_0x00e0;
                case 5: goto L_0x00db;
                case 6: goto L_0x00d6;
                case 7: goto L_0x00cb;
                case 8: goto L_0x00c0;
                case 9: goto L_0x00bb;
                case 10: goto L_0x00b6;
                case 11: goto L_0x00b1;
                case 12: goto L_0x00ac;
                case 13: goto L_0x00a7;
                case 14: goto L_0x00a2;
                case 15: goto L_0x009d;
                case 16: goto L_0x0098;
                case 17: goto L_0x0093;
                case 18: goto L_0x008e;
                case 19: goto L_0x0089;
                case 20: goto L_0x006b;
                case 21: goto L_0x0060;
                case 22: goto L_0x0058;
                case 23: goto L_0x0050;
                case 24: goto L_0x0048;
                case 25: goto L_0x003c;
                case 26: goto L_0x002c;
                case 27: goto L_0x000e;
                default: goto L_0x000c;
            }
        L_0x000c:
            r2 = 0
            return r2
        L_0x000e:
            android.os.IBinder r2 = r3.readStrongBinder()
            if (r2 != 0) goto L_0x0015
            goto L_0x0028
        L_0x0015:
            java.lang.String r3 = "com.google.android.gms.dynamic.IObjectWrapper"
            android.os.IInterface r3 = r2.queryLocalInterface(r3)
            boolean r5 = r3 instanceof com.google.android.gms.dynamic.IObjectWrapper
            if (r5 == 0) goto L_0x0023
            r5 = r3
            com.google.android.gms.dynamic.IObjectWrapper r5 = (com.google.android.gms.dynamic.IObjectWrapper) r5
            goto L_0x0028
        L_0x0023:
            com.google.android.gms.dynamic.zzm r5 = new com.google.android.gms.dynamic.zzm
            r5.<init>(r2)
        L_0x0028:
            r1.zzw(r5)
            goto L_0x0067
        L_0x002c:
            android.os.Parcelable$Creator r2 = android.content.Intent.CREATOR
            android.os.Parcelable r2 = com.google.android.gms.internal.zzew.zza(r3, r2)
            android.content.Intent r2 = (android.content.Intent) r2
            int r3 = r3.readInt()
            r1.startActivityForResult(r2, r3)
            goto L_0x0067
        L_0x003c:
            android.os.Parcelable$Creator r2 = android.content.Intent.CREATOR
            android.os.Parcelable r2 = com.google.android.gms.internal.zzew.zza(r3, r2)
            android.content.Intent r2 = (android.content.Intent) r2
            r1.startActivity(r2)
            goto L_0x0067
        L_0x0048:
            boolean r2 = com.google.android.gms.internal.zzew.zza(r3)
            r1.setUserVisibleHint(r2)
            goto L_0x0067
        L_0x0050:
            boolean r2 = com.google.android.gms.internal.zzew.zza(r3)
            r1.setRetainInstance(r2)
            goto L_0x0067
        L_0x0058:
            boolean r2 = com.google.android.gms.internal.zzew.zza(r3)
            r1.setMenuVisibility(r2)
            goto L_0x0067
        L_0x0060:
            boolean r2 = com.google.android.gms.internal.zzew.zza(r3)
            r1.setHasOptionsMenu(r2)
        L_0x0067:
            r4.writeNoException()
            return r0
        L_0x006b:
            android.os.IBinder r2 = r3.readStrongBinder()
            if (r2 != 0) goto L_0x0072
            goto L_0x0085
        L_0x0072:
            java.lang.String r3 = "com.google.android.gms.dynamic.IObjectWrapper"
            android.os.IInterface r3 = r2.queryLocalInterface(r3)
            boolean r5 = r3 instanceof com.google.android.gms.dynamic.IObjectWrapper
            if (r5 == 0) goto L_0x0080
            r5 = r3
            com.google.android.gms.dynamic.IObjectWrapper r5 = (com.google.android.gms.dynamic.IObjectWrapper) r5
            goto L_0x0085
        L_0x0080:
            com.google.android.gms.dynamic.zzm r5 = new com.google.android.gms.dynamic.zzm
            r5.<init>(r2)
        L_0x0085:
            r1.zzv(r5)
            goto L_0x0067
        L_0x0089:
            boolean r2 = r1.isVisible()
            goto L_0x00cf
        L_0x008e:
            boolean r2 = r1.isResumed()
            goto L_0x00cf
        L_0x0093:
            boolean r2 = r1.isRemoving()
            goto L_0x00cf
        L_0x0098:
            boolean r2 = r1.isInLayout()
            goto L_0x00cf
        L_0x009d:
            boolean r2 = r1.isHidden()
            goto L_0x00cf
        L_0x00a2:
            boolean r2 = r1.isDetached()
            goto L_0x00cf
        L_0x00a7:
            boolean r2 = r1.isAdded()
            goto L_0x00cf
        L_0x00ac:
            com.google.android.gms.dynamic.IObjectWrapper r2 = r1.getView()
            goto L_0x00fa
        L_0x00b1:
            boolean r2 = r1.getUserVisibleHint()
            goto L_0x00cf
        L_0x00b6:
            int r2 = r1.getTargetRequestCode()
            goto L_0x00e4
        L_0x00bb:
            com.google.android.gms.dynamic.zzk r2 = r1.zzaqa()
            goto L_0x00fa
        L_0x00c0:
            java.lang.String r2 = r1.getTag()
            r4.writeNoException()
            r4.writeString(r2)
            return r0
        L_0x00cb:
            boolean r2 = r1.getRetainInstance()
        L_0x00cf:
            r4.writeNoException()
            com.google.android.gms.internal.zzew.zza(r4, r2)
            return r0
        L_0x00d6:
            com.google.android.gms.dynamic.IObjectWrapper r2 = r1.zzapz()
            goto L_0x00fa
        L_0x00db:
            com.google.android.gms.dynamic.zzk r2 = r1.zzapy()
            goto L_0x00fa
        L_0x00e0:
            int r2 = r1.getId()
        L_0x00e4:
            r4.writeNoException()
            r4.writeInt(r2)
            return r0
        L_0x00eb:
            android.os.Bundle r2 = r1.getArguments()
            r4.writeNoException()
            com.google.android.gms.internal.zzew.zzb(r4, r2)
            return r0
        L_0x00f6:
            com.google.android.gms.dynamic.IObjectWrapper r2 = r1.zzapx()
        L_0x00fa:
            r4.writeNoException()
            com.google.android.gms.internal.zzew.zza(r4, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamic.zzl.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
    }
}
