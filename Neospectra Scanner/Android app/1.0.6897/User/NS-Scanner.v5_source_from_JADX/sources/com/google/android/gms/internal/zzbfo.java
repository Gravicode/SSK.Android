package com.google.android.gms.internal;

import android.os.Parcel;

public final class zzbfo extends RuntimeException {
    public zzbfo(String str, Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int dataSize = parcel.dataSize();
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 41);
        sb.append(str);
        sb.append(" Parcel: pos=");
        sb.append(dataPosition);
        sb.append(" size=");
        sb.append(dataSize);
        super(sb.toString());
    }
}
