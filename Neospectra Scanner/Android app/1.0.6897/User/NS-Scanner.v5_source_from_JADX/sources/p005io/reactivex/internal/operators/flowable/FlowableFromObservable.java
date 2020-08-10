package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromObservable */
public final class FlowableFromObservable<T> extends Flowable<T> {
    private final Observable<T> upstream;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromObservable$SubscriberObserver */
    static class SubscriberObserver<T> implements Observer<T>, Subscription {

        /* renamed from: d */
        private Disposable f164d;

        /* renamed from: s */
        private final Subscriber<? super T> f165s;

        SubscriberObserver(Subscriber<? super T> s) {
            this.f165s = s;
        }

        public void onComplete() {
            this.f165s.onComplete();
        }

        public void onError(Throwable e) {
            this.f165s.onError(e);
        }

        public void onNext(T value) {
            this.f165s.onNext(value);
        }

        public void onSubscribe(Disposable d) {
            this.f164d = d;
            this.f165s.onSubscribe(this);
        }

        public void cancel() {
            this.f164d.dispose();
        }

        public void request(long n) {
        }
    }

    public FlowableFromObservable(Observable<T> upstream2) {
        this.upstream = upstream2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.upstream.subscribe((Observer<? super T>) new SubscriberObserver<Object>(s));
    }
}
