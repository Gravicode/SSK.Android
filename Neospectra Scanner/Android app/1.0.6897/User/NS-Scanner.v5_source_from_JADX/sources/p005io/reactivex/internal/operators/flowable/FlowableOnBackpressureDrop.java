package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop */
public final class FlowableOnBackpressureDrop<T> extends AbstractFlowableWithUpstream<T, T> implements Consumer<T> {
    final Consumer<? super T> onDrop;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop$BackpressureDropSubscriber */
    static final class BackpressureDropSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -6246093802440953054L;
        final Subscriber<? super T> actual;
        boolean done;
        final Consumer<? super T> onDrop;

        /* renamed from: s */
        Subscription f177s;

        BackpressureDropSubscriber(Subscriber<? super T> actual2, Consumer<? super T> onDrop2) {
            this.actual = actual2;
            this.onDrop = onDrop2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f177s, s)) {
                this.f177s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (get() != 0) {
                    this.actual.onNext(t);
                    BackpressureHelper.produced(this, 1);
                } else {
                    try {
                        this.onDrop.accept(t);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
                        onError(e);
                    }
                }
            }
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
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this, n);
            }
        }

        public void cancel() {
            this.f177s.cancel();
        }
    }

    public FlowableOnBackpressureDrop(Flowable<T> source) {
        super(source);
        this.onDrop = this;
    }

    public FlowableOnBackpressureDrop(Flowable<T> source, Consumer<? super T> onDrop2) {
        super(source);
        this.onDrop = onDrop2;
    }

    public void accept(T t) {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new BackpressureDropSubscriber<Object>(s, this.onDrop));
    }
}
