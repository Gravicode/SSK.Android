package p005io.reactivex.internal.operators.single;

import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleFromPublisher */
public final class SingleFromPublisher<T> extends Single<T> {
    final Publisher<? extends T> publisher;

    /* renamed from: io.reactivex.internal.operators.single.SingleFromPublisher$ToSingleObserver */
    static final class ToSingleObserver<T> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super T> actual;
        volatile boolean disposed;
        boolean done;

        /* renamed from: s */
        Subscription f418s;
        T value;

        ToSingleObserver(SingleObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f418s, s)) {
                this.f418s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.f418s.cancel();
                    this.done = true;
                    this.value = null;
                    this.actual.onError(new IndexOutOfBoundsException("Too many elements in the Publisher"));
                } else {
                    this.value = t;
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.value = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T v = this.value;
                this.value = null;
                if (v == null) {
                    this.actual.onError(new NoSuchElementException("The source Publisher is empty"));
                } else {
                    this.actual.onSuccess(v);
                }
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void dispose() {
            this.disposed = true;
            this.f418s.cancel();
        }
    }

    public SingleFromPublisher(Publisher<? extends T> publisher2) {
        this.publisher = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.publisher.subscribe(new ToSingleObserver(s));
    }
}
