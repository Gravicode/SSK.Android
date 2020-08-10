package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableLastMaybe */
public final class FlowableLastMaybe<T> extends Maybe<T> {
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableLastMaybe$LastSubscriber */
    static final class LastSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        T item;

        /* renamed from: s */
        Subscription f171s;

        LastSubscriber(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f171s.cancel();
            this.f171s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f171s == SubscriptionHelper.CANCELLED;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f171s, s)) {
                this.f171s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.item = t;
        }

        public void onError(Throwable t) {
            this.f171s = SubscriptionHelper.CANCELLED;
            this.item = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f171s = SubscriptionHelper.CANCELLED;
            T v = this.item;
            if (v != null) {
                this.item = null;
                this.actual.onSuccess(v);
                return;
            }
            this.actual.onComplete();
        }
    }

    public FlowableLastMaybe(Publisher<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new LastSubscriber(observer));
    }
}
