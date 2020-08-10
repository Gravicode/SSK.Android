package p008rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleTakeUntilSingle */
public final class SingleTakeUntilSingle<T, U> implements OnSubscribe<T> {
    final Single<? extends U> other;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleTakeUntilSingle$TakeUntilSourceSubscriber */
    static final class TakeUntilSourceSubscriber<T, U> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();
        final SingleSubscriber<U> other = new OtherSubscriber();

        /* renamed from: rx.internal.operators.SingleTakeUntilSingle$TakeUntilSourceSubscriber$OtherSubscriber */
        final class OtherSubscriber extends SingleSubscriber<U> {
            OtherSubscriber() {
            }

            public void onSuccess(U u) {
                onError(new CancellationException("Stream was canceled before emitting a terminal event."));
            }

            public void onError(Throwable error) {
                TakeUntilSourceSubscriber.this.onError(error);
            }
        }

        TakeUntilSourceSubscriber(SingleSubscriber<? super T> actual2) {
            this.actual = actual2;
            add(this.other);
        }

        public void onSuccess(T value) {
            if (this.once.compareAndSet(false, true)) {
                unsubscribe();
                this.actual.onSuccess(value);
            }
        }

        public void onError(Throwable error) {
            if (this.once.compareAndSet(false, true)) {
                unsubscribe();
                this.actual.onError(error);
                return;
            }
            RxJavaHooks.onError(error);
        }
    }

    public SingleTakeUntilSingle(OnSubscribe<T> source2, Single<? extends U> other2) {
        this.source = source2;
        this.other = other2;
    }

    public void call(SingleSubscriber<? super T> t) {
        TakeUntilSourceSubscriber<T, U> parent = new TakeUntilSourceSubscriber<>(t);
        t.add(parent);
        this.other.subscribe(parent.other);
        this.source.call(parent);
    }
}
