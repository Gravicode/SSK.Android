package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;

public final class RxSeekBar {
    @CheckResult
    @NonNull
    public static Observable<Integer> changes(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new SeekBarChangeOnSubscribe<T>(view, null));
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> userChanges(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new SeekBarChangeOnSubscribe<T>(view, Boolean.valueOf(true)));
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> systemChanges(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new SeekBarChangeOnSubscribe<T>(view, Boolean.valueOf(false)));
    }

    @CheckResult
    @NonNull
    public static Observable<SeekBarChangeEvent> changeEvents(@NonNull SeekBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new SeekBarChangeEventOnSubscribe<T>(view));
    }

    private RxSeekBar() {
        throw new AssertionError("No instances.");
    }
}
