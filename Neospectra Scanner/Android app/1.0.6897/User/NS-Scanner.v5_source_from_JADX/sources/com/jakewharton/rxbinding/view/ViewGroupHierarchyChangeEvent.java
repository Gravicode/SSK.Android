package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewGroupHierarchyChangeEvent extends ViewEvent<ViewGroup> {
    private final View child;

    ViewGroupHierarchyChangeEvent(@NonNull ViewGroup view, View child2) {
        super(view);
        this.child = child2;
    }

    @NonNull
    public final View child() {
        return this.child;
    }
}
