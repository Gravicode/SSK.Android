package p005io.reactivex.internal.observers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.observers.SubscriberCompletableObserver */
public final class SubscriberCompletableObserver<T> implements CompletableObserver, Subscription {

    /* renamed from: d */
    Disposable f94d;
    final Subscriber<? super T> subscriber;

    public SubscriberCompletableObserver(Subscriber<? super T> observer) {
        this.subscriber = observer;
    }

    public void onComplete() {
        this.subscriber.onComplete();
    }

    public void onError(Throwable e) {
        this.subscriber.onError(e);
    }

    public void onSubscribe(Disposable d) {
        if (DisposableHelper.validate(this.f94d, d)) {
            this.f94d = d;
            this.subscriber.onSubscribe(this);
        }
    }

    public void request(long n) {
    }

    public void cancel() {
        this.f94d.dispose();
    }
}
