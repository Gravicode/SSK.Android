package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.observers.DisposableObserver;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.subjects.UnicastSubject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundary */
public final class ObservableWindowBoundary<T, B> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int capacityHint;
    final ObservableSource<B> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundary$WindowBoundaryInnerObserver */
    static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> {
        boolean done;
        final WindowBoundaryMainObserver<T, B> parent;

        WindowBoundaryInnerObserver(WindowBoundaryMainObserver<T, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            if (!this.done) {
                this.parent.innerNext();
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.innerError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.innerComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowBoundary$WindowBoundaryMainObserver */
    static final class WindowBoundaryMainObserver<T, B> extends AtomicInteger implements Observer<T>, Disposable, Runnable {
        static final Object NEXT_WINDOW = new Object();
        private static final long serialVersionUID = 2233020065421370272L;
        final WindowBoundaryInnerObserver<T, B> boundaryObserver = new WindowBoundaryInnerObserver<>(this);
        final int capacityHint;
        volatile boolean done;
        final Observer<? super Observable<T>> downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        final MpscLinkedQueue<Object> queue = new MpscLinkedQueue<>();
        final AtomicBoolean stopWindows = new AtomicBoolean();
        final AtomicReference<Disposable> upstream = new AtomicReference<>();
        UnicastSubject<T> window;
        final AtomicInteger windows = new AtomicInteger(1);

        WindowBoundaryMainObserver(Observer<? super Observable<T>> downstream2, int capacityHint2) {
            this.downstream = downstream2;
            this.capacityHint = capacityHint2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this.upstream, d)) {
                innerNext();
            }
        }

        public void onNext(T t) {
            this.queue.offer(t);
            drain();
        }

        public void onError(Throwable e) {
            this.boundaryObserver.dispose();
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            this.boundaryObserver.dispose();
            this.done = true;
            drain();
        }

        public void dispose() {
            if (this.stopWindows.compareAndSet(false, true)) {
                this.boundaryObserver.dispose();
                if (this.windows.decrementAndGet() == 0) {
                    DisposableHelper.dispose(this.upstream);
                }
            }
        }

        public boolean isDisposed() {
            return this.stopWindows.get();
        }

        public void run() {
            if (this.windows.decrementAndGet() == 0) {
                DisposableHelper.dispose(this.upstream);
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerNext() {
            this.queue.offer(NEXT_WINDOW);
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable e) {
            DisposableHelper.dispose(this.upstream);
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            DisposableHelper.dispose(this.upstream);
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Observer<? super Observable<T>> downstream2 = this.downstream;
                MpscLinkedQueue<Object> queue2 = this.queue;
                AtomicThrowable errors2 = this.errors;
                while (this.windows.get() != 0) {
                    UnicastSubject<T> w = this.window;
                    boolean d = this.done;
                    if (!d || errors2.get() == null) {
                        Object v = queue2.poll();
                        boolean empty = v == null;
                        if (d && empty) {
                            Throwable ex = errors2.terminate();
                            if (ex == null) {
                                if (w != null) {
                                    this.window = null;
                                    w.onComplete();
                                }
                                downstream2.onComplete();
                            } else {
                                if (w != null) {
                                    this.window = null;
                                    w.onError(ex);
                                }
                                downstream2.onError(ex);
                            }
                            return;
                        } else if (empty) {
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else if (v != NEXT_WINDOW) {
                            w.onNext(v);
                        } else {
                            if (w != null) {
                                this.window = null;
                                w.onComplete();
                            }
                            if (!this.stopWindows.get()) {
                                UnicastSubject<T> w2 = UnicastSubject.create(this.capacityHint, this);
                                this.window = w2;
                                this.windows.getAndIncrement();
                                downstream2.onNext(w2);
                            }
                        }
                    } else {
                        queue2.clear();
                        Throwable ex2 = errors2.terminate();
                        if (w != null) {
                            this.window = null;
                            w.onError(ex2);
                        }
                        downstream2.onError(ex2);
                        return;
                    }
                }
                queue2.clear();
                this.window = null;
            }
        }
    }

    public ObservableWindowBoundary(ObservableSource<T> source, ObservableSource<B> other2, int capacityHint2) {
        super(source);
        this.other = other2;
        this.capacityHint = capacityHint2;
    }

    public void subscribeActual(Observer<? super Observable<T>> observer) {
        WindowBoundaryMainObserver<T, B> parent = new WindowBoundaryMainObserver<>(observer, this.capacityHint);
        observer.onSubscribe(parent);
        this.other.subscribe(parent.boundaryObserver);
        this.source.subscribe(parent);
    }
}
