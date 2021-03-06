package p005io.reactivex.internal.operators.mixed;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.FlowableSwitchMapMaybe */
public final class FlowableSwitchMapMaybe<T, R> extends Flowable<R> {
    final boolean delayErrors;
    final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.mixed.FlowableSwitchMapMaybe$SwitchMapMaybeSubscriber */
    static final class SwitchMapMaybeSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        static final SwitchMapMaybeObserver<Object> INNER_DISPOSED = new SwitchMapMaybeObserver<>(null);
        private static final long serialVersionUID = -5402190102429853762L;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final Subscriber<? super R> downstream;
        long emitted;
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicReference<SwitchMapMaybeObserver<R>> inner = new AtomicReference<>();
        final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
        final AtomicLong requested = new AtomicLong();
        Subscription upstream;

        /* renamed from: io.reactivex.internal.operators.mixed.FlowableSwitchMapMaybe$SwitchMapMaybeSubscriber$SwitchMapMaybeObserver */
        static final class SwitchMapMaybeObserver<R> extends AtomicReference<Disposable> implements MaybeObserver<R> {
            private static final long serialVersionUID = 8042919737683345351L;
            volatile R item;
            final SwitchMapMaybeSubscriber<?, R> parent;

            SwitchMapMaybeObserver(SwitchMapMaybeSubscriber<?, R> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onSuccess(R t) {
                this.item = t;
                this.parent.drain();
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

        SwitchMapMaybeSubscriber(Subscriber<? super R> downstream2, Function<? super T, ? extends MaybeSource<? extends R>> mapper2, boolean delayErrors2) {
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
            SwitchMapMaybeObserver<R> current = (SwitchMapMaybeObserver) this.inner.get();
            if (current != null) {
                current.dispose();
            }
            try {
                MaybeSource<? extends R> ms = (MaybeSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null MaybeSource");
                SwitchMapMaybeObserver<R> observer = new SwitchMapMaybeObserver<>(this);
                while (true) {
                    SwitchMapMaybeObserver<R> current2 = (SwitchMapMaybeObserver) this.inner.get();
                    if (current2 != INNER_DISPOSED) {
                        if (this.inner.compareAndSet(current2, observer)) {
                            ms.subscribe(observer);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.upstream.cancel();
                this.inner.getAndSet(INNER_DISPOSED);
                onError(ex);
            }
        }

        public void onError(Throwable t) {
            if (this.errors.addThrowable(t)) {
                if (!this.delayErrors) {
                    disposeInner();
                }
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            SwitchMapMaybeObserver<R> current = (SwitchMapMaybeObserver) this.inner.getAndSet(INNER_DISPOSED);
            if (current != null && current != INNER_DISPOSED) {
                current.dispose();
            }
        }

        public void request(long n) {
            BackpressureHelper.add(this.requested, n);
            drain();
        }

        public void cancel() {
            this.cancelled = true;
            this.upstream.cancel();
            disposeInner();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(SwitchMapMaybeObserver<R> sender, Throwable ex) {
            if (!this.inner.compareAndSet(sender, null) || !this.errors.addThrowable(ex)) {
                RxJavaPlugins.onError(ex);
                return;
            }
            if (!this.delayErrors) {
                this.upstream.cancel();
                disposeInner();
            }
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(SwitchMapMaybeObserver<R> sender) {
            if (this.inner.compareAndSet(sender, null)) {
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super R> downstream2 = this.downstream;
                AtomicThrowable errors2 = this.errors;
                AtomicReference<SwitchMapMaybeObserver<R>> inner2 = this.inner;
                AtomicLong requested2 = this.requested;
                long emitted2 = this.emitted;
                while (!this.cancelled) {
                    if (errors2.get() == null || this.delayErrors) {
                        boolean d = this.done;
                        SwitchMapMaybeObserver<R> current = (SwitchMapMaybeObserver) inner2.get();
                        boolean empty = current == null;
                        if (d && empty) {
                            Throwable ex = errors2.terminate();
                            if (ex != null) {
                                downstream2.onError(ex);
                            } else {
                                downstream2.onComplete();
                            }
                            return;
                        } else if (empty || current.item == null || emitted2 == requested2.get()) {
                            this.emitted = emitted2;
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else {
                            inner2.compareAndSet(current, null);
                            downstream2.onNext(current.item);
                            emitted2++;
                        }
                    } else {
                        downstream2.onError(errors2.terminate());
                        return;
                    }
                }
            }
        }
    }

    public FlowableSwitchMapMaybe(Flowable<T> source2, Function<? super T, ? extends MaybeSource<? extends R>> mapper2, boolean delayErrors2) {
        this.source = source2;
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SwitchMapMaybeSubscriber<Object>(s, this.mapper, this.delayErrors));
    }
}
