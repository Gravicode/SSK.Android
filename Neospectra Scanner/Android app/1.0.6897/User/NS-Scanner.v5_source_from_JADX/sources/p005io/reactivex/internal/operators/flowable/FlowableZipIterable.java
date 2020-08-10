package p005io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableZipIterable */
public final class FlowableZipIterable<T, U, V> extends AbstractFlowableWithUpstream<T, V> {
    final Iterable<U> other;
    final BiFunction<? super T, ? super U, ? extends V> zipper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableZipIterable$ZipIterableSubscriber */
    static final class ZipIterableSubscriber<T, U, V> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super V> actual;
        boolean done;
        final Iterator<U> iterator;

        /* renamed from: s */
        Subscription f238s;
        final BiFunction<? super T, ? super U, ? extends V> zipper;

        ZipIterableSubscriber(Subscriber<? super V> actual2, Iterator<U> iterator2, BiFunction<? super T, ? super U, ? extends V> zipper2) {
            this.actual = actual2;
            this.iterator = iterator2;
            this.zipper = zipper2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f238s, s)) {
                this.f238s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    try {
                        this.actual.onNext(ObjectHelper.requireNonNull(this.zipper.apply(t, ObjectHelper.requireNonNull(this.iterator.next(), "The iterator returned a null value")), "The zipper function returned a null value"));
                        try {
                            if (!this.iterator.hasNext()) {
                                this.done = true;
                                this.f238s.cancel();
                                this.actual.onComplete();
                            }
                        } catch (Throwable e) {
                            error(e);
                        }
                    } catch (Throwable e2) {
                        error(e2);
                    }
                } catch (Throwable e3) {
                    error(e3);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable e) {
            Exceptions.throwIfFatal(e);
            this.done = true;
            this.f238s.cancel();
            this.actual.onError(e);
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            this.f238s.request(n);
        }

        public void cancel() {
            this.f238s.cancel();
        }
    }

    public FlowableZipIterable(Flowable<T> source, Iterable<U> other2, BiFunction<? super T, ? super U, ? extends V> zipper2) {
        super(source);
        this.other = other2;
        this.zipper = zipper2;
    }

    public void subscribeActual(Subscriber<? super V> t) {
        try {
            Iterator<U> it = (Iterator) ObjectHelper.requireNonNull(this.other.iterator(), "The iterator returned by other is null");
            try {
                if (!it.hasNext()) {
                    EmptySubscription.complete(t);
                } else {
                    this.source.subscribe((FlowableSubscriber<? super T>) new ZipIterableSubscriber<Object>(t, it, this.zipper));
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptySubscription.error(e, t);
            }
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            EmptySubscription.error(e2, t);
        }
    }
}
