package p005io.reactivex.internal.operators.observable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.observers.QueueDrainObserver;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.observers.SerializedObserver;
import p005io.reactivex.subjects.UnicastSubject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed */
public final class ObservableWindowTimed<T> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int bufferSize;
    final long maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed$WindowExactBoundedObserver */
    static final class WindowExactBoundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
        final int bufferSize;
        long count;
        final long maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;

        /* renamed from: s */
        Disposable f377s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;
        UnicastSubject<T> window;
        final Worker worker;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed$WindowExactBoundedObserver$ConsumerIndexHolder */
        static final class ConsumerIndexHolder implements Runnable {
            final long index;
            final WindowExactBoundedObserver<?> parent;

            ConsumerIndexHolder(long index2, WindowExactBoundedObserver<?> parent2) {
                this.index = index2;
                this.parent = parent2;
            }

            public void run() {
                WindowExactBoundedObserver<?> p = this.parent;
                if (!p.cancelled) {
                    p.queue.offer(this);
                } else {
                    p.terminated = true;
                    p.disposeTimer();
                }
                if (p.enter()) {
                    p.drainLoop();
                }
            }
        }

        WindowExactBoundedObserver(Observer<? super Observable<T>> actual, long timespan2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2, long maxSize2, boolean restartTimerOnMaxSize2) {
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

        public void onSubscribe(Disposable s) {
            Disposable d;
            if (DisposableHelper.validate(this.f377s, s)) {
                this.f377s = s;
                Observer<? super Observable<T>> a = this.actual;
                a.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
                    this.window = w;
                    a.onNext(w);
                    ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
                    if (this.restartTimerOnMaxSize) {
                        d = this.worker.schedulePeriodically(consumerIndexHolder, this.timespan, this.timespan, this.unit);
                    } else {
                        d = this.scheduler.schedulePeriodicallyDirect(consumerIndexHolder, this.timespan, this.timespan, this.unit);
                    }
                    DisposableHelper.replace(this.timer, d);
                }
            }
        }

        public void onNext(T t) {
            if (!this.terminated) {
                if (fastEnter()) {
                    UnicastSubject<T> w = this.window;
                    w.onNext(t);
                    long c = this.count + 1;
                    if (c >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        w.onComplete();
                        UnicastSubject<T> w2 = UnicastSubject.create(this.bufferSize);
                        this.window = w2;
                        this.actual.onNext(w2);
                        if (this.restartTimerOnMaxSize) {
                            ((Disposable) this.timer.get()).dispose();
                            DisposableHelper.replace(this.timer, this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit));
                        }
                    } else {
                        this.count = c;
                    }
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
            disposeTimer();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            disposeTimer();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeTimer() {
            DisposableHelper.dispose(this.timer);
            Worker w = this.worker;
            if (w != null) {
                w.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            int missed;
            Observer observer;
            MpscLinkedQueue mpscLinkedQueue;
            MpscLinkedQueue mpscLinkedQueue2 = (MpscLinkedQueue) this.queue;
            Observer observer2 = this.actual;
            UnicastSubject<T> w = this.window;
            int missed2 = 1;
            while (!this.terminated) {
                boolean d = this.done;
                Object o = mpscLinkedQueue2.poll();
                boolean empty = o == null;
                boolean isHolder = o instanceof ConsumerIndexHolder;
                if (d && (empty || isHolder)) {
                    this.window = null;
                    mpscLinkedQueue2.clear();
                    disposeTimer();
                    Throwable err = this.error;
                    if (err != null) {
                        w.onError(err);
                    } else {
                        w.onComplete();
                    }
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
                        w = UnicastSubject.create(this.bufferSize);
                        this.window = w;
                        observer2.onNext(w);
                    }
                } else {
                    w.onNext(NotificationLite.getValue(o));
                    long c = this.count + 1;
                    UnicastSubject<T> w2 = w;
                    if (c >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        w2.onComplete();
                        UnicastSubject<T> w3 = UnicastSubject.create(this.bufferSize);
                        this.window = w3;
                        this.actual.onNext(w3);
                        if (this.restartTimerOnMaxSize) {
                            Disposable tm = (Disposable) this.timer.get();
                            tm.dispose();
                            mpscLinkedQueue = mpscLinkedQueue2;
                            observer = observer2;
                            missed = missed2;
                            Disposable task = this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit);
                            if (!this.timer.compareAndSet(tm, task)) {
                                task.dispose();
                            }
                        } else {
                            mpscLinkedQueue = mpscLinkedQueue2;
                            observer = observer2;
                            missed = missed2;
                        }
                        w = w3;
                    } else {
                        mpscLinkedQueue = mpscLinkedQueue2;
                        observer = observer2;
                        missed = missed2;
                        w = w2;
                        this.count = c;
                    }
                    mpscLinkedQueue2 = mpscLinkedQueue;
                    observer2 = observer;
                    missed2 = missed;
                }
            }
            this.f377s.dispose();
            mpscLinkedQueue2.clear();
            disposeTimer();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed$WindowExactUnboundedObserver */
    static final class WindowExactUnboundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Observer<T>, Disposable, Runnable {
        static final Object NEXT = new Object();
        final int bufferSize;

        /* renamed from: s */
        Disposable f378s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;
        UnicastSubject<T> window;

        WindowExactUnboundedObserver(Observer<? super Observable<T>> actual, long timespan2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f378s, s)) {
                this.f378s = s;
                this.window = UnicastSubject.create(this.bufferSize);
                Observer<? super Observable<T>> a = this.actual;
                a.onSubscribe(this);
                a.onNext(this.window);
                if (!this.cancelled) {
                    DisposableHelper.replace(this.timer, this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit));
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
            disposeTimer();
            this.actual.onError(t);
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            disposeTimer();
            this.actual.onComplete();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeTimer() {
            DisposableHelper.dispose(this.timer);
        }

        public void run() {
            if (this.cancelled) {
                this.terminated = true;
                disposeTimer();
            }
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MpscLinkedQueue<Object> q = (MpscLinkedQueue) this.queue;
            Observer<? super Observable<T>> a = this.actual;
            UnicastSubject<T> w = this.window;
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
                            w = UnicastSubject.create(this.bufferSize);
                            this.window = w;
                            a.onNext(w);
                        } else {
                            this.f378s.dispose();
                        }
                    } else {
                        w.onNext(NotificationLite.getValue(o));
                    }
                }
            }
            this.window = null;
            q.clear();
            disposeTimer();
            Throwable err = this.error;
            if (err != null) {
                w.onError(err);
            } else {
                w.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed$WindowSkipObserver */
    static final class WindowSkipObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable, Runnable {
        final int bufferSize;

        /* renamed from: s */
        Disposable f379s;
        volatile boolean terminated;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        final List<UnicastSubject<T>> windows = new LinkedList();
        final Worker worker;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed$WindowSkipObserver$CompletionTask */
        final class CompletionTask implements Runnable {

            /* renamed from: w */
            private final UnicastSubject<T> f380w;

            CompletionTask(UnicastSubject<T> w) {
                this.f380w = w;
            }

            public void run() {
                WindowSkipObserver.this.complete(this.f380w);
            }
        }

        /* renamed from: io.reactivex.internal.operators.observable.ObservableWindowTimed$WindowSkipObserver$SubjectWork */
        static final class SubjectWork<T> {
            final boolean open;

            /* renamed from: w */
            final UnicastSubject<T> f381w;

            SubjectWork(UnicastSubject<T> w, boolean open2) {
                this.f381w = w;
                this.open = open2;
            }
        }

        WindowSkipObserver(Observer<? super Observable<T>> actual, long timespan2, long timeskip2, TimeUnit unit2, Worker worker2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.worker = worker2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f379s, s)) {
                this.f379s = s;
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
                    this.windows.add(w);
                    this.actual.onNext(w);
                    this.worker.schedule(new CompletionTask(w), this.timespan, this.unit);
                    this.worker.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                for (UnicastSubject<T> w : this.windows) {
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
            disposeWorker();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            disposeWorker();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeWorker() {
            this.worker.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void complete(UnicastSubject<T> w) {
            this.queue.offer(new SubjectWork(w, false));
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            MpscLinkedQueue<Object> q = (MpscLinkedQueue) this.queue;
            Observer<? super Observable<T>> a = this.actual;
            List<UnicastSubject<T>> ws = this.windows;
            int missed = 1;
            while (!this.terminated) {
                boolean d = this.done;
                Object v = q.poll();
                boolean empty = v == null;
                boolean sw = v instanceof SubjectWork;
                if (d && (empty || sw)) {
                    q.clear();
                    Throwable e = this.error;
                    if (e != null) {
                        for (UnicastSubject<T> w : ws) {
                            w.onError(e);
                        }
                    } else {
                        for (UnicastSubject<T> w2 : ws) {
                            w2.onComplete();
                        }
                    }
                    disposeWorker();
                    ws.clear();
                    return;
                } else if (empty) {
                    missed = leave(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else if (sw) {
                    SubjectWork<T> work = (SubjectWork) v;
                    if (!work.open) {
                        ws.remove(work.f381w);
                        work.f381w.onComplete();
                        if (ws.isEmpty() && this.cancelled) {
                            this.terminated = true;
                        }
                    } else if (!this.cancelled) {
                        UnicastSubject<T> w3 = UnicastSubject.create(this.bufferSize);
                        ws.add(w3);
                        a.onNext(w3);
                        this.worker.schedule(new CompletionTask(w3), this.timespan, this.unit);
                    }
                } else {
                    for (UnicastSubject<T> w4 : ws) {
                        w4.onNext(v);
                    }
                }
            }
            this.f379s.dispose();
            disposeWorker();
            q.clear();
            ws.clear();
        }

        public void run() {
            SubjectWork<T> sw = new SubjectWork<>(UnicastSubject.create(this.bufferSize), true);
            if (!this.cancelled) {
                this.queue.offer(sw);
            }
            if (enter()) {
                drainLoop();
            }
        }
    }

    public ObservableWindowTimed(ObservableSource<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, long maxSize2, int bufferSize2, boolean restartTimerOnMaxSize2) {
        super(source);
        this.timespan = timespan2;
        this.timeskip = timeskip2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.maxSize = maxSize2;
        this.bufferSize = bufferSize2;
        this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
    }

    public void subscribeActual(Observer<? super Observable<T>> t) {
        SerializedObserver<Observable<T>> actual = new SerializedObserver<>(t);
        if (this.timespan != this.timeskip) {
            ObservableSource observableSource = this.source;
            WindowSkipObserver windowSkipObserver = new WindowSkipObserver(actual, this.timespan, this.timeskip, this.unit, this.scheduler.createWorker(), this.bufferSize);
            observableSource.subscribe(windowSkipObserver);
        } else if (this.maxSize == Long.MAX_VALUE) {
            ObservableSource observableSource2 = this.source;
            WindowExactUnboundedObserver windowExactUnboundedObserver = new WindowExactUnboundedObserver(actual, this.timespan, this.unit, this.scheduler, this.bufferSize);
            observableSource2.subscribe(windowExactUnboundedObserver);
        } else {
            ObservableSource observableSource3 = this.source;
            WindowExactBoundedObserver windowExactBoundedObserver = new WindowExactBoundedObserver(actual, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize);
            observableSource3.subscribe(windowExactBoundedObserver);
        }
    }
}
