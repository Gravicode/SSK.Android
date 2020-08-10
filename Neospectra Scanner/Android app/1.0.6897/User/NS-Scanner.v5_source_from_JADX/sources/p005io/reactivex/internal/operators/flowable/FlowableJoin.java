package p005io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableJoin */
public final class FlowableJoin<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AbstractFlowableWithUpstream<TLeft, R> {
    final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
    final Publisher<? extends TRight> other;
    final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
    final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableJoin$JoinSubscription */
    static final class JoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AtomicInteger implements Subscription, JoinSupport {
        static final Integer LEFT_CLOSE = Integer.valueOf(3);
        static final Integer LEFT_VALUE = Integer.valueOf(1);
        static final Integer RIGHT_CLOSE = Integer.valueOf(4);
        static final Integer RIGHT_VALUE = Integer.valueOf(2);
        private static final long serialVersionUID = -6071216598687999801L;
        final AtomicInteger active;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        final CompositeDisposable disposables = new CompositeDisposable();
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
        int leftIndex;
        final Map<Integer, TLeft> lefts = new LinkedHashMap();
        final SpscLinkedArrayQueue<Object> queue = new SpscLinkedArrayQueue<>(Flowable.bufferSize());
        final AtomicLong requested = new AtomicLong();
        final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
        final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
        int rightIndex;
        final Map<Integer, TRight> rights = new LinkedHashMap();

        JoinSubscription(Subscriber<? super R> actual2, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector2) {
            this.actual = actual2;
            this.leftEnd = leftEnd2;
            this.rightEnd = rightEnd2;
            this.resultSelector = resultSelector2;
            this.active = new AtomicInteger(2);
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            this.disposables.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Subscriber<?> a) {
            Throwable ex = ExceptionHelper.terminate(this.error);
            this.lefts.clear();
            this.rights.clear();
            a.onError(ex);
        }

