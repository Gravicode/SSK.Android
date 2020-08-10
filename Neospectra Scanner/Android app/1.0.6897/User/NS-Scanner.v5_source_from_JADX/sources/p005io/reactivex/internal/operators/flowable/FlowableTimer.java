package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimer */
public final class FlowableTimer extends Flowable<Long> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimer$TimerSubscriber */
    static final class TimerSubscriber extends AtomicReference<Disposable> implements Subscription, Runnable {
        private static final long serialVersionUID = -2809475196591179431L;
        final Subscriber<? super Long> actual;
        volatile boolean requested;

        TimerSubscriber(Subscriber<? super Long> actual2) {
            this.actual = actual2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                this.requested = true;
            }
        }

        public void cancel() {
            DisposableHelper.dispose(this);
        }

        public void run() {
            if (get() == DisposableHelper.DISPOSED) {
                return;
            }
            if (this.requested) {
                this.actual.onNext(Long.valueOf(0));
                lazySet(EmptyDisposable.INSTANCE);
                this.actual.onComplete();
                return;
            }
            lazySet(EmptyDisposable.INSTANCE);
            this.actual.onError(new MissingBackpressureException("Can't deliver value due to lack of requests"));
        }

        public void setResource(Disposable d) {
            DisposableHelper.trySet(this, d);
        }
    }

    public FlowableTimer(long delay2, TimeUnit unit2, Scheduler scheduler2) {
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Subscriber<? super Long> s) {
        TimerSubscriber ios = new TimerSubscriber(s);
        s.onSubscribe(ios);
        ios.setResource(this.scheduler.scheduleDirect(ios, this.delay, this.unit));
    }
}
