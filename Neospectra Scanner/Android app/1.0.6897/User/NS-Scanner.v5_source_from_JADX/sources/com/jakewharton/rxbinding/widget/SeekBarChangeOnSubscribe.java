package com.jakewharton.rxbinding.widget;

import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class SeekBarChangeOnSubscribe implements OnSubscribe<Integer> {
    @Nullable
    final Boolean shouldBeFromUser;
    final SeekBar view;

    public SeekBarChangeOnSubscribe(SeekBar view2, @Nullable Boolean shouldBeFromUser2) {
        this.view = view2;
        this.shouldBeFromUser = shouldBeFromUser2;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                if (SeekBarChangeOnSubscribe.this.shouldBeFromUser == null || SeekBarChangeOnSubscribe.this.shouldBeFromUser.booleanValue() == fromUser) {
                    subscriber.onNext(Integer.valueOf(progress));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                SeekBarChangeOnSubscribe.this.view.setOnSeekBarChangeListener(null);
            }
        });
        subscriber.onNext(Integer.valueOf(this.view.getProgress()));
    }
}
