package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay */
public final class FlowableDelay<T> extends AbstractFlowableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber */
    static final class DelaySubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final long delay;
        final boolean delayError;

        /* renamed from: s */
        Subscription f142s;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f143w;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber$OnComplete */
        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                try {
                    DelaySubscriber.this.actual.onComplete();
                } finally {
                    DelaySubscriber.this.f143w.dispose();
                }
            }
        }

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber$OnError */
        final class OnError implements Runnable {

            /* renamed from: t */
            private final Throwable f144t;

            OnError(Throwable t) {
                this.f144t = t;
            }

            public void run() {
                try {
                    DelaySubscriber.this.actual.onError(this.f144t);
                } finally {
                    DelaySubscriber.this.f143w.dispose();
                }
            }
        }

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber$OnNext */
        final class OnNext implements Runnable {

            /* renamed from: t */
            private final T f145t;

            OnNext(T t) {
                this.f145t = t;
            }

            public void run() {
                DelaySubscriber.this.actual.onNext(this.f145t);
            }
        }

        DelaySubscriber(Subscriber<? super T> actual2, long delay2, TimeUnit unit2, Worker w, boolean delayError2) {
            this.actual = actual2;
            this.delay = delay2;
            this.unit = unit2;
            this.f143w = w;
            this.delayError = delayError2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f142s, s)) {
                this.f142s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.f143w.schedule(new OnNext(t), this.delay, this.unit);
        }

        public void onError(Throwable t) {
            this.f143w.schedule(new OnError(t), this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.f143w.schedule(new OnComplete(), this.delay, this.unit);
        }

        public void request(long n) {
            this.f142s.request(n);
        }

        public void cancel() {
            this.f142s.cancel();
            this.f143w.dispose();
        }
    }

    public FlowableDelay(Flowable<T> source, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        super(source);
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> t) {
        Subscriber<? super T> serializedSubscriber;
        if (this.delayError) {
            serializedSubscriber = t;
        } else {
            serializedSubscriber = new SerializedSubscriber<>(t);
        }
        Subscriber<? super T> s = serializedSubscriber;
        Worker w = this.scheduler.createWorker();
        Flowable flowable = this.source;
        DelaySubscriber delaySubscriber = new DelaySubscriber(s, this.delay, this.unit, w, this.delayError);
        flowable.subscribe((FlowableSubscriber<? super T>) delaySubscriber);
    }
}
