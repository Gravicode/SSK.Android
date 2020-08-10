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
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.subjects.UnicastSubject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableGroupJoin */
public final class ObservableGroupJoin<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AbstractObservableWithUpstream<TLeft, R> {
    final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
    final ObservableSource<? extends TRight> other;
    final BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector;
    final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupJoin$GroupJoinDisposable */
    static final class GroupJoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AtomicInteger implements Disposable, JoinSupport {
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
        final Map<Integer, UnicastSubject<TRight>> lefts = new LinkedHashMap();
        final SpscLinkedArrayQueue<Object> queue = new SpscLinkedArrayQueue<>(Observable.bufferSize());
        final BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector;
        final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
        int rightIndex;
        final Map<Integer, TRight> rights = new LinkedHashMap();

        GroupJoinDisposable(Observer<? super R> actual2, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector2) {
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
            for (UnicastSubject<TRight> up : this.lefts.values()) {
                up.onError(ex);
            }
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
                    TRight mode = (Integer) q.poll();
                    boolean empty = mode == null;
                    if (d && empty) {
                        for (UnicastSubject<?> up : this.lefts.values()) {
                            up.onComplete();
                        }
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
                            UnicastSubject<TRight> up2 = UnicastSubject.create();
                            int idx = this.leftIndex;
                            this.leftIndex = idx + 1;
                            this.lefts.put(Integer.valueOf(idx), up2);
                            try {
                                ObservableSource<TLeftEnd> p = (ObservableSource) ObjectHelper.requireNonNull(this.leftEnd.apply(obj), "The leftEnd returned a null ObservableSource");
                                LeftRightEndObserver end = new LeftRightEndObserver(this, true, idx);
                                this.disposables.add(end);
                                p.subscribe(end);
                                if (((Throwable) this.error.get()) != null) {
                                    q.clear();
                                    cancelAll();
                                    errorAll(a);
                                    return;
                                }
                                try {
                                    missed = missed2;
                                    try {
                                        Object requireNonNull = ObjectHelper.requireNonNull(this.resultSelector.apply(obj, up2), "The resultSelector returned a null value");
                                        a.onNext(requireNonNull);
                                        for (R w : this.rights.values()) {
                                            Object obj2 = requireNonNull;
                                            up2.onNext(w);
                                            requireNonNull = obj2;
                                        }
                                    } catch (Throwable th) {
                                        exc = th;
                                        fail(exc, a, q);
                                        return;
                                    }
                                } catch (Throwable th2) {
                                    int i = missed2;
                                    exc = th2;
                                    fail(exc, a, q);
                                    return;
                                }
                            } catch (Throwable th3) {
                                int i2 = missed2;
                                fail(th3, a, q);
                                return;
                            }
                        } else {
                            missed = missed2;
                            if (mode == RIGHT_VALUE) {
                                Object obj3 = val;
                                int idx2 = this.rightIndex;
                                this.rightIndex = idx2 + 1;
                                this.rights.put(Integer.valueOf(idx2), obj3);
                                try {
                                    ObservableSource<TRightEnd> p2 = (ObservableSource) ObjectHelper.requireNonNull(this.rightEnd.apply(obj3), "The rightEnd returned a null ObservableSource");
                                    LeftRightEndObserver end2 = new LeftRightEndObserver(this, false, idx2);
                                    this.disposables.add(end2);
                                    p2.subscribe(end2);
                                    if (((Throwable) this.error.get()) != null) {
                                        q.clear();
                                        cancelAll();
                                        errorAll(a);
                                        return;
                                    }
                                    for (UnicastSubject<TRight> up3 : this.lefts.values()) {
                                        up3.onNext(obj3);
                                    }
                                } catch (Throwable th4) {
                                    fail(th4, a, q);
                                    return;
                                }
                            } else if (mode == LEFT_CLOSE) {
                                LeftRightEndObserver end3 = (LeftRightEndObserver) val;
                                UnicastSubject<TRight> up4 = (UnicastSubject) this.lefts.remove(Integer.valueOf(end3.index));
                                this.disposables.remove(end3);
                                if (up4 != null) {
                                    up4.onComplete();
                                }
                            } else if (mode == RIGHT_CLOSE) {
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

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupJoin$JoinSupport */
    interface JoinSupport {
        void innerClose(boolean z, LeftRightEndObserver leftRightEndObserver);

        void innerCloseError(Throwable th);

        void innerComplete(LeftRightObserver leftRightObserver);

        void innerError(Throwable th);

        void innerValue(boolean z, Object obj);
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupJoin$LeftRightEndObserver */
    static final class LeftRightEndObserver extends AtomicReference<Disposable> implements Observer<Object>, Disposable {
        private static final long serialVersionUID = 1883890389173668373L;
        final int index;
        final boolean isLeft;
        final JoinSupport parent;

        LeftRightEndObserver(JoinSupport parent2, boolean isLeft2, int index2) {
            this.parent = parent2;
            this.isLeft = isLeft2;
            this.index = index2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this, s);
        }

        public void onNext(Object t) {
            if (DisposableHelper.dispose(this)) {
                this.parent.innerClose(this.isLeft, this);
            }
        }

        public void onError(Throwable t) {
            this.parent.innerCloseError(t);
        }

        public void onComplete() {
            this.parent.innerClose(this.isLeft, this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupJoin$LeftRightObserver */
    static final class LeftRightObserver extends AtomicReference<Disposable> implements Observer<Object>, Disposable {
        private static final long serialVersionUID = 1883890389173668373L;
        final boolean isLeft;
        final JoinSupport parent;

        LeftRightObserver(JoinSupport parent2, boolean isLeft2) {
            this.parent = parent2;
            this.isLeft = isLeft2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this, s);
        }

        public void onNext(Object t) {
            this.parent.innerValue(this.isLeft, t);
        }

        public void onError(Throwable t) {
            this.parent.innerError(t);
        }

        public void onComplete() {
            this.parent.innerComplete(this);
        }
    }

    public ObservableGroupJoin(ObservableSource<TLeft> source, ObservableSource<? extends TRight> other2, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector2) {
        super(source);
        this.other = other2;
        this.leftEnd = leftEnd2;
        this.rightEnd = rightEnd2;
        this.resultSelector = resultSelector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> s) {
        GroupJoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new GroupJoinDisposable<>(s, this.leftEnd, this.rightEnd, this.resultSelector);
        s.onSubscribe(parent);
        LeftRightObserver left = new LeftRightObserver(parent, true);
        parent.disposables.add(left);
        LeftRightObserver right = new LeftRightObserver(parent, false);
        parent.disposables.add(right);
        this.source.subscribe(left);
        this.other.subscribe(right);
    }
}
