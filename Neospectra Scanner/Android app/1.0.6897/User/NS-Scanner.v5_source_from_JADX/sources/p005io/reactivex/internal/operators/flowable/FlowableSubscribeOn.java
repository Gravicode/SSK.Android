package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSubscribeOn */
public final class FlowableSubscribeOn<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean nonScheduledRequests;
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSubscribeOn$SubscribeOnSubscriber */
    static final class SubscribeOnSubscriber<T> extends AtomicReference<Thread> implements FlowableSubscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Subscriber<? super T> actual;
        final boolean nonScheduledRequests;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        final AtomicReference<Subscription> f209s = new AtomicReference<>();
        Publisher<T> source;
        final Worker worker;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableSubscribeOn$SubscribeOnSubscriber$Request */
        static final class Request implements Runnable {

            /* renamed from: n */
            private final long f210n;

            /* renamed from: s */
            private final Subscription f211s;

            Request(Subscription s, long n) {
                this.f211s = s;
                this.f210n = n;
            }

            public void run() {
                this.f211s.request(this.f210n);
            }
        }

        SubscribeOnSubscriber(Subscriber<? super T> actual2, Worker worker2, Publisher<T> source2, boolean requestOn) {
            this.actual = actual2;
            this.worker = worker2;
            this.source = source2;
            this.nonScheduledRequests = !requestOn;
        }

        public void run() {
            lazySet(Thread.currentThread());
            Publisher<T> src = this.source;
            this.source = null;
            src.subscribe(this);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.f209s, s)) {
                long r = this.requested.getAndSet(0);
                if (r != 0) {
                    requestUpstream(r, s);
                }
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            this.worker.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
            this.worker.dispose();
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                Subscription s = (Subscription) this.f209s.get();
                if (s != null) {
                    requestUpstream(n, s);
                    return;
                }
                BackpressureHelper.add(this.requested, n);
                Subscription s2 = (Subscription) this.f209s.get();
                if (s2 != null) {
                    long r = this.requested.getAndSet(0);
                    if (r != 0) {
                        requestUpstream(r, s2);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void requestUpstream(long n, Subscription s) {
            if (this.nonScheduledRequests || Thread.currentThread() == get()) {
                s.request(n);
            } else {
                this.worker.schedule(new Request(s, n));
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f209s);
            this.worker.dispose();
        }
    }

    public FlowableSubscribeOn(Flowable<T> source, Scheduler scheduler2, boolean nonScheduledRequests2) {
        super(source);
        this.scheduler = scheduler2;
        this.nonScheduledRequests = nonScheduledRequests2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        Worker w = this.scheduler.createWorker();
        SubscribeOnSubscriber<T> sos = new SubscribeOnSubscriber<>(s, w, this.source, this.nonScheduledRequests);
        s.onSubscribe(sos);
        w.schedule(sos);
    }
}
