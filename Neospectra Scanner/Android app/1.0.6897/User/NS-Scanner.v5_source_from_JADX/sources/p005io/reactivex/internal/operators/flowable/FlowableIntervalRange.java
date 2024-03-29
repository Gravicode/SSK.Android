package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.schedulers.TrampolineScheduler;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableIntervalRange */
public final class FlowableIntervalRange extends Flowable<Long> {
    final long end;
    final long initialDelay;
    final long period;
    final Scheduler scheduler;
    final long start;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableIntervalRange$IntervalRangeSubscriber */
    static final class IntervalRangeSubscriber extends AtomicLong implements Subscription, Runnable {
        private static final long serialVersionUID = -2809475196591179431L;
        final Subscriber<? super Long> actual;
        long count;
        final long end;
        final AtomicReference<Disposable> resource = new AtomicReference<>();

        IntervalRangeSubscriber(Subscriber<? super Long> actual2, long start, long end2) {
            this.actual = actual2;
            this.count = start;
            this.end = end2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this, n);
            }
        }

        public void cancel() {
            DisposableHelper.dispose(this.resource);
        }

        public void run() {
            if (this.resource.get() != DisposableHelper.DISPOSED) {
                long r = get();
                if (r != 0) {
                    long c = this.count;
                    this.actual.onNext(Long.valueOf(c));
                    if (c == this.end) {
                        if (this.resource.get() != DisposableHelper.DISPOSED) {
                            this.actual.onComplete();
                        }
                        DisposableHelper.dispose(this.resource);
                        return;
                    }
                    this.count = 1 + c;
                    if (r != Long.MAX_VALUE) {
                        decrementAndGet();
                    }
                } else {
                    Subscriber<? super Long> subscriber = this.actual;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Can't deliver value ");
                    sb.append(this.count);
                    sb.append(" due to lack of requests");
                    subscriber.onError(new MissingBackpressureException(sb.toString()));
                    DisposableHelper.dispose(this.resource);
                }
            }
        }

        public void setResource(Disposable d) {
            DisposableHelper.setOnce(this.resource, d);
        }
    }

    public FlowableIntervalRange(long start2, long end2, long initialDelay2, long period2, TimeUnit unit2, Scheduler scheduler2) {
        this.initialDelay = initialDelay2;
        this.period = period2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.start = start2;
        this.end = end2;
    }

    public void subscribeActual(Subscriber<? super Long> s) {
        IntervalRangeSubscriber is = new IntervalRangeSubscriber(s, this.start, this.end);
        s.onSubscribe(is);
        Scheduler sch = this.scheduler;
        if (sch instanceof TrampolineScheduler) {
            Worker worker = sch.createWorker();
            is.setResource(worker);
            worker.schedulePeriodically(is, this.initialDelay, this.period, this.unit);
            return;
        }
        is.setResource(sch.schedulePeriodicallyDirect(is, this.initialDelay, this.period, this.unit));
    }
}
