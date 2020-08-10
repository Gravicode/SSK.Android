package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.MenuItem;

public abstract class MenuItemEvent<T extends MenuItem> {
    private final T menuItem;

    protected MenuItemEvent(@NonNull T menuItem2) {
        this.menuItem = menuItem2;
    }

    @NonNull
    public T menuItem() {
        return this.menuItem;
    }
}
