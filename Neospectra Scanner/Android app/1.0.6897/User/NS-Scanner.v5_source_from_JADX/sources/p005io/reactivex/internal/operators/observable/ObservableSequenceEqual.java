package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiPredicate;
import p005io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSequenceEqual */
public final class ObservableSequenceEqual<T> extends Observable<Boolean> {
    final int bufferSize;
    final BiPredicate<? super T, ? super T> comparer;
    final ObservableSource<? extends T> first;
    final ObservableSource<? extends T> second;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSequenceEqual$EqualCoordinator */
    static final class EqualCoordinator<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -6178010334400373240L;
        final Observer<? super Boolean> actual;
        volatile boolean cancelled;
        final BiPredicate<? super T, ? super T> comparer;
        final ObservableSource<? extends T> first;
        final EqualObserver<T>[] observers;
        final ArrayCompositeDisposable resources = new ArrayCompositeDisposable(2);
        final ObservableSource<? extends T> second;

        /* renamed from: v1 */
        T f344v1;

        /* renamed from: v2 */
        T f345v2;

        EqualCoordinator(Observer<? super Boolean> actual2, int bufferSize, ObservableSource<? extends T> first2, ObservableSource<? extends T> second2, BiPredicate<? super T, ? super T> comparer2) {
            this.actual = actual2;
            this.first = first2;
            this.second = second2;
            this.comparer = comparer2;
            EqualObserver<T>[] as = new EqualObserver[2];
            this.observers = as;
            as[0] = new EqualObserver<>(this, 0, bufferSize);
            as[1] = new EqualObserver<>(this, 1, bufferSize);
        }

        /* access modifiers changed from: 0000 */
        public boolean setDisposable(Disposable s, int index) {
            return this.resources.setResource(index, s);
        }

        /* access modifiers changed from: 0000 */
        public void subscribe() {
            EqualObserver<T>[] as = this.observers;
            this.first.subscribe(as[0]);
            this.second.subscribe(as[1]);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.resources.dispose();
                if (getAndIncrement() == 0) {
                    EqualObserver<T>[] as = this.observers;
                    as[0].queue.clear();
                    as[1].queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void cancel(SpscLinkedArrayQueue<T> q1, SpscLinkedArrayQueue<T> q2) {
            this.cancelled = true;
            q1.clear();
            q2.clear();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                EqualObserver<T>[] as = this.observers;
                EqualObserver<T> s1 = as[0];
                SpscLinkedArrayQueue<T> q1 = s1.queue;
                EqualObserver<T> s2 = as[1];
                SpscLinkedArrayQueue<T> q2 = s2.queue;
                while (!this.cancelled) {
                    boolean d1 = s1.done;
                    if (d1) {
                        Throwable e = s1.error;
                        if (e != null) {
                            cancel(q1, q2);
                            this.actual.onError(e);
                            return;
                        }
                    }
                    boolean d2 = s2.done;
                    if (d2) {
                        Throwable e2 = s2.error;
                        if (e2 != null) {
                            cancel(q1, q2);
                            this.actual.onError(e2);
                            return;
                        }
                    }
                    if (this.f344v1 == null) {
                        this.f344v1 = q1.poll();
                    }
                    boolean e1 = this.f344v1 == null;
                    if (this.f345v2 == null) {
                        this.f345v2 = q2.poll();
                    }
                    boolean e22 = this.f345v2 == null;
                    if (d1 && d2 && e1 && e22) {
                        this.actual.onNext(Boolean.valueOf(true));
                        this.actual.onComplete();
                        return;
                    } else if (!d1 || !d2 || e1 == e22) {
                        if (!e1 && !e22) {
                            try {
                                if (!this.comparer.test(this.f344v1, this.f345v2)) {
                                    cancel(q1, q2);
                                    this.actual.onNext(Boolean.valueOf(false));
                                    this.actual.onComplete();
                                    return;
                                }
                                this.f344v1 = null;
                                this.f345v2 = null;
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                cancel(q1, q2);
                                this.actual.onError(ex);
                                return;
                            }
                        }
                        if (e1 || e22) {
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        }
                    } else {
                        cancel(q1, q2);
                        this.actual.onNext(Boolean.valueOf(false));
                        this.actual.onComplete();
                        return;
                    }
                }
                q1.clear();
                q2.clear();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSequenceEqual$EqualObserver */
    static final class EqualObserver<T> implements Observer<T> {
        volatile boolean done;
        Throwable error;
        final int index;
        final EqualCoordinator<T> parent;
        final SpscLinkedArrayQueue<T> queue;

        EqualObserver(EqualCoordinator<T> parent2, int index2, int bufferSize) {
            this.parent = parent2;
            this.index = index2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
        }

        public void onSubscribe(Disposable s) {
            this.parent.setDisposable(s, this.index);
        }

        public void onNext(T t) {
            this.queue.offer(t);
            this.parent.drain();
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            this.parent.drain();
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }
    }

    public ObservableSequenceEqual(ObservableSource<? extends T> first2, ObservableSource<? extends T> second2, BiPredicate<? super T, ? super T> comparer2, int bufferSize2) {
        this.first = first2;
        this.second = second2;
        this.comparer = comparer2;
        this.bufferSize = bufferSize2;
    }

    public void subscribeActual(Observer<? super Boolean> s) {
        EqualCoordinator<T> ec = new EqualCoordinator<>(s, this.bufferSize, this.first, this.second, this.comparer);
        s.onSubscribe(ec);
        ec.subscribe();
    }
}