        /* access modifiers changed from: 0000 */
        public void fail(Throwable exc, Subscriber<?> a, SimpleQueue<?> q) {
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
            Object obj;
            Throwable exc2;
            if (getAndIncrement() == 0) {
                int missed2 = 1;
                SpscLinkedArrayQueue<Object> q = this.queue;
                Subscriber<? super R> a = this.actual;
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
                            Object obj2 = val;
                            int idx = this.leftIndex;
                            this.leftIndex = idx + 1;
                            this.lefts.put(Integer.valueOf(idx), obj2);
                            try {
                                Publisher<TLeftEnd> p = (Publisher) ObjectHelper.requireNonNull(this.leftEnd.apply(obj2), "The leftEnd returned a null Publisher");
                                LeftRightEndSubscriber end = new LeftRightEndSubscriber(this, true, idx);
                                this.disposables.add(end);
                                p.subscribe(end);
                                Throwable ex = (Throwable) this.error.get();
                                if (ex != null) {
                                    q.clear();
                                    cancelAll();
                                    errorAll(a);
                                    return;
                                }
                                long r = this.requested.get();
                                Throwable th = ex;
                                boolean z = d;
                                long e = 0;
                                for (Object next : this.rights.values()) {
                                    try {
                                        int missed3 = missed2;
                                        Object obj3 = next;
                                        try {
                                            Object obj4 = obj3;
                                        } catch (Throwable th2) {
                                            Object obj5 = obj3;
                                            exc2 = th2;
                                            fail(exc2, a, q);
                                            return;
                                        }
                                        try {
                                            R w = ObjectHelper.requireNonNull(this.resultSelector.apply(obj2, obj3), "The resultSelector returned a null value");
                                            if (e != r) {
                                                a.onNext(w);
                                                e++;
                                                missed2 = missed3;
                                            } else {
                                                Object obj6 = w;
                                                ExceptionHelper.addThrowable(this.error, new MissingBackpressureException("Could not emit value due to lack of requests"));
                                                q.clear();
                                                cancelAll();
                                                errorAll(a);
                                                return;
                                            }
                                        } catch (Throwable th3) {
                                            exc2 = th3;
                                            fail(exc2, a, q);
                                            return;
                                        }
                                    } catch (Throwable th4) {
                                        int i = missed2;
                                        Object obj7 = next;
                                        exc2 = th4;
                                        fail(exc2, a, q);
                                        return;
                                    }
                                }
                                missed = missed2;
                                if (e != 0) {
                                    BackpressureHelper.produced(this.requested, e);
                                }
                            } catch (Throwable th5) {
                                int i2 = missed2;
                                boolean z2 = d;
                                fail(th5, a, q);
                                return;
                            }
                        } else {
                            missed = missed2;
                            boolean z3 = d;
                            if (mode == RIGHT_VALUE) {
                                Object obj8 = val;
                                int idx2 = this.rightIndex;
                                this.rightIndex = idx2 + 1;
                                this.rights.put(Integer.valueOf(idx2), obj8);
                                try {
                                    Publisher<TRightEnd> p2 = (Publisher) ObjectHelper.requireNonNull(this.rightEnd.apply(obj8), "The rightEnd returned a null Publisher");
                                    LeftRightEndSubscriber end2 = new LeftRightEndSubscriber(this, false, idx2);
                                    this.disposables.add(end2);
                                    p2.subscribe(end2);
                                    Throwable ex2 = (Throwable) this.error.get();
                                    if (ex2 != null) {
                                        q.clear();
                                        cancelAll();
                                        errorAll(a);
                                        return;
                                    }
                                    long r2 = this.requested.get();
                                    long e2 = 0;
                                    Throwable th6 = ex2;
                                    Iterator it = this.lefts.values().iterator();
                                    while (it.hasNext()) {
                                        Object next2 = it.next();
                                        Iterator it2 = it;
                                        try {
                                            int idx3 = idx2;
                                            Object obj9 = next2;
                                            try {
                                                obj = obj8;
                                            } catch (Throwable th7) {
                                                Object obj10 = obj8;
                                                Object obj11 = obj9;
                                                exc = th7;
                                                fail(exc, a, q);
                                                return;
                                            }
                                            try {
                                                R w2 = ObjectHelper.requireNonNull(this.resultSelector.apply(obj9, obj8), "The resultSelector returned a null value");
                                                if (e2 != r2) {
                                                    a.onNext(w2);
                                                    e2++;
                                                    it = it2;
                                                    idx2 = idx3;
                                                    obj8 = obj;
                                                } else {
                                                    Object obj12 = w2;
                                                    Object obj13 = obj9;
                                                    ExceptionHelper.addThrowable(this.error, new MissingBackpressureException("Could not emit value due to lack of requests"));
                                                    q.clear();
                                                    cancelAll();
                                                    errorAll(a);
                                                    return;
                                                }
                                            } catch (Throwable th8) {
                                                Object obj14 = obj9;
                                                exc = th8;
                                                fail(exc, a, q);
                                                return;
                                            }
                                        } catch (Throwable th9) {
                                            Object obj15 = obj8;
                                            int i3 = idx2;
                                            Object obj16 = next2;
                                            exc = th9;
                                            fail(exc, a, q);
                                            return;
                                        }
                                    }
                                    Object obj17 = obj8;
                                    int i4 = idx2;
                                    if (e2 != 0) {
                                        BackpressureHelper.produced(this.requested, e2);
                                    }
                                } catch (Throwable th10) {
                                    Object obj18 = obj8;
                                    int i5 = idx2;
                                    fail(th10, a, q);
                                    return;
                                }
                            } else if (mode == LEFT_CLOSE) {
                                LeftRightEndSubscriber end3 = (LeftRightEndSubscriber) val;
                                this.lefts.remove(Integer.valueOf(end3.index));
                                this.disposables.remove(end3);
                            } else if (mode == RIGHT_CLOSE) {
                                LeftRightEndSubscriber end4 = (LeftRightEndSubscriber) val;
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

        public void innerComplete(LeftRightSubscriber sender) {
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

        public void innerClose(boolean isLeft, LeftRightEndSubscriber index) {
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

    public FlowableJoin(Flowable<TLeft> source, Publisher<? extends TRight> other2, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector2) {
        super(source);
        this.other = other2;
        this.leftEnd = leftEnd2;
        this.rightEnd = rightEnd2;
        this.resultSelector = resultSelector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        JoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new JoinSubscription<>(s, this.leftEnd, this.rightEnd, this.resultSelector);
        s.onSubscribe(parent);
        LeftRightSubscriber left = new LeftRightSubscriber(parent, true);
        parent.disposables.add(left);
        LeftRightSubscriber right = new LeftRightSubscriber(parent, false);
        parent.disposables.add(right);
        this.source.subscribe((FlowableSubscriber<? super T>) left);
        this.other.subscribe(right);
    }
}
