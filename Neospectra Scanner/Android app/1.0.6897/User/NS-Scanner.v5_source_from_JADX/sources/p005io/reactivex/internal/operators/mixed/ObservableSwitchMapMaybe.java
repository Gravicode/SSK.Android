package p005io.reactivex.internal.operators.mixed;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.ObservableSwitchMapMaybe */
public final class ObservableSwitchMapMaybe<T, R> extends Observable<R> {
    final boolean delayErrors;
    final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
    final Observable<T> source;

    /* renamed from: io.reactivex.internal.operators.mixed.ObservableSwitchMapMaybe$SwitchMapMaybeMainObserver */
    static final class SwitchMapMaybeMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        static final SwitchMapMaybeObserver<Object> INNER_DISPOSED = new SwitchMapMaybeObserver<>(null);
        private static final long serialVersionUID = -5402190102429853762L;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final Observer<? super R> downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicReference<SwitchMapMaybeObserver<R>> inner = new AtomicReference<>();
        final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
        Disposable upstream;

        /* renamed from: io.reactivex.internal.operators.mixed.ObservableSwitchMapMaybe$SwitchMapMaybeMainObserver$SwitchMapMaybeObserver */
        static final class SwitchMapMaybeObserver<R> extends AtomicReference<Disposable> implements MaybeObserver<R> {
            private static final long serialVersionUID = 8042919737683345351L;
            volatile R item;
            final SwitchMapMaybeMainObserver<?, R> parent;

            SwitchMapMaybeObserver(SwitchMapMaybeMainObserver<?, R> parent2) {
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

        SwitchMapMaybeMainObserver(Observer<? super R> downstream2, Function<? super T, ? extends MaybeSource<? extends R>> mapper2, boolean delayErrors2) {
            this.downstream = downstream2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
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
                this.upstream.dispose();
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

        public void dispose() {
            this.cancelled = true;
            this.upstream.dispose();
            disposeInner();
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void innerError(SwitchMapMaybeObserver<R> sender, Throwable ex) {
            if (!this.inner.compareAndSet(sender, null) || !this.errors.addThrowable(ex)) {
                RxJavaPlugins.onError(ex);
                return;
            }
            if (!this.delayErrors) {
                this.upstream.dispose();
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
                Observer<? super R> downstream2 = this.downstream;
                AtomicThrowable errors2 = this.errors;
                AtomicReference<SwitchMapMaybeObserver<R>> inner2 = this.inner;
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
                        } else if (empty || current.item == null) {
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else {
                            inner2.compareAndSet(current, null);
                            downstream2.onNext(current.item);
                        }
                    } else {
                        downstream2.onError(errors2.terminate());
                        return;
                    }
                }
            }
        }
    }

    public ObservableSwitchMapMaybe(Observable<T> source2, Function<? super T, ? extends MaybeSource<? extends R>> mapper2, boolean delayErrors2) {
        this.source = source2;
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> s) {
        if (!ScalarXMapZHelper.tryAsMaybe(this.source, this.mapper, s)) {
            this.source.subscribe((Observer<? super T>) new SwitchMapMaybeMainObserver<Object>(s, this.mapper, this.delayErrors));
        }
    }
}
