package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func1;

final class AdapterViewItemLongClickEventOnSubscribe implements OnSubscribe<AdapterViewItemLongClickEvent> {
    final Func1<? super AdapterViewItemLongClickEvent, Boolean> handled;
    final AdapterView<?> view;

    public AdapterViewItemLongClickEventOnSubscribe(AdapterView<?> view2, Func1<? super AdapterViewItemLongClickEvent, Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super AdapterViewItemLongClickEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterViewItemLongClickEvent event = AdapterViewItemLongClickEvent.create(parent, view, position, id);
                if (!((Boolean) AdapterViewItemLongClickEventOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                AdapterViewItemLongClickEventOnSubscribe.this.view.setOnItemLongClickListener(null);
            }
        });
    }
}
