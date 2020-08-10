package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.functions.Action1;

public final class RxRadioGroup {
    @CheckResult
    @NonNull
    public static Observable<Integer> checkedChanges(@NonNull RadioGroup view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new RadioGroupCheckedChangeOnSubscribe<T>(view)).distinctUntilChanged();
    }

    @CheckResult
    @NonNull
    public static Action1<? super Integer> checked(@NonNull final RadioGroup view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            public void call(Integer value) {
                if (value.intValue() == -1) {
                    view.clearCheck();
                } else {
                    view.check(value.intValue());
                }
            }
        };
    }

    private RxRadioGroup() {
        throw new AssertionError("No instances.");
    }
}
