package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher */
public final class MaybeDelayOtherPublisher<T, U> extends AbstractMaybeWithUpstream<T, T> {
    final Publisher<U> other;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher$DelayMaybeObserver */
    static final class DelayMaybeObserver<T, U> implements MaybeObserver<T>, Disposable {

        /* renamed from: d */
        Disposable f241d;
        final OtherSubscriber<T> other;
        final Publisher<U> otherSource;

        DelayMaybeObserver(MaybeObserver<? super T> actual, Publisher<U> otherSource2) {
            this.other = new OtherSubscriber<>(actual);
            this.otherSource = otherSource2;
        }

        public void dispose() {
            this.f241d.dispose();
            this.f241d = DisposableHelper.DISPOSED;
            SubscriptionHelper.cancel(this.other);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) this.other.get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f241d, d)) {
                this.f241d = d;
                this.other.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f241d = DisposableHelper.DISPOSED;
            this.other.value = value;
            subscribeNext();
        }

        public void onError(Throwable e) {
            this.f241d = DisposableHelper.DISPOSED;
            this.other.error = e;
            subscribeNext();
        }

        public void onComplete() {
            this.f241d = DisposableHelper.DISPOSED;
            subscribeNext();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            this.otherSource.subscribe(this.other);
        }
    }

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher$OtherSubscriber */
    static final class OtherSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
        private static final long serialVersionUID = -1215060610805418006L;
        final MaybeObserver<? super T> actual;
        Throwable error;
        T value;

        OtherSubscriber(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
        }

        public void onNext(Object t) {
            Subscription s = (Subscription) get();
            if (s != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                s.cancel();
                onComplete();
            }
        }

        public void onError(Throwable t) {
            Throwable e = this.error;
            if (e == null) {
                this.actual.onError(t);
                return;
            }
            this.actual.onError(new CompositeException(e, t));
        }

        public void onComplete() {
            Throwable e = this.error;
            if (e != null) {
                this.actual.onError(e);
                return;
            }
            T v = this.value;
            if (v != null) {
                this.actual.onSuccess(v);
            } else {
                this.actual.onComplete();
            }
        }
    }

    public MaybeDelayOtherPublisher(MaybeSource<T> source, Publisher<U> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new DelayMaybeObserver(observer, this.other));
    }
}
