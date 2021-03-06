package p005io.reactivex.internal.operators.flowable;

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
import p005io.reactivex.disposables.Disposable;
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
import p005io.reactivex.processors.UnicastProcessor;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableGroupJoin */
public final class FlowableGroupJoin<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AbstractFlowableWithUpstream<TLeft, R> {
    final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
    final Publisher<? extends TRight> other;
    final BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector;
    final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableGroupJoin$GroupJoinSubscription */
    static final class GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AtomicInteger implements Subscription, JoinSupport {
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
        final Map<Integer, UnicastProcessor<TRight>> lefts = new LinkedHashMap();
        final SpscLinkedArrayQueue<Object> queue = new SpscLinkedArrayQueue<>(Flowable.bufferSize());
        final AtomicLong requested = new AtomicLong();
        final BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector;
        final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
        int rightIndex;
        final Map<Integer, TRight> rights = new LinkedHashMap();

        GroupJoinSubscription(Subscriber<? super R> actual2, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector2) {
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
            for (UnicastProcessor<TRight> up : this.lefts.values()) {
                up.onError(ex);
            }
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
                    TRight mode = (Integer) q.poll();
                    boolean empty = mode == null;
                    if (d && empty) {
                        for (UnicastProcessor<?> up : this.lefts.values()) {
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
                            UnicastProcessor<TRight> up2 = UnicastProcessor.create();
                            int idx = this.leftIndex;
                            this.leftIndex = idx + 1;
                            this.lefts.put(Integer.valueOf(idx), up2);
                            try {
                                Publisher<TLeftEnd> p = (Publisher) ObjectHelper.requireNonNull(this.leftEnd.apply(obj), "The leftEnd returned a null Publisher");
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
                                try {
                                    missed = missed2;
                                    try {
                                        R w = ObjectHelper.requireNonNull(this.resultSelector.apply(obj, up2), "The resultSelector returned a null value");
                                        if (this.requested.get() != 0) {
                                            a.onNext(w);
                                            Throwable th = ex;
                                            boolean z = d;
                                            BackpressureHelper.produced(this.requested, 1);
                                            for (TRight right : this.rights.values()) {
                                                up2.onNext(right);
                                            }
                                        } else {
                                            boolean z2 = d;
                                            fail(new MissingBackpressureException("Could not emit value due to lack of requests"), a, q);
                                            return;
                                        }
                                    } catch (Throwable th2) {
                                        Throwable th3 = ex;
                                        boolean z3 = d;
                                        exc = th2;
                                        fail(exc, a, q);
                                        return;
                                    }
                                } catch (Throwable th4) {
                                    int i = missed2;
                                    Throwable th5 = ex;
                                    boolean z4 = d;
                                    exc = th4;
                                    fail(exc, a, q);
                                    return;
                                }
                            } catch (Throwable th6) {
                                int i2 = missed2;
                                boolean z5 = d;
                                fail(th6, a, q);
                                return;
                            }
                        } else {
                            missed = missed2;
                            boolean z6 = d;
                            if (mode == RIGHT_VALUE) {
                                Object obj2 = val;
                                int idx2 = this.rightIndex;
                                this.rightIndex = idx2 + 1;
                                this.rights.put(Integer.valueOf(idx2), obj2);
                                try {
                                    Publisher<TRightEnd> p2 = (Publisher) ObjectHelper.requireNonNull(this.rightEnd.apply(obj2), "The rightEnd returned a null Publisher");
                                    LeftRightEndSubscriber end2 = new LeftRightEndSubscriber(this, false, idx2);
                                    this.disposables.add(end2);
                                    p2.subscribe(end2);
                                    if (((Throwable) this.error.get()) != null) {
                                        q.clear();
                                        cancelAll();
                                        errorAll(a);
                                        return;
                                    }
                                    for (UnicastProcessor<TRight> up3 : this.lefts.values()) {
                                        up3.onNext(obj2);
                                    }
                                } catch (Throwable th7) {
                                    fail(th7, a, q);
                                    return;
                                }
                            } else if (mode == LEFT_CLOSE) {
                                LeftRightEndSubscriber end3 = (LeftRightEndSubscriber) val;
                                UnicastProcessor<TRight> up4 = (UnicastProcessor) this.lefts.remove(Integer.valueOf(end3.index));
                                this.disposables.remove(end3);
                                if (up4 != null) {
                                    up4.onComplete();
                                }
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableGroupJoin$JoinSupport */
    interface JoinSupport {
        void innerClose(boolean z, LeftRightEndSubscriber leftRightEndSubscriber);

        void innerCloseError(Throwable th);

        void innerComplete(LeftRightSubscriber leftRightSubscriber);

        void innerError(Throwable th);

        void innerValue(boolean z, Object obj);
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableGroupJoin$LeftRightEndSubscriber */
    static final class LeftRightEndSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
        private static final long serialVersionUID = 1883890389173668373L;
        final int index;
        final boolean isLeft;
        final JoinSupport parent;

        LeftRightEndSubscriber(JoinSupport parent2, boolean isLeft2, int index2) {
            this.parent = parent2;
            this.isLeft = isLeft2;
            this.index = index2;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
        }

        public void onNext(Object t) {
            if (SubscriptionHelper.cancel(this)) {
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableGroupJoin$LeftRightSubscriber */
    static final class LeftRightSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
        private static final long serialVersionUID = 1883890389173668373L;
        final boolean isLeft;
        final JoinSupport parent;

        LeftRightSubscriber(JoinSupport parent2, boolean isLeft2) {
            this.parent = parent2;
            this.isLeft = isLeft2;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
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

    public FlowableGroupJoin(Flowable<TLeft> source, Publisher<? extends TRight> other2, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd2, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd2, BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector2) {
        super(source);
        this.other = other2;
        this.leftEnd = leftEnd2;
        this.rightEnd = rightEnd2;
        this.resultSelector = resultSelector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new GroupJoinSubscription<>(s, this.leftEnd, this.rightEnd, this.resultSelector);
        s.onSubscribe(parent);
        LeftRightSubscriber left = new LeftRightSubscriber(parent, true);
        parent.disposables.add(left);
        LeftRightSubscriber right = new LeftRightSubscriber(parent, false);
        parent.disposables.add(right);
        this.source.subscribe((FlowableSubscriber<? super T>) left);
        this.other.subscribe(right);
    }
}
