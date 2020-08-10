package p008rx.internal.operators;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.FuncN;
import p008rx.observers.SerializedSubscriber;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorWithLatestFromMany */
public final class OperatorWithLatestFromMany<T, R> implements OnSubscribe<R> {
    final FuncN<R> combiner;
    final Observable<T> main;
    final Observable<?>[] others;
    final Iterable<Observable<?>> othersIterable;

    /* renamed from: rx.internal.operators.OperatorWithLatestFromMany$WithLatestMainSubscriber */
    static final class WithLatestMainSubscriber<T, R> extends Subscriber<T> {
        static final Object EMPTY = new Object();
        final Subscriber<? super R> actual;
        final FuncN<R> combiner;
        final AtomicReferenceArray<Object> current;
        boolean done;
        final AtomicInteger ready;

        public WithLatestMainSubscriber(Subscriber<? super R> actual2, FuncN<R> combiner2, int n) {
            this.actual = actual2;
            this.combiner = combiner2;
            AtomicReferenceArray<Object> array = new AtomicReferenceArray<>(n + 1);
            for (int i = 0; i <= n; i++) {
                array.lazySet(i, EMPTY);
            }
            this.current = array;
            this.ready = new AtomicInteger(n);
            request(0);
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.ready.get() == 0) {
                    AtomicReferenceArray<Object> array = this.current;
                    int n = array.length();
                    array.lazySet(0, t);
                    Object[] copy = new Object[array.length()];
                    for (int i = 0; i < n; i++) {
                        copy[i] = array.get(i);
                    }
                    try {
                        this.actual.onNext(this.combiner.call(copy));
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        onError(ex);
                    }
                } else {
                    request(1);
                }
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaHooks.onError(e);
                return;
            }
            this.done = true;
            unsubscribe();
            this.actual.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                unsubscribe();
                this.actual.onCompleted();
            }
        }

        public void setProducer(Producer p) {
            super.setProducer(p);
            this.actual.setProducer(p);
        }

        /* access modifiers changed from: 0000 */
        public void innerNext(int index, Object o) {
            if (this.current.getAndSet(index, o) == EMPTY) {
                this.ready.decrementAndGet();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(int index, Throwable e) {
            onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(int index) {
            if (this.current.get(index) == EMPTY) {
                onCompleted();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorWithLatestFromMany$WithLatestOtherSubscriber */
    static final class WithLatestOtherSubscriber extends Subscriber<Object> {
        final int index;
        final WithLatestMainSubscriber<?, ?> parent;

        public WithLatestOtherSubscriber(WithLatestMainSubscriber<?, ?> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onNext(Object t) {
            this.parent.innerNext(this.index, t);
        }

        public void onError(Throwable e) {
            this.parent.innerError(this.index, e);
        }

        public void onCompleted() {
            this.parent.innerComplete(this.index);
        }
    }

    public OperatorWithLatestFromMany(Observable<T> main2, Observable<?>[] others2, Iterable<Observable<?>> othersIterable2, FuncN<R> combiner2) {
        this.main = main2;
        this.others = others2;
        this.othersIterable = othersIterable2;
        this.combiner = combiner2;
    }

    public void call(Subscriber<? super R> t) {
        Observable<?>[] sources;
        SerializedSubscriber<R> serial = new SerializedSubscriber<>(t);
        int n = 0;
        if (this.others != null) {
            sources = this.others;
            n = sources.length;
        } else {
            sources = new Observable[8];
            for (Observable<?> o : this.othersIterable) {
                if (n == sources.length) {
                    sources = (Observable[]) Arrays.copyOf(sources, (n >> 2) + n);
                }
                int n2 = n + 1;
                sources[n] = o;
                n = n2;
            }
        }
        WithLatestMainSubscriber<T, R> parent = new WithLatestMainSubscriber<>(t, this.combiner, n);
        serial.add(parent);
        int i = 0;
        while (i < n) {
            if (!serial.isUnsubscribed()) {
                WithLatestOtherSubscriber inner = new WithLatestOtherSubscriber(parent, i + 1);
                parent.add(inner);
                sources[i].unsafeSubscribe(inner);
                i++;
            } else {
                return;
            }
        }
        this.main.unsafeSubscribe(parent);
    }
}
