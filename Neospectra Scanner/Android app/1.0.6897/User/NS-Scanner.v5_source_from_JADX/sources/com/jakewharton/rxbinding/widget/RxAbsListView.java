package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;

public final class RxAbsListView {
    @CheckResult
    @NonNull
    public static Observable<AbsListViewScrollEvent> scrollEvents(@NonNull AbsListView absListView) {
        Preconditions.checkNotNull(absListView, "absListView == null");
        return Observable.create((OnSubscribe<T>) new AbsListViewScrollEventOnSubscribe<T>(absListView));
    }

    private RxAbsListView() {
        throw new AssertionError("No instances.");
    }
}
