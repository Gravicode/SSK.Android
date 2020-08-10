package p005io.reactivex.internal.operators.flowable;

import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableLastSingle */
public final class FlowableLastSingle<T> extends Single<T> {
    final T defaultItem;
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableLastSingle$LastSubscriber */
    static final class LastSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super T> actual;
        final T defaultItem;
        T item;

        /* renamed from: s */
        Subscription f172s;

        LastSubscriber(SingleObserver<? super T> actual2, T defaultItem2) {
            this.actual = actual2;
            this.defaultItem = defaultItem2;
        }

        public void dispose() {
            this.f172s.cancel();
            this.f172s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f172s == SubscriptionHelper.CANCELLED;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f172s, s)) {
                this.f172s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.item = t;
        }

        public void onError(Throwable t) {
            this.f172s = SubscriptionHelper.CANCELLED;
            this.item = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f172s = SubscriptionHelper.CANCELLED;
            T v = this.item;
            if (v != null) {
                this.item = null;
                this.actual.onSuccess(v);
                return;
            }
            T v2 = this.defaultItem;
            if (v2 != null) {
                this.actual.onSuccess(v2);
            } else {
                this.actual.onError(new NoSuchElementException());
            }
        }
    }

    public FlowableLastSingle(Publisher<T> source2, T defaultItem2) {
        this.source = source2;
        this.defaultItem = defaultItem2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(new LastSubscriber(observer, this.defaultItem));
    }
}
