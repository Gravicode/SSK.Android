package p005io.reactivex.internal.util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.util.EmptyComponent */
public enum EmptyComponent implements FlowableSubscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable {
    INSTANCE;

    public static <T> Subscriber<T> asSubscriber() {
        return INSTANCE;
    }

    public static <T> Observer<T> asObserver() {
        return INSTANCE;
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return true;
    }

    public void request(long n) {
    }

    public void cancel() {
    }

    public void onSubscribe(Disposable d) {
        d.dispose();
    }

    public void onSubscribe(Subscription s) {
        s.cancel();
    }

    public void onNext(Object t) {
    }

    public void onError(Throwable t) {
        RxJavaPlugins.onError(t);
    }

    public void onComplete() {
    }

    public void onSuccess(Object value) {
    }
}
