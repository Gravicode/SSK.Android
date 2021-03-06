package com.google.android.gms.common.stats;

import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.internal.zzbfm;

public abstract class StatsEvent extends zzbfm implements ReflectedParcelable {
    public abstract int getEventType();

    public abstract long getTimeMillis();

    public String toString() {
        long timeMillis = getTimeMillis();
        String str = "\t";
        int eventType = getEventType();
        String str2 = "\t";
        long zzamd = zzamd();
        String zzame = zzame();
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51 + String.valueOf(str2).length() + String.valueOf(zzame).length());
        sb.append(timeMillis);
        sb.append(str);
        sb.append(eventType);
        sb.append(str2);
        sb.append(zzamd);
        sb.append(zzame);
        return sb.toString();
    }

    public abstract long zzamd();

    public abstract String zzame();
}
