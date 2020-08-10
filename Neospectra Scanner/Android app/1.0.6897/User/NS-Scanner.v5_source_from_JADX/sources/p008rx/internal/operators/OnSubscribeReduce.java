package p008rx.internal.operators;

import java.util.NoSuchElementException;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func2;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeReduce */
public final class OnSubscribeReduce<T> implements OnSubscribe<T> {
    final Func2<T, T, T> reducer;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeReduce$ReduceSubscriber */
    static final class ReduceSubscriber<T> extends Subscriber<T> {
        static final Object EMPTY = new Object();
        final Subscriber<? super T> actual;
        boolean done;
        final Func2<T, T, T> reducer;
        T value = EMPTY;

        public ReduceSubscriber(Subscriber<? super T> actual2, Func2<T, T, T> reducer2) {
            this.actual = actual2;
            this.reducer = reducer2;
            request(0);
        }

        public void onNext(T t) {
            if (!this.done) {
                Object o = this.value;
                if (o == EMPTY) {
                    this.value = t;
                } else {
                    try {
                        this.value = this.reducer.call(o, t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        unsubscribe();
                        onError(ex);
                    }
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                this.actual.onError(e);
                return;
            }
            RxJavaHooks.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                Object o = this.value;
                if (o != EMPTY) {
                    this.actual.onNext(o);
                    this.actual.onCompleted();
                } else {
                    this.actual.onError(new NoSuchElementException());
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void downstreamRequest(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            } else if (n != 0) {
                request(Long.MAX_VALUE);
            }
        }
    }

    public OnSubscribeReduce(Observable<T> source2, Func2<T, T, T> reducer2) {
        this.source = source2;
        this.reducer = reducer2;
    }

    public void call(Subscriber<? super T> t) {
        final ReduceSubscriber<T> parent = new ReduceSubscriber<>(t, this.reducer);
        t.add(parent);
        t.setProducer(new Producer() {
            public void request(long n) {
                parent.downstreamRequest(n);
            }
        });
        this.source.unsafeSubscribe(parent);
    }
}
