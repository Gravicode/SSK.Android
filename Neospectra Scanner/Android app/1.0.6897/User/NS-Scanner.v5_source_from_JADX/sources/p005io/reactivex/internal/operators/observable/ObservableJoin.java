package p005io.reactivex.internal.operators.observable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableJoin */
public final class ObservableJoin<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AbstractObservableWithUpstream<TLeft, R> {
    final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
    final ObservableSource<? extends TRight> other;
    final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
    final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableJoin$JoinDisposable */
    static final class JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AtomicInteger implements Disposable, JoinSupport {
        static final Integer LEFT_CLOSE = Integer.valueOf(3);
        static final Integer LEFT_VALUE = Integer.valueOf(1);
        static final Integer RIGHT_CLOSE = Integer.valueOf(4);
        static final Integer RIGHT_VALUE = Integer.valueOf(2);
        private static final long serialVersionUID = -6071216598687999801L;
        final AtomicInteger active;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        final CompositeDisposable disposables = new CompositeDisposable();
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
        int leftIndex;
        final Map<Integer, TLeft> lefts = new LinkedHashMap();
        final SpscLinkedArrayQueue<Object> queue = new SpscLinkedArrayQueue<>(Observable.bufferSize());
        final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
        final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
        int rightIndex;
        final Map<Integer, TRight> rights = new LinkedHashMap();

        JoinDisposable(Observer<? super R> actual2, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector2) {
            this.actual = actual2;
            this.leftEnd = leftEnd2;
            this.rightEnd = rightEnd2;
            this.resultSelector = resultSelector2;
            this.active = new AtomicInteger(2);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            this.disposables.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Observer<?> a) {
            Throwable ex = ExceptionHelper.terminate(this.error);
            this.lefts.clear();
            this.rights.clear();
            a.onError(ex);
        }

