package com.google.firebase.storage;

import android.support.annotation.NonNull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

final class zzv implements ThreadFactory {
    private final AtomicInteger zzici = new AtomicInteger(1);
    private final String zzokg;

    zzv(@NonNull String str) {
        this.zzokg = str;
    }

    public final Thread newThread(@NonNull Runnable runnable) {
        String str = this.zzokg;
        int andIncrement = this.zzici.getAndIncrement();
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 27);
        sb.append("FirebaseStorage-");
        sb.append(str);
        sb.append(andIncrement);
        Thread thread = new Thread(runnable, sb.toString());
        thread.setDaemon(false);
        thread.setPriority(9);
        return thread;
    }
}
