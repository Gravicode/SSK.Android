package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.PopupMenu;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;

public final class RxPopupMenu {
    @CheckResult
    @NonNull
    public static Observable<MenuItem> itemClicks(@NonNull PopupMenu view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new PopupMenuItemClickOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Observable<Void> dismisses(@NonNull PopupMenu view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new PopupMenuDismissOnSubscribe<T>(view));
    }

    private RxPopupMenu() {
        throw new AssertionError("No instances.");
    }
}