        /* access modifiers changed from: 0000 */
        public void fail(Throwable exc, Observer<?> a, SpscLinkedArrayQueue<?> q) {
            Exceptions.throwIfFatal(exc);
            ExceptionHelper.addThrowable(this.error, exc);
            q.clear();
            cancelAll();
            errorAll(a);
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            int missed;
            Throwable exc;
            Throwable exc2;
            if (getAndIncrement() == 0) {
                int missed2 = 1;
                SpscLinkedArrayQueue<Object> q = this.queue;
                Observer<? super R> a = this.actual;
                while (!this.cancelled) {
                    if (((Throwable) this.error.get()) != null) {
                        q.clear();
                        cancelAll();
                        errorAll(a);
                        return;
                    }
                    boolean d = this.active.get() == 0;
                    Integer mode = (Integer) q.poll();
                    boolean empty = mode == null;
                    if (d && empty) {
                        this.lefts.clear();
                        this.rights.clear();
                        this.disposables.dispose();
                        a.onComplete();
                        return;
                    } else if (empty) {
                        missed2 = addAndGet(-missed2);
                        if (missed2 == 0) {
                            return;
                        }
                    } else {
                        Object val = q.poll();
                        if (mode == LEFT_VALUE) {
                            Object obj = val;
                            int idx = this.leftIndex;
                            this.leftIndex = idx + 1;
                            this.lefts.put(Integer.valueOf(idx), obj);
                            try {
                                ObservableSource<TLeftEnd> p = (ObservableSource) ObjectHelper.requireNonNull(this.leftEnd.apply(obj), "The leftEnd returned a null ObservableSource");
                                LeftRightEndObserver end = new LeftRightEndObserver(this, true, idx);
                                this.disposables.add(end);
                                p.subscribe(end);
                                Throwable ex = (Throwable) this.error.get();
                                if (ex != null) {
                                    q.clear();
                                    cancelAll();
                                    errorAll(a);
                                    return;
                                }
                                for (TRight right : this.rights.values()) {
                                    int missed3 = missed2;
                                    try {
                                        Throwable ex2 = ex;
                                        try {
                                            a.onNext(ObjectHelper.requireNonNull(this.resultSelector.apply(obj, right), "The resultSelector returned a null value"));
                                            missed2 = missed3;
                                            ex = ex2;
                                        } catch (Throwable th) {
                                            exc2 = th;
                                            fail(exc2, a, q);
                                            return;
                                        }
                                    } catch (Throwable th2) {
                                        Throwable th3 = ex;
                                        exc2 = th2;
                                        fail(exc2, a, q);
                                        return;
                                    }
                                }
                                missed = missed2;
                                Throwable th4 = ex;
                            } catch (Throwable th5) {
                                int i = missed2;
                                fail(th5, a, q);
                                return;
                            }
                        } else {
                            missed = missed2;
                            if (mode == RIGHT_VALUE) {
                                Object obj2 = val;
                                int idx2 = this.rightIndex;
                                this.rightIndex = idx2 + 1;
                                this.rights.put(Integer.valueOf(idx2), obj2);
                                try {
                                    ObservableSource<TRightEnd> p2 = (ObservableSource) ObjectHelper.requireNonNull(this.rightEnd.apply(obj2), "The rightEnd returned a null ObservableSource");
                                    LeftRightEndObserver end2 = new LeftRightEndObserver(this, false, idx2);
                                    this.disposables.add(end2);
                                    p2.subscribe(end2);
                                    if (((Throwable) this.error.get()) != null) {
                                        q.clear();
                                        cancelAll();
                                        errorAll(a);
                                        return;
                                    }
                                    for (TLeft left : this.lefts.values()) {
                                        try {
                                            Object obj3 = obj2;
                                            try {
                                                a.onNext(ObjectHelper.requireNonNull(this.resultSelector.apply(left, obj2), "The resultSelector returned a null value"));
                                                obj2 = obj3;
                                            } catch (Throwable th6) {
                                                exc = th6;
                                                fail(exc, a, q);
                                                return;
                                            }
                                        } catch (Throwable th7) {
                                            Object obj4 = obj2;
                                            exc = th7;
                                            fail(exc, a, q);
                                            return;
                                        }
                                    }
                                } catch (Throwable th8) {
                                    Object obj5 = obj2;
                                    fail(th8, a, q);
                                    return;
                                }
                            } else if (mode == LEFT_CLOSE) {
                                LeftRightEndObserver end3 = (LeftRightEndObserver) val;
                                this.lefts.remove(Integer.valueOf(end3.index));
                                this.disposables.remove(end3);
                            } else {
                                LeftRightEndObserver end4 = (LeftRightEndObserver) val;
                                this.rights.remove(Integer.valueOf(end4.index));
                                this.disposables.remove(end4);
                            }
                        }
                        missed2 = missed;
                    }
                }
                q.clear();
            }
        }

        public void innerError(Throwable ex) {
            if (ExceptionHelper.addThrowable(this.error, ex)) {
                this.active.decrementAndGet();
                drain();
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void innerComplete(LeftRightObserver sender) {
            this.disposables.delete(sender);
            this.active.decrementAndGet();
            drain();
        }

        public void innerValue(boolean isLeft, Object o) {
            synchronized (this) {
                this.queue.offer(isLeft ? LEFT_VALUE : RIGHT_VALUE, o);
            }
            drain();
        }

        public void innerClose(boolean isLeft, LeftRightEndObserver index) {
            synchronized (this) {
                this.queue.offer(isLeft ? LEFT_CLOSE : RIGHT_CLOSE, index);
            }
            drain();
        }

        public void innerCloseError(Throwable ex) {
            if (ExceptionHelper.addThrowable(this.error, ex)) {
                drain();
            } else {
                RxJavaPlugins.onError(ex);
            }
        }
    }

    public ObservableJoin(ObservableSource<TLeft> source, ObservableSource<? extends TRight> other2, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector2) {
        super(source);
        this.other = other2;
        this.leftEnd = leftEnd2;
        this.rightEnd = rightEnd2;
        this.resultSelector = resultSelector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> s) {
        JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new JoinDisposable<>(s, this.leftEnd, this.rightEnd, this.resultSelector);
        s.onSubscribe(parent);
        LeftRightObserver left = new LeftRightObserver(parent, true);
        parent.disposables.add(left);
        LeftRightObserver right = new LeftRightObserver(parent, false);
        parent.disposables.add(right);
        this.source.subscribe(left);
        this.other.subscribe(right);
    }
}
