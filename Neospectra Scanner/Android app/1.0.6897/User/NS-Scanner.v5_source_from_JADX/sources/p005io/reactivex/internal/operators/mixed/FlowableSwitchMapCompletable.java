package p005io.reactivex.internal.operators.mixed;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.FlowableSwitchMapCompletable */
public final class FlowableSwitchMapCompletable<T> extends Completable {
    final boolean delayErrors;
    final Function<? super T, ? extends CompletableSource> mapper;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.mixed.FlowableSwitchMapCompletable$SwitchMapCompletableObserver */
    static final class SwitchMapCompletableObserver<T> implements FlowableSubscriber<T>, Disposable {
        static final SwitchMapInnerObserver INNER_DISPOSED = new SwitchMapInnerObserver(null);
        final boolean delayErrors;
        volatile boolean done;
        final CompletableObserver downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicReference<SwitchMapInnerObserver> inner = new AtomicReference<>();
        final Function<? super T, ? extends CompletableSource> mapper;
        Subscription upstream;

        /* renamed from: io.reactivex.internal.operators.mixed.FlowableSwitchMapCompletable$SwitchMapCompletableObserver$SwitchMapInnerObserver */
        static final class SwitchMapInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
            private static final long serialVersionUID = -8003404460084760287L;
            final SwitchMapCompletableObserver<?> parent;

            SwitchMapInnerObserver(SwitchMapCompletableObserver<?> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onError(Throwable e) {
                this.parent.innerError(this, e);
            }

            public void onComplete() {
                this.parent.innerComplete(this);
            }

            /* access modifiers changed from: 0000 */
            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }

        SwitchMapCompletableObserver(CompletableObserver downstream2, Function<? super T, ? extends CompletableSource> mapper2, boolean delayErrors2) {
            this.downstream = downstream2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            try {
                CompletableSource c = (CompletableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null CompletableSource");
                SwitchMapInnerObserver o = new SwitchMapInnerObserver(this);
                while (true) {
                    SwitchMapInnerObserver current = (SwitchMapInnerObserver) this.inner.get();
                    if (current == INNER_DISPOSED) {
                        break;
                    } else if (this.inner.compareAndSet(current, o)) {
                        if (current != null) {
                            current.dispose();
                        }
                        c.subscribe(o);
                    }
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.upstream.cancel();
                onError(ex);
            }
        }

        public void onError(Throwable t) {
            if (!this.errors.addThrowable(t)) {
                RxJavaPlugins.onError(t);
            } else if (this.delayErrors) {
                onComplete();
            } else {
                disposeInner();
                Throwable ex = this.errors.terminate();
                if (ex != ExceptionHelper.TERMINATED) {
                    this.downstream.onError(ex);
                }
            }
        }

        public void onComplete() {
            this.done = true;
            if (this.inner.get() == null) {
                Throwable ex = this.errors.terminate();
                if (ex == null) {
                    this.downstream.onComplete();
                } else {
                    this.downstream.onError(ex);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            SwitchMapInnerObserver o = (SwitchMapInnerObserver) this.inner.getAndSet(INNER_DISPOSED);
            if (o != null && o != INNER_DISPOSED) {
                o.dispose();
            }
        }

        public void dispose() {
            this.upstream.cancel();
            disposeInner();
        }

        public boolean isDisposed() {
            return this.inner.get() == INNER_DISPOSED;
        }

        /* access modifiers changed from: 0000 */
        public void innerError(SwitchMapInnerObserver sender, Throwable error) {
            if (!this.inner.compareAndSet(sender, null) || !this.errors.addThrowable(error)) {
                RxJavaPlugins.onError(error);
                return;
            }
            if (!this.delayErrors) {
                dispose();
                Throwable ex = this.errors.terminate();
                if (ex != ExceptionHelper.TERMINATED) {
                    this.downstream.onError(ex);
                }
            } else if (this.done) {
                this.downstream.onError(this.errors.terminate());
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(SwitchMapInnerObserver sender) {
            if (this.inner.compareAndSet(sender, null) && this.done) {
                Throwable ex = this.errors.terminate();
                if (ex == null) {
                    this.downstream.onComplete();
                } else {
                    this.downstream.onError(ex);
                }
            }
        }
    }

    public FlowableSwitchMapCompletable(Flowable<T> source2, Function<? super T, ? extends CompletableSource> mapper2, boolean delayErrors2) {
        this.source = source2;
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SwitchMapCompletableObserver<Object>(s, this.mapper, this.delayErrors));
    }
}
