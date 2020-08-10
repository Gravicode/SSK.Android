package p008rx;

import p008rx.AsyncEmitter.Cancellable;
import p008rx.annotations.Experimental;

@Experimental
@Deprecated
/* renamed from: rx.CompletableEmitter */
public interface CompletableEmitter {
    void onCompleted();

    void onError(Throwable th);

    void setCancellation(Cancellable cancellable);

    void setSubscription(Subscription subscription);
}
