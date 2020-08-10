package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatWithMaybe */
public final class FlowableConcatWithMaybe<T> extends AbstractFlowableWithUpstream<T, T> {
    final MaybeSource<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatWithMaybe$ConcatWithSubscriber */
    static final class ConcatWithSubscriber<T> extends SinglePostCompleteSubscriber<T, T> implements MaybeObserver<T> {
        private static final long serialVersionUID = -7346385463600070225L;
        boolean inMaybe;
        MaybeSource<? extends T> other;
        final AtomicReference<Disposable> otherDisposable = new AtomicReference<>();

        ConcatWithSubscriber(Subscriber<? super T> actual, MaybeSource<? extends T> other2) {
            super(actual);
            this.other = other2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.otherDisposable, d);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onSuccess(T t) {
            complete(t);
        }

        public void onComplete() {
            if (this.inMaybe) {
                this.actual.onComplete();
                return;
            }
            this.inMaybe = true;
            this.f453s = SubscriptionHelper.CANCELLED;
            MaybeSource<? extends T> ms = this.other;
            this.other = null;
            ms.subscribe(this);
        }

        public void cancel() {
            super.cancel();
            DisposableHelper.dispose(this.otherDisposable);
        }
    }

    public FlowableConcatWithMaybe(Flowable<T> source, MaybeSource<? extends T> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ConcatWithSubscriber<Object>(s, this.other));
    }
}
