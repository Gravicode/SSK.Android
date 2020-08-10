package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLastTimed */
public final class ObservableTakeLastTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final int bufferSize;
    final long count;
    final boolean delayError;
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLastTimed$TakeLastTimedObserver */
    static final class TakeLastTimedObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
        private static final long serialVersionUID = -5677354903406201275L;
        final Observer<? super T> actual;
        volatile boolean cancelled;
        final long count;

        /* renamed from: d */
        Disposable f361d;
        final boolean delayError;
        Throwable error;
        final SpscLinkedArrayQueue<Object> queue;
        final Scheduler scheduler;
        final long time;
        final TimeUnit unit;

        TakeLastTimedObserver(Observer<? super T> actual2, long count2, long time2, TimeUnit unit2, Scheduler scheduler2, int bufferSize, boolean delayError2) {
            this.actual = actual2;
            this.count = count2;
            this.time = time2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
            this.delayError = delayError2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f361d, d)) {
                this.f361d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            SpscLinkedArrayQueue<Object> q = this.queue;
            long now = this.scheduler.now(this.unit);
            long time2 = this.time;
            long c = this.count;
            boolean unbounded = c == Long.MAX_VALUE;
            q.offer(Long.valueOf(now), t);
            while (!q.isEmpty()) {
                if (((Long) q.peek()).longValue() <= now - time2 || (!unbounded && ((long) (q.size() >> 1)) > c)) {
                    q.poll();
                    q.poll();
                } else {
                    return;
                }
            }
        }

        public void onError(Throwable t) {
            this.error = t;
            drain();
        }

        public void onComplete() {
            drain();
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f361d.dispose();
                if (compareAndSet(false, true)) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (compareAndSet(false, true)) {
                Observer<? super T> a = this.actual;
                SpscLinkedArrayQueue<Object> q = this.queue;
                boolean delayError2 = this.delayError;
                while (!this.cancelled) {
                    if (!delayError2) {
                        Throwable ex = this.error;
                        if (ex != null) {
                            q.clear();
                            a.onError(ex);
                            return;
                        }
                    }
                    Object ts = q.poll();
                    if (ts == null) {
                        Throwable ex2 = this.error;
                        if (ex2 != null) {
                            a.onError(ex2);
                        } else {
                            a.onComplete();
                        }
                        return;
                    }
                    T o = q.poll();
                    if (((Long) ts).longValue() >= this.scheduler.now(this.unit) - this.time) {
                        a.onNext(o);
                    }
                }
                q.clear();
            }
        }
    }

    public ObservableTakeLastTimed(ObservableSource<T> source, long count2, long time2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2, boolean delayError2) {
        super(source);
        this.count = count2;
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Observer<? super T> t) {
        ObservableSource observableSource = this.source;
        TakeLastTimedObserver takeLastTimedObserver = new TakeLastTimedObserver(t, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError);
        observableSource.subscribe(takeLastTimedObserver);
    }
}
