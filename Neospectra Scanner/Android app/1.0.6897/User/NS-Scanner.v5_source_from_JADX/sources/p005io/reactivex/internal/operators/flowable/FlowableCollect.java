package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCollect */
public final class FlowableCollect<T, U> extends AbstractFlowableWithUpstream<T, U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCollect$CollectSubscriber */
    static final class CollectSubscriber<T, U> extends DeferredScalarSubscription<U> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -3589550218733891694L;
        final BiConsumer<? super U, ? super T> collector;
        boolean done;

        /* renamed from: s */
        Subscription f132s;

        /* renamed from: u */
        final U f133u;

        CollectSubscriber(Subscriber<? super U> actual, U u, BiConsumer<? super U, ? super T> collector2) {
            super(actual);
            this.collector = collector2;
            this.f133u = u;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f132s, s)) {
                this.f132s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.collector.accept(this.f133u, t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f132s.cancel();
                    onError(e);
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
                complete(this.f133u);
            }
        }

        public void cancel() {
            super.cancel();
            this.f132s.cancel();
        }
    }

    public FlowableCollect(Flowable<T> source, Callable<? extends U> initialSupplier2, BiConsumer<? super U, ? super T> collector2) {
        super(source);
        this.initialSupplier = initialSupplier2;
        this.collector = collector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        try {
            this.source.subscribe((FlowableSubscriber<? super T>) new CollectSubscriber<Object>(s, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initial value supplied is null"), this.collector));
        } catch (Throwable e) {
            EmptySubscription.error(e, s);
        }
    }
}
