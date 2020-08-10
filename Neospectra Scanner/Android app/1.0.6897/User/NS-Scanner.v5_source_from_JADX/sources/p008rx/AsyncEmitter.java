package p008rx;

import p008rx.annotations.Experimental;

@Experimental
@Deprecated
/* renamed from: rx.AsyncEmitter */
public interface AsyncEmitter<T> extends Observer<T> {

    /* renamed from: rx.AsyncEmitter$BackpressureMode */
    public enum BackpressureMode {
        NONE,
        ERROR,
        BUFFER,
        DROP,
        LATEST
    }

    /* renamed from: rx.AsyncEmitter$Cancellable */
    public interface Cancellable {
        void cancel() throws Exception;
    }

    long requested();

    void setCancellation(Cancellable cancellable);

    void setSubscription(Subscription subscription);
}
