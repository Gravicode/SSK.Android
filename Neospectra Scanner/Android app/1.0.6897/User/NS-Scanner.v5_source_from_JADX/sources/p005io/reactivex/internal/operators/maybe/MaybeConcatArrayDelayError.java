package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError */
public final class MaybeConcatArrayDelayError<T> extends Flowable<T> {
    final MaybeSource<? extends T>[] sources;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError$ConcatMaybeObserver */
    static final class ConcatMaybeObserver<T> extends AtomicInteger implements MaybeObserver<T>, Subscription {
        private static final long serialVersionUID = 3520831347801429610L;
        final Subscriber<? super T> actual;
        final AtomicReference<Object> current = new AtomicReference<>(NotificationLite.COMPLETE);
        final SequentialDisposable disposables = new SequentialDisposable();
        final AtomicThrowable errors = new AtomicThrowable();
        int index;
        long produced;
        final AtomicLong requested = new AtomicLong();
        final MaybeSource<? extends T>[] sources;

        ConcatMaybeObserver(Subscriber<? super T> actual2, MaybeSource<? extends T>[] sources2) {
            this.actual = actual2;
            this.sources = sources2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            this.disposables.dispose();
        }

        public void onSubscribe(Disposable d) {
            this.disposables.replace(d);
        }

        public void onSuccess(T value) {
            this.current.lazySet(value);
            drain();
        }

        public void onError(Throwable e) {
            this.current.lazySet(NotificationLite.COMPLETE);
            if (this.errors.addThrowable(e)) {
                drain();
            } else {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            this.current.lazySet(NotificationLite.COMPLETE);
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            boolean goNextSource;
            if (getAndIncrement() == 0) {
                AtomicReference<Object> c = this.current;
                Subscriber<? super T> a = this.actual;
                Disposable cancelled = this.disposables;
                while (!cancelled.isDisposed()) {
                    Object o = c.get();
                    if (o != null) {
                        if (o != NotificationLite.COMPLETE) {
                            long p = this.produced;
                            if (p != this.requested.get()) {
                                this.produced = 1 + p;
                                c.lazySet(null);
                                goNextSource = true;
                                a.onNext(o);
                            } else {
                                goNextSource = false;
                            }
                        } else {
                            c.lazySet(null);
                            goNextSource = true;
                        }
                        if (goNextSource && !cancelled.isDisposed()) {
                            int i = this.index;
                            if (i == this.sources.length) {
                                if (((Throwable) this.errors.get()) != null) {
                                    a.onError(this.errors.terminate());
                                } else {
                                    a.onComplete();
                                }
                                return;
                            }
                            this.index = i + 1;
                            this.sources[i].subscribe(this);
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
                c.lazySet(null);
            }
        }
    }

    public MaybeConcatArrayDelayError(MaybeSource<? extends T>[] sources2) {
        this.sources = sources2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        ConcatMaybeObserver<T> parent = new ConcatMaybeObserver<>(s, this.sources);
        s.onSubscribe(parent);
        parent.drain();
    }
}
