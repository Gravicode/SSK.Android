package p005io.reactivex.internal.operators.flowable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.subscribers.QueueDrainSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.processors.UnicastProcessor;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed */
public final class FlowableWindowTimed<T> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
    final int bufferSize;
    final long maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed$WindowExactBoundedSubscriber */
    static final class WindowExactBoundedSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription {
        final int bufferSize;
        long count;
        final long maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;

        /* renamed from: s */
        Subscription f232s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final SequentialDisposable timer = new SequentialDisposable();
        final long timespan;
        final TimeUnit unit;
        UnicastProcessor<T> window;
        final Worker worker;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed$WindowExactBoundedSubscriber$ConsumerIndexHolder */
        static final class ConsumerIndexHolder implements Runnable {
            final long index;
            final WindowExactBoundedSubscriber<?> parent;

            ConsumerIndexHolder(long index2, WindowExactBoundedSubscriber<?> parent2) {
                this.index = index2;
                this.parent = parent2;
            }

            public void run() {
                WindowExactBoundedSubscriber<?> p = this.parent;
                if (!p.cancelled) {
                    p.queue.offer(this);
                } else {
                    p.terminated = true;
                    p.dispose();
                }
                if (p.enter()) {
                    p.drainLoop();
                }
            }
        }

        WindowExactBoundedSubscriber(Subscriber<? super Flowable<T>> actual, long timespan2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2, long maxSize2, boolean restartTimerOnMaxSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.bufferSize = bufferSize2;
            this.maxSize = maxSize2;
            this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
            if (restartTimerOnMaxSize2) {
                this.worker = scheduler2.createWorker();
            } else {
                this.worker = null;
            }
        }

        public void onSubscribe(Subscription s) {
            Disposable d;
            Subscription subscription = s;
            if (SubscriptionHelper.validate(this.f232s, subscription)) {
                this.f232s = subscription;
                Subscriber<? super Flowable<T>> a = this.actual;
                a.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize);
                    this.window = w;
                    long r = requested();
                    if (r != 0) {
                        a.onNext(w);
                        if (r != Long.MAX_VALUE) {
                            produced(1);
                        }
                        ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
                        if (this.restartTimerOnMaxSize) {
                            d = this.worker.schedulePeriodically(consumerIndexHolder, this.timespan, this.timespan, this.unit);
                        } else {
                            d = this.scheduler.schedulePeriodicallyDirect(consumerIndexHolder, this.timespan, this.timespan, this.unit);
                        }
                        if (this.timer.replace(d)) {
                            subscription.request(Long.MAX_VALUE);
                        }
                    } else {
                        this.cancelled = true;
                        s.cancel();
                        a.onError(new MissingBackpressureException("Could not deliver initial window due to lack of requests."));
                    }
                }
            }
        }

        public void onNext(T t) {
            if (!this.terminated) {
                if (fastEnter()) {
                    UnicastProcessor<T> w = this.window;
                    w.onNext(t);
                    long c = this.count + 1;
                    if (c >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        w.onComplete();
                        long r = requested();
                        if (r != 0) {
                            UnicastProcessor<T> w2 = UnicastProcessor.create(this.bufferSize);
                            this.window = w2;
                            this.actual.onNext(w2);
                            if (r != Long.MAX_VALUE) {
                                produced(1);
                            }
                            if (this.restartTimerOnMaxSize) {
                                ((Disposable) this.timer.get()).dispose();
                                this.timer.replace(this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit));
                            }
                        } else {
                            this.window = null;
                            this.f232s.cancel();
                            this.actual.onError(new MissingBackpressureException("Could not deliver window due to lack of requests"));
                            dispose();
                            return;
                        }
                    } else {
                        this.count = c;
                    }
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    T t2 = t;
                    this.queue.offer(NotificationLite.next(t));
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(t);
            dispose();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            dispose();
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            Worker w = this.worker;
            if (w != null) {
                w.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            int missed;
            Subscriber subscriber;
            SimplePlainQueue simplePlainQueue;
            SimplePlainQueue simplePlainQueue2 = this.queue;
            Subscriber subscriber2 = this.actual;
            UnicastProcessor<T> w = this.window;
            int missed2 = 1;
            while (!this.terminated) {
                boolean d = this.done;
                Object o = simplePlainQueue2.poll();
                boolean empty = o == null;
                boolean isHolder = o instanceof ConsumerIndexHolder;
                if (d && (empty || isHolder)) {
                    this.window = null;
                    simplePlainQueue2.clear();
                    Throwable err = this.error;
                    if (err != null) {
                        w.onError(err);
                    } else {
                        w.onComplete();
                    }
                    dispose();
                    return;
                } else if (empty) {
                    missed2 = leave(-missed2);
                    if (missed2 == 0) {
                        return;
                    }
                } else if (isHolder) {
                    ConsumerIndexHolder consumerIndexHolder = (ConsumerIndexHolder) o;
                    if (this.restartTimerOnMaxSize || this.producerIndex == consumerIndexHolder.index) {
                        w.onComplete();
                        this.count = 0;
                        w = UnicastProcessor.create(this.bufferSize);
                        this.window = w;
                        long r = requested();
                        if (r != 0) {
                            subscriber2.onNext(w);
                            if (r != Long.MAX_VALUE) {
                                produced(1);
                            }
                        } else {
                            this.window = null;
                            this.queue.clear();
                            this.f232s.cancel();
                            subscriber2.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests."));
                            dispose();
                            return;
                        }
                    }
                } else {
                    w.onNext(NotificationLite.getValue(o));
                    long c = this.count + 1;
                    if (c >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        w.onComplete();
                        long r2 = requested();
                        if (r2 != 0) {
                            UnicastProcessor<T> w2 = UnicastProcessor.create(this.bufferSize);
                            this.window = w2;
                            this.actual.onNext(w2);
                            if (r2 != Long.MAX_VALUE) {
                                produced(1);
                            }
                            if (this.restartTimerOnMaxSize) {
                                ((Disposable) this.timer.get()).dispose();
                                simplePlainQueue = simplePlainQueue2;
                                subscriber = subscriber2;
                                missed = missed2;
                                this.timer.replace(this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit));
                            } else {
                                simplePlainQueue = simplePlainQueue2;
                                subscriber = subscriber2;
                                missed = missed2;
                            }
                            w = w2;
                        } else {
                            SimplePlainQueue simplePlainQueue3 = simplePlainQueue2;
                            Subscriber subscriber3 = subscriber2;
                            int i = missed2;
                            this.window = null;
                            this.f232s.cancel();
                            this.actual.onError(new MissingBackpressureException("Could not deliver window due to lack of requests"));
                            dispose();
                            return;
                        }
                    } else {
                        simplePlainQueue = simplePlainQueue2;
                        subscriber = subscriber2;
                        missed = missed2;
                        this.count = c;
                    }
                    simplePlainQueue2 = simplePlainQueue;
                    subscriber2 = subscriber;
                    missed2 = missed;
                }
            }
            this.f232s.cancel();
            simplePlainQueue2.clear();
            dispose();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed$WindowExactUnboundedSubscriber */
    static final class WindowExactUnboundedSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements FlowableSubscriber<T>, Subscription, Runnable {
        static final Object NEXT = new Object();
        final int bufferSize;

        /* renamed from: s */
        Subscription f233s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final SequentialDisposable timer = new SequentialDisposable();
        final long timespan;
        final TimeUnit unit;
        UnicastProcessor<T> window;

        WindowExactUnboundedSubscriber(Subscriber<? super Flowable<T>> actual, long timespan2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f233s, s)) {
                this.f233s = s;
                this.window = UnicastProcessor.create(this.bufferSize);
                Subscriber<? super Flowable<T>> a = this.actual;
                a.onSubscribe(this);
                long r = requested();
                if (r != 0) {
                    a.onNext(this.window);
                    if (r != Long.MAX_VALUE) {
                        produced(1);
                    }
                    if (!this.cancelled) {
                        if (this.timer.replace(this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit))) {
                            s.request(Long.MAX_VALUE);
                        }
                    }
                } else {
                    this.cancelled = true;
                    s.cancel();
                    a.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests."));
                }
            }
        }

        public void onNext(T t) {
            if (!this.terminated) {
                if (fastEnter()) {
                    this.window.onNext(t);
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    this.queue.offer(NotificationLite.next(t));
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(t);
            dispose();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            dispose();
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
        }

        public void run() {
            if (this.cancelled) {
                this.terminated = true;
                dispose();
            }
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            SimplePlainQueue<Object> q = this.queue;
            Subscriber<? super Flowable<T>> a = this.actual;
            UnicastProcessor<T> w = this.window;
            int missed = 1;
            while (true) {
                boolean term = this.terminated;
                boolean d = this.done;
                Object o = q.poll();
                if (!d || !(o == null || o == NEXT)) {
                    if (o == null) {
                        missed = leave(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else if (o == NEXT) {
                        w.onComplete();
                        if (!term) {
                            w = UnicastProcessor.create(this.bufferSize);
                            this.window = w;
                            long r = requested();
                            if (r != 0) {
                                a.onNext(w);
                                if (r != Long.MAX_VALUE) {
                                    produced(1);
                                }
                            } else {
                                this.window = null;
                                this.queue.clear();
                                this.f233s.cancel();
                                dispose();
                                a.onError(new MissingBackpressureException("Could not deliver first window due to lack of requests."));
                                return;
                            }
                        } else {
                            this.f233s.cancel();
                        }
                    } else {
                        w.onNext(NotificationLite.getValue(o));
                    }
                }
            }
            this.window = null;
            q.clear();
            dispose();
            Throwable err = this.error;
            if (err != null) {
                w.onError(err);
            } else {
                w.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed$WindowSkipSubscriber */
    static final class WindowSkipSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription, Runnable {
        final int bufferSize;

        /* renamed from: s */
        Subscription f234s;
        volatile boolean terminated;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        final List<UnicastProcessor<T>> windows = new LinkedList();
        final Worker worker;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed$WindowSkipSubscriber$Completion */
        final class Completion implements Runnable {
            private final UnicastProcessor<T> processor;

            Completion(UnicastProcessor<T> processor2) {
                this.processor = processor2;
            }

            public void run() {
                WindowSkipSubscriber.this.complete(this.processor);
            }
        }

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableWindowTimed$WindowSkipSubscriber$SubjectWork */
        static final class SubjectWork<T> {
            final boolean open;

            /* renamed from: w */
            final UnicastProcessor<T> f235w;

            SubjectWork(UnicastProcessor<T> w, boolean open2) {
                this.f235w = w;
                this.open = open2;
            }
        }

        WindowSkipSubscriber(Subscriber<? super Flowable<T>> actual, long timespan2, long timeskip2, TimeUnit unit2, Worker worker2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.worker = worker2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f234s, s)) {
                this.f234s = s;
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    long r = requested();
                    if (r != 0) {
                        UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize);
                        this.windows.add(w);
                        this.actual.onNext(w);
                        if (r != Long.MAX_VALUE) {
                            produced(1);
                        }
                        this.worker.schedule(new Completion(w), this.timespan, this.unit);
                        this.worker.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                        s.request(Long.MAX_VALUE);
                    } else {
                        s.cancel();
                        this.actual.onError(new MissingBackpressureException("Could not emit the first window due to lack of requests"));
                    }
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                for (UnicastProcessor<T> w : this.windows) {
                    w.onNext(t);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                this.queue.offer(t);
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(t);
            dispose();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            dispose();
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
        }

        public void dispose() {
            this.worker.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void complete(UnicastProcessor<T> w) {
            this.queue.offer(new SubjectWork(w, false));
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            SimplePlainQueue simplePlainQueue;
            int missed;
            SimplePlainQueue simplePlainQueue2 = this.queue;
            Subscriber<? super Flowable<T>> a = this.actual;
            List<UnicastProcessor<T>> ws = this.windows;
            int missed2 = 1;
            while (!this.terminated) {
                boolean d = this.done;
                Object v = simplePlainQueue2.poll();
                boolean empty = v == null;
                boolean sw = v instanceof SubjectWork;
                if (d && (empty || sw)) {
                    simplePlainQueue2.clear();
                    Throwable e = this.error;
                    if (e != null) {
                        for (UnicastProcessor<T> w : ws) {
                            w.onError(e);
                        }
                    } else {
                        for (UnicastProcessor<T> w2 : ws) {
                            w2.onComplete();
                        }
                    }
                    ws.clear();
                    dispose();
                    return;
                } else if (empty) {
                    missed2 = leave(-missed2);
                    if (missed2 == 0) {
                        return;
                    }
                } else {
                    if (sw) {
                        SubjectWork<T> work = (SubjectWork) v;
                        if (!work.open) {
                            simplePlainQueue = simplePlainQueue2;
                            missed = missed2;
                            ws.remove(work.f235w);
                            work.f235w.onComplete();
                            if (ws.isEmpty() && this.cancelled) {
                                this.terminated = true;
                            }
                        } else if (this.cancelled) {
                            simplePlainQueue = simplePlainQueue2;
                            missed = missed2;
                        } else {
                            long r = requested();
                            if (r != 0) {
                                UnicastProcessor<T> w3 = UnicastProcessor.create(this.bufferSize);
                                ws.add(w3);
                                a.onNext(w3);
                                if (r != Long.MAX_VALUE) {
                                    produced(1);
                                }
                                missed = missed2;
                                simplePlainQueue = simplePlainQueue2;
                                this.worker.schedule(new Completion(w3), this.timespan, this.unit);
                            } else {
                                simplePlainQueue = simplePlainQueue2;
                                missed = missed2;
                                a.onError(new MissingBackpressureException("Can't emit window due to lack of requests"));
                            }
                        }
                    } else {
                        simplePlainQueue = simplePlainQueue2;
                        missed = missed2;
                        for (UnicastProcessor<T> w4 : ws) {
                            w4.onNext(v);
                        }
                    }
                    missed2 = missed;
                    simplePlainQueue2 = simplePlainQueue;
                }
            }
            this.f234s.cancel();
            dispose();
            simplePlainQueue2.clear();
            ws.clear();
        }

        public void run() {
            SubjectWork<T> sw = new SubjectWork<>(UnicastProcessor.create(this.bufferSize), true);
            if (!this.cancelled) {
                this.queue.offer(sw);
            }
            if (enter()) {
                drainLoop();
            }
        }
    }

    public FlowableWindowTimed(Flowable<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, long maxSize2, int bufferSize2, boolean restartTimerOnMaxSize2) {
        super(source);
        this.timespan = timespan2;
        this.timeskip = timeskip2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.maxSize = maxSize2;
        this.bufferSize = bufferSize2;
        this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Flowable<T>> s) {
        SerializedSubscriber<Flowable<T>> actual = new SerializedSubscriber<>(s);
        if (this.timespan != this.timeskip) {
            Flowable flowable = this.source;
            WindowSkipSubscriber windowSkipSubscriber = new WindowSkipSubscriber(actual, this.timespan, this.timeskip, this.unit, this.scheduler.createWorker(), this.bufferSize);
            flowable.subscribe((FlowableSubscriber<? super T>) windowSkipSubscriber);
        } else if (this.maxSize == Long.MAX_VALUE) {
            Flowable flowable2 = this.source;
            WindowExactUnboundedSubscriber windowExactUnboundedSubscriber = new WindowExactUnboundedSubscriber(actual, this.timespan, this.unit, this.scheduler, this.bufferSize);
            flowable2.subscribe((FlowableSubscriber<? super T>) windowExactUnboundedSubscriber);
        } else {
            Flowable flowable3 = this.source;
            WindowExactBoundedSubscriber windowExactBoundedSubscriber = new WindowExactBoundedSubscriber(actual, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize);
            flowable3.subscribe((FlowableSubscriber<? super T>) windowExactBoundedSubscriber);
        }
    }
}
