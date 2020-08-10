package com.jakewharton.rxbinding.internal;

import android.os.Looper;

public final class Preconditions {
    public static void checkArgument(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value != null) {
            return value;
        }
        throw new NullPointerException(message);
    }

    public static void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Must be called from the main thread. Was: ");
            sb.append(Thread.currentThread());
            throw new IllegalStateException(sb.toString());
        }
    }

    private Preconditions() {
        throw new AssertionError("No instances.");
    }
}
