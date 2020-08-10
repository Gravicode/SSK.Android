package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;

@TargetApi(21)
public final class RxToolbar {
    @CheckResult
    @NonNull
    public static Observable<MenuItem> itemClicks(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new ToolbarItemClickOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Observable<Void> navigationClicks(@NonNull Toolbar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new ToolbarNavigationClickOnSubscribe<T>(view));
    }

    private RxToolbar() {
        throw new AssertionError("No instances.");
    }
}
