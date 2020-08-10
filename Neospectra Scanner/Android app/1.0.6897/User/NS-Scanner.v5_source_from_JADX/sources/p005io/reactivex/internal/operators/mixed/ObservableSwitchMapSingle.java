package p005io.reactivex.internal.operators.mixed;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle */
public final class ObservableSwitchMapSingle<T, R> extends Observable<R> {
    final boolean delayErrors;
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    final Observable<T> source;

    /* renamed from: io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle$SwitchMapSingleMainObserver */
    static final class SwitchMapSingleMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        static final SwitchMapSingleObserver<Object> INNER_DISPOSED = new SwitchMapSingleObserver<>(null);
        private static final long serialVersionUID = -5402190102429853762L;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final Observer<? super R> downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicReference<SwitchMapSingleObserver<R>> inner = new AtomicReference<>();
        final Function<? super T, ? extends SingleSource<? extends R>> mapper;
        Disposable upstream;

        /* renamed from: io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle$SwitchMapSingleMainObserver$SwitchMapSingleObserver */
        static final class SwitchMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
            private static final long serialVersionUID = 8042919737683345351L;
            volatile R item;
            final SwitchMapSingleMainObserver<?, R> parent;

            SwitchMapSingleObserver(SwitchMapSingleMainObserver<?, R> parent2) {
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

            /* access modifiers changed from: 0000 */
            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }

        SwitchMapSingleMainObserver(Observer<? super R> downstream2, Function<? super T, ? extends SingleSource<? extends R>> mapper2, boolean delayErrors2) {
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
            SwitchMapSingleObserver<R> current = (SwitchMapSingleObserver) this.inner.get();
            if (current != null) {
                current.dispose();
            }
            try {
                SingleSource<? extends R> ss = (SingleSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null SingleSource");
                SwitchMapSingleObserver<R> observer = new SwitchMapSingleObserver<>(this);
                while (true) {
                    SwitchMapSingleObserver<R> current2 = (SwitchMapSingleObserver) this.inner.get();
                    if (current2 != INNER_DISPOSED) {
                        if (this.inner.compareAndSet(current2, observer)) {
                            ss.subscribe(observer);
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
            SwitchMapSingleObserver<R> current = (SwitchMapSingleObserver) this.inner.getAndSet(INNER_DISPOSED);
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
        public void innerError(SwitchMapSingleObserver<R> sender, Throwable ex) {
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
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Observer<? super R> downstream2 = this.downstream;
                AtomicThrowable errors2 = this.errors;
                AtomicReference<SwitchMapSingleObserver<R>> inner2 = this.inner;
                while (!this.cancelled) {
                    if (errors2.get() == null || this.delayErrors) {
                        boolean d = this.done;
                        SwitchMapSingleObserver<R> current = (SwitchMapSingleObserver) inner2.get();
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

    public ObservableSwitchMapSingle(Observable<T> source2, Function<? super T, ? extends SingleSource<? extends R>> mapper2, boolean delayErrors2) {
        this.source = source2;
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> s) {
        if (!ScalarXMapZHelper.tryAsSingle(this.source, this.mapper, s)) {
            this.source.subscribe((Observer<? super T>) new SwitchMapSingleMainObserver<Object>(s, this.mapper, this.delayErrors));
        }
    }
}
