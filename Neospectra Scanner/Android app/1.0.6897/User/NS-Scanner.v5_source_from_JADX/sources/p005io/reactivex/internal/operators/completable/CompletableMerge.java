package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableMerge */
public final class CompletableMerge extends Completable {
    final boolean delayErrors;
    final int maxConcurrency;
    final Publisher<? extends CompletableSource> source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableMerge$CompletableMergeSubscriber */
    static final class CompletableMergeSubscriber extends AtomicInteger implements FlowableSubscriber<CompletableSource>, Disposable {
        private static final long serialVersionUID = -2108443387387077490L;
        final CompletableObserver actual;
        final boolean delayErrors;
        final AtomicThrowable error = new AtomicThrowable();
        final int maxConcurrency;

        /* renamed from: s */
        Subscription f110s;
        final CompositeDisposable set = new CompositeDisposable();

        /* renamed from: io.reactivex.internal.operators.completable.CompletableMerge$CompletableMergeSubscriber$MergeInnerObserver */
        final class MergeInnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 251330541679988317L;

            MergeInnerObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onError(Throwable e) {
                CompletableMergeSubscriber.this.innerError(this, e);
            }

            public void onComplete() {
                CompletableMergeSubscriber.this.innerComplete(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }

        CompletableMergeSubscriber(CompletableObserver actual2, int maxConcurrency2, boolean delayErrors2) {
            this.actual = actual2;
            this.maxConcurrency = maxConcurrency2;
            this.delayErrors = delayErrors2;
            lazySet(1);
        }

        public void dispose() {
            this.f110s.cancel();
            this.set.dispose();
        }

        public boolean isDisposed() {
            return this.set.isDisposed();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f110s, s)) {
                this.f110s = s;
                this.actual.onSubscribe(this);
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    s.request(Long.MAX_VALUE);
                } else {
                    s.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(CompletableSource t) {
            getAndIncrement();
            MergeInnerObserver inner = new MergeInnerObserver();
            this.set.add(inner);
            t.subscribe(inner);
        }

        public void onError(Throwable t) {
            if (!this.delayErrors) {
                this.set.dispose();
                if (!this.error.addThrowable(t)) {
                    RxJavaPlugins.onError(t);
                } else if (getAndSet(0) > 0) {
                    this.actual.onError(this.error.terminate());
                }
            } else if (!this.error.addThrowable(t)) {
                RxJavaPlugins.onError(t);
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.error.terminate());
            }
        }

        public void onComplete() {
            if (decrementAndGet() != 0) {
                return;
            }
            if (((Throwable) this.error.get()) != null) {
                this.actual.onError(this.error.terminate());
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(MergeInnerObserver inner, Throwable t) {
            this.set.delete(inner);
            if (!this.delayErrors) {
                this.f110s.cancel();
                this.set.dispose();
                if (!this.error.addThrowable(t)) {
                    RxJavaPlugins.onError(t);
                } else if (getAndSet(0) > 0) {
                    this.actual.onError(this.error.terminate());
                }
            } else if (!this.error.addThrowable(t)) {
                RxJavaPlugins.onError(t);
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.error.terminate());
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f110s.request(1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(MergeInnerObserver inner) {
            this.set.delete(inner);
            if (decrementAndGet() == 0) {
                Throwable ex = (Throwable) this.error.get();
                if (ex != null) {
                    this.actual.onError(ex);
                } else {
                    this.actual.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.f110s.request(1);
            }
        }
    }

    public CompletableMerge(Publisher<? extends CompletableSource> source2, int maxConcurrency2, boolean delayErrors2) {
        this.source = source2;
        this.maxConcurrency = maxConcurrency2;
        this.delayErrors = delayErrors2;
    }

    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new CompletableMergeSubscriber(s, this.maxConcurrency, this.delayErrors));
    }
}
