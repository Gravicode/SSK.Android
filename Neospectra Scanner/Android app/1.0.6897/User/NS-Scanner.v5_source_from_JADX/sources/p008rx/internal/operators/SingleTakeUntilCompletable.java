package p008rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Completable;
import p008rx.CompletableSubscriber;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.Subscription;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleTakeUntilCompletable */
public final class SingleTakeUntilCompletable<T> implements OnSubscribe<T> {
    final Completable other;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleTakeUntilCompletable$TakeUntilSourceSubscriber */
    static final class TakeUntilSourceSubscriber<T> extends SingleSubscriber<T> implements CompletableSubscriber {
        final SingleSubscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();

        TakeUntilSourceSubscriber(SingleSubscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription d) {
            add(d);
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

        public void onCompleted() {
            onError(new CancellationException("Stream was canceled before emitting a terminal event."));
        }
    }

    public SingleTakeUntilCompletable(OnSubscribe<T> source2, Completable other2) {
        this.source = source2;
        this.other = other2;
    }

    public void call(SingleSubscriber<? super T> t) {
        TakeUntilSourceSubscriber<T> parent = new TakeUntilSourceSubscriber<>(t);
        t.add(parent);
        this.other.subscribe((CompletableSubscriber) parent);
        this.source.call(parent);
    }
}
