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

/* renamed from: io.reactivex.internal.operators.flowable.FlowableInterval */
public final class FlowableInterval extends Flowable<Long> {
    final long initialDelay;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInterval$IntervalSubscriber */
    static final class IntervalSubscriber extends AtomicLong implements Subscription, Runnable {
        private static final long serialVersionUID = -2809475196591179431L;
        final Subscriber<? super Long> actual;
        long count;
        final AtomicReference<Disposable> resource = new AtomicReference<>();

        IntervalSubscriber(Subscriber<? super Long> actual2) {
            this.actual = actual2;
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
            if (this.resource.get() == DisposableHelper.DISPOSED) {
                return;
            }
            if (get() != 0) {
                Subscriber<? super Long> subscriber = this.actual;
                long j = this.count;
                this.count = j + 1;
                subscriber.onNext(Long.valueOf(j));
                BackpressureHelper.produced(this, 1);
                return;
            }
            Subscriber<? super Long> subscriber2 = this.actual;
            StringBuilder sb = new StringBuilder();
            sb.append("Can't deliver value ");
            sb.append(this.count);
            sb.append(" due to lack of requests");
            subscriber2.onError(new MissingBackpressureException(sb.toString()));
            DisposableHelper.dispose(this.resource);
        }

        public void setResource(Disposable d) {
            DisposableHelper.setOnce(this.resource, d);
        }
    }

    public FlowableInterval(long initialDelay2, long period2, TimeUnit unit2, Scheduler scheduler2) {
        this.initialDelay = initialDelay2;
        this.period = period2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Subscriber<? super Long> s) {
        IntervalSubscriber is = new IntervalSubscriber(s);
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
