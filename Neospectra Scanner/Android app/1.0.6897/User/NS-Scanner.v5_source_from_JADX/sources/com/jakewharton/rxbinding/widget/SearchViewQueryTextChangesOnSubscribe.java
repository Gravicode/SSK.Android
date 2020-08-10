package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class SearchViewQueryTextChangesOnSubscribe implements OnSubscribe<CharSequence> {
    final SearchView view;

    SearchViewQueryTextChangesOnSubscribe(SearchView view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super CharSequence> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextChange(String s) {
                if (subscriber.isUnsubscribed()) {
                    return false;
                }
                subscriber.onNext(s);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                SearchViewQueryTextChangesOnSubscribe.this.view.setOnQueryTextListener(null);
            }
        });
        subscriber.onNext(this.view.getQuery());
    }
}
