package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapCompletable */
public final class FlowableFlatMapCompletable<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean delayErrors;
    final Function<? super T, ? extends CompletableSource> mapper;
    final int maxConcurrency;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapCompletable$FlatMapCompletableMainSubscriber */
    static final class FlatMapCompletableMainSubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = 8443155186132538303L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final boolean delayErrors;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends CompletableSource> mapper;
        final int maxConcurrency;

        /* renamed from: s */
        Subscription f158s;
        final CompositeDisposable set = new CompositeDisposable();

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapCompletable$FlatMapCompletableMainSubscriber$InnerConsumer */
        final class InnerConsumer extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 8606673141535671828L;

            InnerConsumer() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onComplete() {
                FlatMapCompletableMainSubscriber.this.innerComplete(this);
            }

            public void onError(Throwable e) {
                FlatMapCompletableMainSubscriber.this.innerError(this, e);
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }
        }

        FlatMapCompletableMainSubscriber(Subscriber<? super T> observer, Function<? super T, ? extends CompletableSource> mapper2, boolean delayErrors2, int maxConcurrency2) {
            this.actual = observer;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            this.maxConcurrency = maxConcurrency2;
            lazySet(1);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f158s, s)) {
                this.f158s = s;
                this.actual.onSubscribe(this);
                int m = this.maxConcurrency;
                if (m == Integer.MAX_VALUE) {
                    s.request(Long.MAX_VALUE);
                } else {
                    s.request((long) m);
                }
            }
        }

        public void onNext(T value) {
            try {
                CompletableSource cs = (CompletableSource) ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
                getAndIncrement();
                InnerConsumer inner = new InnerConsumer<>();
                if (!this.cancelled && this.set.add(inner)) {
                    cs.subscribe(inner);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f158s.cancel();
                onError(ex);
            }
        }

        public void onError(Throwable e) {
            if (!this.errors.addThrowable(e)) {
                RxJavaPlugins.onError(e);
            } else if (!this.delayErrors) {
                cancel();
                if (getAndSet(0) > 0) {
                    this.actual.onError(this.errors.terminate());
                }
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.errors.terminate());
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f158s.request(1);
            }
        }

        public void onComplete() {
            if (decrementAndGet() == 0) {
                Throwable ex = this.errors.terminate();
                if (ex != null) {
                    this.actual.onError(ex);
                } else {
                    this.actual.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f158s.request(1);
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f158s.cancel();
            this.set.dispose();
        }

        public void request(long n) {
        }

        @Nullable
        public T poll() throws Exception {
            return null;
        }

        public boolean isEmpty() {
            return true;
        }

        public void clear() {
        }

        public int requestFusion(int mode) {
            return mode & 2;
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerConsumer inner) {
            this.set.delete(inner);
            onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerConsumer inner, Throwable e) {
            this.set.delete(inner);
            onError(e);
        }
    }

    public FlowableFlatMapCompletable(Flowable<T> source, Function<? super T, ? extends CompletableSource> mapper2, boolean delayErrors2, int maxConcurrency2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
        this.maxConcurrency = maxConcurrency2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> observer) {
        this.source.subscribe((FlowableSubscriber<? super T>) new FlatMapCompletableMainSubscriber<Object>(observer, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
