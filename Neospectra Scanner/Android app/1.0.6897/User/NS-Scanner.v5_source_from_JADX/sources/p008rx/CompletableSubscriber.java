package p008rx;

import p008rx.annotations.Experimental;

@Experimental
/* renamed from: rx.CompletableSubscriber */
public interface CompletableSubscriber {
    void onCompleted();

    void onError(Throwable th);

    void onSubscribe(Subscription subscription);
}
