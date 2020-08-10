package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewGroupHierarchyChangeEventOnSubscribe implements OnSubscribe<ViewGroupHierarchyChangeEvent> {
    final ViewGroup viewGroup;

    ViewGroupHierarchyChangeEventOnSubscribe(ViewGroup viewGroup2) {
        this.viewGroup = viewGroup2;
    }

    public void call(final Subscriber<? super ViewGroupHierarchyChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        this.viewGroup.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            public void onChildViewAdded(View parent, View child) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ViewGroupHierarchyChildViewAddEvent.create((ViewGroup) parent, child));
                }
            }

            public void onChildViewRemoved(View parent, View child) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ViewGroupHierarchyChildViewRemoveEvent.create((ViewGroup) parent, child));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewGroupHierarchyChangeEventOnSubscribe.this.viewGroup.setOnHierarchyChangeListener(null);
            }
        });
    }
}
