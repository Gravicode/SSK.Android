package p005io.reactivex.internal.operators.flowable;

import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.ArrayListSupplier;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableToListSingle */
public final class FlowableToListSingle<T, U extends Collection<? super T>> extends Single<U> implements FuseToFlowable<U> {
    final Callable<U> collectionSupplier;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableToListSingle$ToListSubscriber */
    static final class ToListSubscriber<T, U extends Collection<? super T>> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super U> actual;

        /* renamed from: s */
        Subscription f222s;
        U value;

        ToListSubscriber(SingleObserver<? super U> actual2, U collection) {
            this.actual = actual2;
            this.value = collection;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f222s, s)) {
                this.f222s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.value.add(t);
        }

        public void onError(Throwable t) {
            this.value = null;
            this.f222s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f222s = SubscriptionHelper.CANCELLED;
            this.actual.onSuccess(this.value);
        }

        public void dispose() {
            this.f222s.cancel();
            this.f222s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f222s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableToListSingle(Flowable<T> source2) {
        this(source2, ArrayListSupplier.asCallable());
    }

    public FlowableToListSingle(Flowable<T> source2, Callable<U> collectionSupplier2) {
        this.source = source2;
        this.collectionSupplier = collectionSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> s) {
        try {
            this.source.subscribe((FlowableSubscriber<? super T>) new ToListSubscriber<Object>(s, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, s);
        }
    }

    public Flowable<U> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableToList<T>(this.source, this.collectionSupplier));
    }
}
