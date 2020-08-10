package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.QueueDisposable;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSwitchMap */
public final class ObservableSwitchMap<T, R> extends AbstractObservableWithUpstream<T, R> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends R>> mapper;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapInnerObserver */
    static final class SwitchMapInnerObserver<T, R> extends AtomicReference<Disposable> implements Observer<R> {
        private static final long serialVersionUID = 3837284832786408377L;
        final int bufferSize;
        volatile boolean done;
        final long index;
        final SwitchMapObserver<T, R> parent;
        volatile SimpleQueue<R> queue;

        SwitchMapInnerObserver(SwitchMapObserver<T, R> parent2, long index2, int bufferSize2) {
            this.parent = parent2;
            this.index = index2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.setOnce(this, s)) {
                if (s instanceof QueueDisposable) {
                    QueueDisposable<R> qd = (QueueDisposable) s;
                    int m = qd.requestFusion(7);
                    if (m == 1) {
                        this.queue = qd;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (m == 2) {
                        this.queue = qd;
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
            }
        }

        public void onNext(R t) {
            if (this.index == this.parent.unique) {
                if (t != null) {
                    this.queue.offer(t);
                }
                this.parent.drain();
            }
        }

        public void onError(Throwable t) {
            this.parent.innerError(this, t);
        }

        public void onComplete() {
            if (this.index == this.parent.unique) {
                this.done = true;
                this.parent.drain();
            }
        }

        public void cancel() {
            DisposableHelper.dispose(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSwitchMap$SwitchMapObserver */
    static final class SwitchMapObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable {
        static final SwitchMapInnerObserver<Object, Object> CANCELLED = new SwitchMapInnerObserver<>(null, -1, 1);
        private static final long serialVersionUID = -3491074160481096299L;
        final AtomicReference<SwitchMapInnerObserver<T, R>> active = new AtomicReference<>();
        final Observer<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors;
        final Function<? super T, ? extends ObservableSource<? extends R>> mapper;

        /* renamed from: s */
        Disposable f358s;
        volatile long unique;

        static {
            CANCELLED.cancel();
        }

        SwitchMapObserver(Observer<? super R> actual2, Function<? super T, ? extends ObservableSource<? extends R>> mapper2, int bufferSize2, boolean delayErrors2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.delayErrors = delayErrors2;
            this.errors = new AtomicThrowable();
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f358s, s)) {
                this.f358s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            long c = this.unique + 1;
            this.unique = c;
            SwitchMapInnerObserver<T, R> inner = (SwitchMapInnerObserver) this.active.get();
            if (inner != null) {
                inner.cancel();
            }
            try {
                ObservableSource<? extends R> p = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The ObservableSource returned is null");
                SwitchMapInnerObserver<T, R> nextInner = new SwitchMapInnerObserver<>(this, c, this.bufferSize);
                while (true) {
                    SwitchMapInnerObserver<T, R> inner2 = (SwitchMapInnerObserver) this.active.get();
                    if (inner2 != CANCELLED) {
                        if (this.active.compareAndSet(inner2, nextInner)) {
                            p.subscribe(nextInner);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.f358s.dispose();
                onError(e);
            }
        }

        public void onError(Throwable t) {
            if (this.done || !this.errors.addThrowable(t)) {
                RxJavaPlugins.onError(t);
                return;
            }
            if (!this.delayErrors) {
                disposeInner();
            }
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f358s.dispose();
                disposeInner();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            if (((SwitchMapInnerObserver) this.active.get()) != CANCELLED) {
                SwitchMapInnerObserver<T, R> a = (SwitchMapInnerObserver) this.active.getAndSet(CANCELLED);
                if (a != CANCELLED && a != null) {
                    a.cancel();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            boolean retry;
            Object obj;
            if (getAndIncrement() == 0) {
                Observer<? super R> a = this.actual;
                AtomicReference<SwitchMapInnerObserver<T, R>> active2 = this.active;
                boolean delayErrors2 = this.delayErrors;
                int missing = 1;
                while (!this.cancelled) {
                    if (this.done) {
                        boolean empty = active2.get() == null;
                        if (delayErrors2) {
                            if (empty) {
                                Throwable ex = (Throwable) this.errors.get();
                                if (ex != null) {
                                    a.onError(ex);
                                } else {
                                    a.onComplete();
                                }
                                return;
                            }
                        } else if (((Throwable) this.errors.get()) != null) {
                            a.onError(this.errors.terminate());
                            return;
                        } else if (empty) {
                            a.onComplete();
                            return;
                        }
                    }
                    SwitchMapInnerObserver<T, R> inner = (SwitchMapInnerObserver) active2.get();
                    if (inner != null) {
                        SimpleQueue<R> q = inner.queue;
                        if (q != null) {
                            if (inner.done) {
                                boolean empty2 = q.isEmpty();
                                if (delayErrors2) {
                                    if (empty2) {
                                        active2.compareAndSet(inner, null);
                                    }
                                } else if (((Throwable) this.errors.get()) != null) {
                                    a.onError(this.errors.terminate());
                                    return;
                                } else if (empty2) {
                                    active2.compareAndSet(inner, null);
                                }
                            }
                            boolean retry2 = false;
                            while (!this.cancelled) {
                                if (inner != active2.get()) {
                                    retry = true;
                                } else if (delayErrors2 || ((Throwable) this.errors.get()) == null) {
                                    boolean d = inner.done;
                                    try {
                                        obj = q.poll();
                                    } catch (Throwable ex2) {
                                        Exceptions.throwIfFatal(ex2);
                                        this.errors.addThrowable(ex2);
                                        active2.compareAndSet(inner, null);
                                        if (!delayErrors2) {
                                            disposeInner();
                                            this.f358s.dispose();
                                            this.done = true;
                                        } else {
                                            inner.cancel();
                                        }
                                        retry2 = true;
                                        obj = null;
                                    }
                                    boolean empty3 = obj == null;
                                    if (d && empty3) {
                                        active2.compareAndSet(inner, null);
                                        retry = true;
                                    } else if (empty3) {
                                        retry = retry2;
                                    } else {
                                        a.onNext(obj);
                                    }
                                } else {
                                    a.onError(this.errors.terminate());
                                    return;
                                }
                                if (retry) {
                                    continue;
                                }
                            }
                            return;
                        }
                    }
                    missing = addAndGet(-missing);
                    if (missing == 0) {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(SwitchMapInnerObserver<T, R> inner, Throwable ex) {
            if (inner.index != this.unique || !this.errors.addThrowable(ex)) {
                RxJavaPlugins.onError(ex);
                return;
            }
            if (!this.delayErrors) {
                this.f358s.dispose();
            }
            inner.done = true;
            drain();
        }
    }

    public ObservableSwitchMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper2, int bufferSize2, boolean delayErrors2) {
        super(source);
        this.mapper = mapper2;
        this.bufferSize = bufferSize2;
        this.delayErrors = delayErrors2;
    }

    public void subscribeActual(Observer<? super R> t) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, t, this.mapper)) {
            this.source.subscribe(new SwitchMapObserver(t, this.mapper, this.bufferSize, this.delayErrors));
        }
    }
}
