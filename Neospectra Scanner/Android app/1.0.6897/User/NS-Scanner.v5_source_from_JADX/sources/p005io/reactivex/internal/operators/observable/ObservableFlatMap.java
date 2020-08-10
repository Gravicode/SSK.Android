package p005io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
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
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMap */
public final class ObservableFlatMap<T, U> extends AbstractObservableWithUpstream<T, U> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
    final int maxConcurrency;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver */
    static final class InnerObserver<T, U> extends AtomicReference<Disposable> implements Observer<U> {
        private static final long serialVersionUID = -4606175640614850599L;
        volatile boolean done;
        int fusionMode;

        /* renamed from: id */
        final long f308id;
        final MergeObserver<T, U> parent;
        volatile SimpleQueue<U> queue;

        InnerObserver(MergeObserver<T, U> parent2, long id) {
            this.f308id = id;
            this.parent = parent2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.setOnce(this, s) && (s instanceof QueueDisposable)) {
                QueueDisposable<U> qd = (QueueDisposable) s;
                int m = qd.requestFusion(7);
                if (m == 1) {
                    this.fusionMode = m;
                    this.queue = qd;
                    this.done = true;
                    this.parent.drain();
                } else if (m == 2) {
                    this.fusionMode = m;
                    this.queue = qd;
                }
            }
        }

        public void onNext(U t) {
            if (this.fusionMode == 0) {
                this.parent.tryEmit(t, this);
            } else {
                this.parent.drain();
            }
        }

        public void onError(Throwable t) {
            if (this.parent.errors.addThrowable(t)) {
                if (!this.parent.delayErrors) {
                    this.parent.disposeAll();
                }
                this.done = true;
                this.parent.drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMap$MergeObserver */
    static final class MergeObserver<T, U> extends AtomicInteger implements Disposable, Observer<T> {
        static final InnerObserver<?, ?>[] CANCELLED = new InnerObserver[0];
        static final InnerObserver<?, ?>[] EMPTY = new InnerObserver[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Observer<? super U> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
        final int maxConcurrency;
        final AtomicReference<InnerObserver<?, ?>[]> observers;
        volatile SimplePlainQueue<U> queue;

        /* renamed from: s */
        Disposable f309s;
        Queue<ObservableSource<? extends U>> sources;
        long uniqueId;
        int wip;

        MergeObserver(Observer<? super U> actual2, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            this.maxConcurrency = maxConcurrency2;
            this.bufferSize = bufferSize2;
            if (maxConcurrency2 != Integer.MAX_VALUE) {
                this.sources = new ArrayDeque(maxConcurrency2);
            }
            this.observers = new AtomicReference<>(EMPTY);
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f309s, s)) {
                this.f309s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    ObservableSource<? extends U> p = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        synchronized (this) {
                            if (this.wip == this.maxConcurrency) {
                                this.sources.offer(p);
                                return;
                            }
                            this.wip++;
                        }
                    }
                    subscribeInner(p);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f309s.dispose();
                    onError(e);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeInner(ObservableSource<? extends U> p) {
            while (p instanceof Callable) {
                if (tryEmitScalar((Callable) p) && this.maxConcurrency != Integer.MAX_VALUE) {
                    boolean empty = false;
                    synchronized (this) {
                        p = (ObservableSource) this.sources.poll();
                        if (p == null) {
                            this.wip--;
                            empty = true;
                        }
                    }
                    if (empty) {
                        drain();
                        return;
                    }
                } else {
                    return;
                }
            }
            long j = this.uniqueId;
            this.uniqueId = 1 + j;
            InnerObserver<T, U> inner = new InnerObserver<>(this, j);
            if (addInner(inner)) {
                p.subscribe(inner);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean addInner(InnerObserver<T, U> inner) {
            InnerObserver<?, ?>[] a;
            InnerObserver<?, ?>[] b;
            do {
                a = (InnerObserver[]) this.observers.get();
                if (a == CANCELLED) {
                    inner.dispose();
                    return false;
                }
                int n = a.length;
                b = new InnerObserver[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = inner;
            } while (!this.observers.compareAndSet(a, b));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerObserver<T, U> inner) {
            InnerObserver<?, ?>[] a;
            InnerObserver<?, ?>[] b;
            do {
                a = (InnerObserver[]) this.observers.get();
                int n = a.length;
                if (n != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (a[i] == inner) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        if (n == 1) {
                            b = EMPTY;
                        } else {
                            InnerObserver<?, ?>[] b2 = new InnerObserver[(n - 1)];
                            System.arraycopy(a, 0, b2, 0, j);
                            System.arraycopy(a, j + 1, b2, j, (n - j) - 1);
                            b = b2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(a, b));
        }

        /* access modifiers changed from: 0000 */
        public boolean tryEmitScalar(Callable<? extends U> value) {
            try {
                U u = value.call();
                if (u == null) {
                    return true;
                }
                if (get() != 0 || !compareAndSet(0, 1)) {
                    SimplePlainQueue<U> q = this.queue;
                    if (q == null) {
                        if (this.maxConcurrency == Integer.MAX_VALUE) {
                            q = new SpscLinkedArrayQueue<>(this.bufferSize);
                        } else {
                            q = new SpscArrayQueue<>(this.maxConcurrency);
                        }
                        this.queue = q;
                    }
                    if (!q.offer(u)) {
                        onError(new IllegalStateException("Scalar queue full?!"));
                        return true;
                    } else if (getAndIncrement() != 0) {
                        return false;
                    }
                } else {
                    this.actual.onNext(u);
                    if (decrementAndGet() == 0) {
                        return true;
                    }
                }
                drainLoop();
                return true;
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.errors.addThrowable(ex);
                drain();
                return true;
            }
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(U value, InnerObserver<T, U> inner) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue<U> q = inner.queue;
                if (q == null) {
                    q = new SpscLinkedArrayQueue<>(this.bufferSize);
                    inner.queue = q;
                }
                q.offer(value);
                if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                this.actual.onNext(value);
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            if (this.errors.addThrowable(t)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(t);
            }
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
                if (disposeAll()) {
                    Throwable ex = this.errors.terminate();
                    if (ex != null && ex != ExceptionHelper.TERMINATED) {
                        RxJavaPlugins.onError(ex);
                    }
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x00d9, code lost:
            if (r15 != null) goto L_0x00c4;
         */
        /* JADX WARNING: Removed duplicated region for block: B:138:0x0128 A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x00bb  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r21 = this;
                r1 = r21
                io.reactivex.Observer<? super U> r2 = r1.actual
                r4 = 1
            L_0x0005:
                boolean r5 = r21.checkTerminate()
                if (r5 == 0) goto L_0x000c
                return
            L_0x000c:
                io.reactivex.internal.fuseable.SimplePlainQueue<U> r5 = r1.queue
                if (r5 == 0) goto L_0x0026
            L_0x0010:
                boolean r6 = r21.checkTerminate()
                if (r6 == 0) goto L_0x0017
                return
            L_0x0017:
                java.lang.Object r6 = r5.poll()
                if (r6 != 0) goto L_0x0022
                if (r6 != 0) goto L_0x0021
                goto L_0x0026
            L_0x0021:
                goto L_0x0010
            L_0x0022:
                r2.onNext(r6)
                goto L_0x0010
            L_0x0026:
                boolean r6 = r1.done
                io.reactivex.internal.fuseable.SimplePlainQueue<U> r5 = r1.queue
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver<?, ?>[]> r7 = r1.observers
                java.lang.Object r7 = r7.get()
                io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver[] r7 = (p005io.reactivex.internal.operators.observable.ObservableFlatMap.InnerObserver[]) r7
                int r8 = r7.length
                r9 = 0
                int r10 = r1.maxConcurrency
                r11 = 2147483647(0x7fffffff, float:NaN)
                if (r10 == r11) goto L_0x0049
                monitor-enter(r21)
                java.util.Queue<io.reactivex.ObservableSource<? extends U>> r10 = r1.sources     // Catch:{ all -> 0x0045 }
                int r10 = r10.size()     // Catch:{ all -> 0x0045 }
                r9 = r10
                monitor-exit(r21)     // Catch:{ all -> 0x0045 }
                goto L_0x0049
            L_0x0045:
                r0 = move-exception
                r3 = r0
                monitor-exit(r21)     // Catch:{ all -> 0x0045 }
                throw r3
            L_0x0049:
                if (r6 == 0) goto L_0x006b
                if (r5 == 0) goto L_0x0053
                boolean r10 = r5.isEmpty()
                if (r10 == 0) goto L_0x006b
            L_0x0053:
                if (r8 != 0) goto L_0x006b
                if (r9 != 0) goto L_0x006b
                io.reactivex.internal.util.AtomicThrowable r3 = r1.errors
                java.lang.Throwable r3 = r3.terminate()
                java.lang.Throwable r10 = p005io.reactivex.internal.util.ExceptionHelper.TERMINATED
                if (r3 == r10) goto L_0x006a
                if (r3 != 0) goto L_0x0067
                r2.onComplete()
                goto L_0x006a
            L_0x0067:
                r2.onError(r3)
            L_0x006a:
                return
            L_0x006b:
                r10 = 0
                if (r8 == 0) goto L_0x0131
                long r12 = r1.lastId
                int r14 = r1.lastIndex
                if (r8 <= r14) goto L_0x0084
                r15 = r7[r14]
                r17 = r4
                long r3 = r15.f308id
                int r3 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1))
                if (r3 == 0) goto L_0x007f
                goto L_0x0086
            L_0x007f:
                r19 = r5
                r18 = r6
                goto L_0x00b4
            L_0x0084:
                r17 = r4
            L_0x0086:
                if (r8 > r14) goto L_0x0089
                r14 = 0
            L_0x0089:
                r3 = r14
                r4 = r3
                r3 = 0
            L_0x008c:
                if (r3 >= r8) goto L_0x00a7
                r15 = r7[r4]
                r19 = r5
                r18 = r6
                long r5 = r15.f308id
                int r5 = (r5 > r12 ? 1 : (r5 == r12 ? 0 : -1))
                if (r5 != 0) goto L_0x009b
                goto L_0x00ab
            L_0x009b:
                int r4 = r4 + 1
                if (r4 != r8) goto L_0x00a0
                r4 = 0
            L_0x00a0:
                int r3 = r3 + 1
                r6 = r18
                r5 = r19
                goto L_0x008c
            L_0x00a7:
                r19 = r5
                r18 = r6
            L_0x00ab:
                r14 = r4
                r1.lastIndex = r4
                r3 = r7[r4]
                long r5 = r3.f308id
                r1.lastId = r5
            L_0x00b4:
                r3 = r14
                r16 = 0
            L_0x00b7:
                r4 = r16
                if (r4 >= r8) goto L_0x0128
                boolean r5 = r21.checkTerminate()
                if (r5 == 0) goto L_0x00c2
                return
            L_0x00c2:
                r5 = r7[r3]
            L_0x00c4:
                boolean r6 = r21.checkTerminate()
                if (r6 == 0) goto L_0x00cb
                return
            L_0x00cb:
                io.reactivex.internal.fuseable.SimpleQueue<U> r6 = r5.queue
                if (r6 != 0) goto L_0x00d0
                goto L_0x00dc
            L_0x00d0:
                java.lang.Object r15 = r6.poll()     // Catch:{ Throwable -> 0x0106 }
                if (r15 != 0) goto L_0x00fc
                if (r15 != 0) goto L_0x00fb
            L_0x00dc:
                boolean r6 = r5.done
                io.reactivex.internal.fuseable.SimpleQueue<U> r15 = r5.queue
                if (r6 == 0) goto L_0x00f5
                if (r15 == 0) goto L_0x00ea
                boolean r16 = r15.isEmpty()
                if (r16 == 0) goto L_0x00f5
            L_0x00ea:
                r1.removeInner(r5)
                boolean r16 = r21.checkTerminate()
                if (r16 == 0) goto L_0x00f4
                return
            L_0x00f4:
                r10 = 1
            L_0x00f5:
                int r3 = r3 + 1
                if (r3 != r8) goto L_0x0121
                r3 = 0
                goto L_0x0121
            L_0x00fb:
                goto L_0x00c4
            L_0x00fc:
                r2.onNext(r15)
                boolean r16 = r21.checkTerminate()
                if (r16 == 0) goto L_0x00d0
                return
            L_0x0106:
                r0 = move-exception
                r15 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r15)
                r5.dispose()
                io.reactivex.internal.util.AtomicThrowable r11 = r1.errors
                r11.addThrowable(r15)
                boolean r11 = r21.checkTerminate()
                if (r11 == 0) goto L_0x011a
                return
            L_0x011a:
                r1.removeInner(r5)
                r10 = 1
                int r4 = r4 + 1
            L_0x0121:
                r5 = 1
                int r16 = r4 + 1
                r11 = 2147483647(0x7fffffff, float:NaN)
                goto L_0x00b7
            L_0x0128:
                r1.lastIndex = r3
                r4 = r7[r3]
                long r4 = r4.f308id
                r1.lastId = r4
                goto L_0x0137
            L_0x0131:
                r17 = r4
                r19 = r5
                r18 = r6
            L_0x0137:
                if (r10 == 0) goto L_0x0162
                int r3 = r1.maxConcurrency
                r4 = 2147483647(0x7fffffff, float:NaN)
                if (r3 == r4) goto L_0x015d
                monitor-enter(r21)
                java.util.Queue<io.reactivex.ObservableSource<? extends U>> r3 = r1.sources     // Catch:{ all -> 0x0159 }
                java.lang.Object r3 = r3.poll()     // Catch:{ all -> 0x0159 }
                io.reactivex.ObservableSource r3 = (p005io.reactivex.ObservableSource) r3     // Catch:{ all -> 0x0159 }
                if (r3 != 0) goto L_0x0153
                int r4 = r1.wip     // Catch:{ all -> 0x0159 }
                r5 = 1
                int r4 = r4 - r5
                r1.wip = r4     // Catch:{ all -> 0x0159 }
                monitor-exit(r21)     // Catch:{ all -> 0x0159 }
                goto L_0x015e
            L_0x0153:
                r5 = 1
                monitor-exit(r21)     // Catch:{ all -> 0x0159 }
                r1.subscribeInner(r3)
                goto L_0x015e
            L_0x0159:
                r0 = move-exception
                r3 = r0
                monitor-exit(r21)     // Catch:{ all -> 0x0159 }
                throw r3
            L_0x015d:
                r5 = 1
            L_0x015e:
                r4 = r17
                goto L_0x0005
            L_0x0162:
                r5 = 1
                r3 = r17
                int r4 = -r3
                int r4 = r1.addAndGet(r4)
                if (r4 != 0) goto L_0x016e
                return
            L_0x016e:
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.observable.ObservableFlatMap.MergeObserver.drainLoop():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.cancelled) {
                return true;
            }
            Throwable e = (Throwable) this.errors.get();
            if (this.delayErrors || e == null) {
                return false;
            }
            disposeAll();
            Throwable e2 = this.errors.terminate();
            if (e2 != ExceptionHelper.TERMINATED) {
                this.actual.onError(e2);
            }
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean disposeAll() {
            this.f309s.dispose();
            if (((InnerObserver[]) this.observers.get()) != CANCELLED) {
                InnerObserver<?, ?>[] a = (InnerObserver[]) this.observers.getAndSet(CANCELLED);
                if (a != CANCELLED) {
                    for (InnerObserver<?, ?> inner : a) {
                        inner.dispose();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public ObservableFlatMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
        this.maxConcurrency = maxConcurrency2;
        this.bufferSize = bufferSize2;
    }

    public void subscribeActual(Observer<? super U> t) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, t, this.mapper)) {
            ObservableSource observableSource = this.source;
            MergeObserver mergeObserver = new MergeObserver(t, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize);
            observableSource.subscribe(mergeObserver);
        }
    }
}
