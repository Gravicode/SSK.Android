package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;

public final class MenuItemActionViewEvent extends MenuItemEvent<MenuItem> {
    private final Kind kind;

    public enum Kind {
        EXPAND,
        COLLAPSE
    }

    @CheckResult
    @NonNull
    public static MenuItemActionViewEvent create(@NonNull MenuItem menuItem, @NonNull Kind kind2) {
        return new MenuItemActionViewEvent(menuItem, kind2);
    }

    private MenuItemActionViewEvent(@NonNull MenuItem menuItem, @NonNull Kind kind2) {
        super(menuItem);
        this.kind = kind2;
    }

    @NonNull
    public Kind kind() {
        return this.kind;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuItemActionViewEvent that = (MenuItemActionViewEvent) o;
        if (!menuItem().equals(that.menuItem())) {
            return false;
        }
        if (this.kind != that.kind) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (menuItem().hashCode() * 31) + this.kind.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MenuItemActionViewEvent{menuItem=");
        sb.append(menuItem());
        sb.append(", kind=");
        sb.append(this.kind);
        sb.append('}');
        return sb.toString();
    }
}
