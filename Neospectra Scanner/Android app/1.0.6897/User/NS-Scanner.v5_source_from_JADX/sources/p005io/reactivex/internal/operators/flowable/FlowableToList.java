package p005io.reactivex.internal.operators.flowable;

import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableToList */
public final class FlowableToList<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<U> collectionSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableToList$ToListSubscriber */
    static final class ToListSubscriber<T, U extends Collection<? super T>> extends DeferredScalarSubscription<U> implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -8134157938864266736L;

        /* renamed from: s */
        Subscription f221s;

        ToListSubscriber(Subscriber<? super U> actual, U collection) {
            super(actual);
            this.value = collection;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f221s, s)) {
                this.f221s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            U v = (Collection) this.value;
            if (v != null) {
                v.add(t);
            }
        }

        public void onError(Throwable t) {
            this.value = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            complete(this.value);
        }

        public void cancel() {
            super.cancel();
            this.f221s.cancel();
        }
    }

    public FlowableToList(Flowable<T> source, Callable<U> collectionSupplier2) {
        super(source);
        this.collectionSupplier = collectionSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        try {
            this.source.subscribe((FlowableSubscriber<? super T>) new ToListSubscriber<Object>(s, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }
}
