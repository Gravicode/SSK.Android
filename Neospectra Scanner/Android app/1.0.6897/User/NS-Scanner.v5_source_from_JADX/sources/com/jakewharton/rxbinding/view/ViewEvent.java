package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;

public abstract class ViewEvent<T extends View> {
    private final T view;

    protected ViewEvent(@NonNull T view2) {
        this.view = view2;
    }

    @NonNull
    public T view() {
        return this.view;
    }
}
