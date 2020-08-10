package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFrom */
public final class FlowableWithLatestFrom<T, U, R> extends AbstractFlowableWithUpstream<T, R> {
    final BiFunction<? super T, ? super U, ? extends R> combiner;
    final Publisher<? extends U> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFrom$FlowableWithLatestSubscriber */
    final class FlowableWithLatestSubscriber implements FlowableSubscriber<U> {
        private final WithLatestFromSubscriber<T, U, R> wlf;

        FlowableWithLatestSubscriber(WithLatestFromSubscriber<T, U, R> wlf2) {
            this.wlf = wlf2;
        }

        public void onSubscribe(Subscription s) {
            if (this.wlf.setOther(s)) {
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(U t) {
            this.wlf.lazySet(t);
        }

        public void onError(Throwable t) {
            this.wlf.otherError(t);
        }

        public void onComplete() {
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFrom$WithLatestFromSubscriber */
    static final class WithLatestFromSubscriber<T, U, R> extends AtomicReference<U> implements ConditionalSubscriber<T>, Subscription {
        private static final long serialVersionUID = -312246233408980075L;
        final Subscriber<? super R> actual;
        final BiFunction<? super T, ? super U, ? extends R> combiner;
        final AtomicReference<Subscription> other = new AtomicReference<>();
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        final AtomicReference<Subscription> f236s = new AtomicReference<>();

        WithLatestFromSubscriber(Subscriber<? super R> actual2, BiFunction<? super T, ? super U, ? extends R> combiner2) {
            this.actual = actual2;
            this.combiner = combiner2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.f236s, this.requested, s);
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                ((Subscription) this.f236s.get()).request(1);
            }
        }

        public boolean tryOnNext(T t) {
            U u = get();
            if (u == null) {
                return false;
            }
            try {
                this.actual.onNext(ObjectHelper.requireNonNull(this.combiner.apply(t, u), "The combiner returned a null value"));
                return true;
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                this.actual.onError(e);
                return false;
            }
        }

        public void onError(Throwable t) {
            SubscriptionHelper.cancel(this.other);
            this.actual.onError(t);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            this.actual.onComplete();
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.f236s, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f236s);
            SubscriptionHelper.cancel(this.other);
        }

        public boolean setOther(Subscription o) {
            return SubscriptionHelper.setOnce(this.other, o);
        }

        public void otherError(Throwable e) {
            SubscriptionHelper.cancel(this.f236s);
            this.actual.onError(e);
        }
    }

    public FlowableWithLatestFrom(Flowable<T> source, BiFunction<? super T, ? super U, ? extends R> combiner2, Publisher<? extends U> other2) {
        super(source);
        this.combiner = combiner2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        SerializedSubscriber<R> serial = new SerializedSubscriber<>(s);
        WithLatestFromSubscriber<T, U, R> wlf = new WithLatestFromSubscriber<>(serial, this.combiner);
        serial.onSubscribe(wlf);
        this.other.subscribe(new FlowableWithLatestSubscriber(wlf));
        this.source.subscribe((FlowableSubscriber<? super T>) wlf);
    }
}
