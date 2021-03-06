package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import android.widget.AdapterView;
import com.jakewharton.rxbinding.internal.Functions;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.functions.Action1;
import p008rx.functions.Func0;
import p008rx.functions.Func1;

public final class RxAdapterView {
    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemSelections(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new AdapterViewItemSelectionOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewSelectionEvent> selectionEvents(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new AdapterViewSelectionOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemClicks(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new AdapterViewItemClickOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewItemClickEvent> itemClickEvents(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new AdapterViewItemClickEventOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemLongClicks(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return itemLongClicks(view, Functions.FUNC0_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<Integer> itemLongClicks(@NonNull AdapterView<T> view, @NonNull Func0<Boolean> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return Observable.create((OnSubscribe<T>) new AdapterViewItemLongClickOnSubscribe<T>(view, handled));
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewItemLongClickEvent> itemLongClickEvents(@NonNull AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return itemLongClickEvents(view, Functions.FUNC1_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<AdapterViewItemLongClickEvent> itemLongClickEvents(@NonNull AdapterView<T> view, @NonNull Func1<? super AdapterViewItemLongClickEvent, Boolean> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return Observable.create((OnSubscribe<T>) new AdapterViewItemLongClickEventOnSubscribe<T>(view, handled));
    }

    @CheckResult
    @NonNull
    public static <T extends Adapter> Action1<? super Integer> selection(@NonNull final AdapterView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            public void call(Integer position) {
                view.setSelection(position.intValue());
            }
        };
    }

    private RxAdapterView() {
        throw new AssertionError("No instances.");
    }
}
