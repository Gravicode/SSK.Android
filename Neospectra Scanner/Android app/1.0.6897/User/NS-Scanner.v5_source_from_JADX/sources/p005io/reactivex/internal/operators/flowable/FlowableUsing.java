package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableUsing */
public final class FlowableUsing<T, D> extends Flowable<T> {
    final Consumer<? super D> disposer;
    final boolean eager;
    final Callable<? extends D> resourceSupplier;
    final Function<? super D, ? extends Publisher<? extends T>> sourceSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableUsing$UsingSubscriber */
    static final class UsingSubscriber<T, D> extends AtomicBoolean implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = 5904473792286235046L;
        final Subscriber<? super T> actual;
        final Consumer<? super D> disposer;
        final boolean eager;
        final D resource;

        /* renamed from: s */
        Subscription f224s;

        UsingSubscriber(Subscriber<? super T> actual2, D resource2, Consumer<? super D> disposer2, boolean eager2) {
            this.actual = actual2;
            this.resource = resource2;
            this.disposer = disposer2;
            this.eager = eager2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f224s, s)) {
                this.f224s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (this.eager) {
                Throwable innerError = null;
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        innerError = e;
                    }
                }
                this.f224s.cancel();
                if (innerError != null) {
                    this.actual.onError(new CompositeException(t, innerError));
                    return;
                }
                this.actual.onError(t);
                return;
            }
            this.actual.onError(t);
            this.f224s.cancel();
            disposeAfter();
        }

        public void onComplete() {
            if (this.eager) {
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        this.actual.onError(e);
                        return;
                    }
                }
                this.f224s.cancel();
                this.actual.onComplete();
            } else {
                this.actual.onComplete();
                this.f224s.cancel();
                disposeAfter();
            }
        }

        public void request(long n) {
            this.f224s.request(n);
        }

        public void cancel() {
            disposeAfter();
            this.f224s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void disposeAfter() {
            if (compareAndSet(false, true)) {
                try {
                    this.disposer.accept(this.resource);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
        }
    }

    public FlowableUsing(Callable<? extends D> resourceSupplier2, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier2, Consumer<? super D> disposer2, boolean eager2) {
        this.resourceSupplier = resourceSupplier2;
        this.sourceSupplier = sourceSupplier2;
        this.disposer = disposer2;
        this.eager = eager2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        try {
            D resource = this.resourceSupplier.call();
            try {
                ((Publisher) ObjectHelper.requireNonNull(this.sourceSupplier.apply(resource), "The sourceSupplier returned a null Publisher")).subscribe(new UsingSubscriber<>(s, resource, this.disposer, this.eager));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptySubscription.error(new CompositeException(e, ex), s);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }
}
