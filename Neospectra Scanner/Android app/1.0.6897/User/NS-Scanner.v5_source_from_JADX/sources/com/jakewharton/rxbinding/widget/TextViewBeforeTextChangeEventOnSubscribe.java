package com.jakewharton.rxbinding.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class TextViewBeforeTextChangeEventOnSubscribe implements OnSubscribe<TextViewBeforeTextChangeEvent> {
    final TextView view;

    TextViewBeforeTextChangeEventOnSubscribe(TextView view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super TextViewBeforeTextChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        final TextWatcher watcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(TextViewBeforeTextChangeEvent.create(TextViewBeforeTextChangeEventOnSubscribe.this.view, s, start, count, after));
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.view.addTextChangedListener(watcher);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                TextViewBeforeTextChangeEventOnSubscribe.this.view.removeTextChangedListener(watcher);
            }
        });
        subscriber.onNext(TextViewBeforeTextChangeEvent.create(this.view, this.view.getText(), 0, 0, 0));
    }
}
