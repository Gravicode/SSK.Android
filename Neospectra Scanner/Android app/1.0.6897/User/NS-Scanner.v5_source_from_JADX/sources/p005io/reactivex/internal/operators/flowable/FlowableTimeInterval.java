package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeInterval */
public final class FlowableTimeInterval<T> extends AbstractFlowableWithUpstream<T, Timed<T>> {
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeInterval$TimeIntervalSubscriber */
    static final class TimeIntervalSubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super Timed<T>> actual;
        long lastTime;

        /* renamed from: s */
        Subscription f220s;
        final Scheduler scheduler;
        final TimeUnit unit;

        TimeIntervalSubscriber(Subscriber<? super Timed<T>> actual2, TimeUnit unit2, Scheduler scheduler2) {
            this.actual = actual2;
            this.scheduler = scheduler2;
            this.unit = unit2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f220s, s)) {
                this.lastTime = this.scheduler.now(this.unit);
                this.f220s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            long now = this.scheduler.now(this.unit);
            long last = this.lastTime;
            this.lastTime = now;
            this.actual.onNext(new Timed(t, now - last, this.unit));
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long n) {
            this.f220s.request(n);
        }

        public void cancel() {
            this.f220s.cancel();
        }
    }

    public FlowableTimeInterval(Flowable<T> source, TimeUnit unit2, Scheduler scheduler2) {
        super(source);
        this.scheduler = scheduler2;
        this.unit = unit2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Timed<T>> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new TimeIntervalSubscriber<Object>(s, this.unit, this.scheduler));
    }
}
