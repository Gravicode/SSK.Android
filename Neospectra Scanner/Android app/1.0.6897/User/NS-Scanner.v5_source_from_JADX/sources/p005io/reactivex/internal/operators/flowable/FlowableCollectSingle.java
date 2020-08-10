package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCollectSingle */
public final class FlowableCollectSingle<T, U> extends Single<U> implements FuseToFlowable<U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCollectSingle$CollectSubscriber */
    static final class CollectSubscriber<T, U> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super U> actual;
        final BiConsumer<? super U, ? super T> collector;
        boolean done;

        /* renamed from: s */
        Subscription f134s;

        /* renamed from: u */
        final U f135u;

        CollectSubscriber(SingleObserver<? super U> actual2, U u, BiConsumer<? super U, ? super T> collector2) {
            this.actual = actual2;
            this.collector = collector2;
            this.f135u = u;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f134s, s)) {
                this.f134s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.collector.accept(this.f135u, t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f134s.cancel();
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
            this.f134s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.f134s = SubscriptionHelper.CANCELLED;
                this.actual.onSuccess(this.f135u);
            }
        }

        public void dispose() {
            this.f134s.cancel();
            this.f134s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f134s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableCollectSingle(Flowable<T> source2, Callable<? extends U> initialSupplier2, BiConsumer<? super U, ? super T> collector2) {
        this.source = source2;
        this.initialSupplier = initialSupplier2;
        this.collector = collector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> s) {
        try {
            this.source.subscribe((FlowableSubscriber<? super T>) new CollectSubscriber<Object>(s, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.collector));
        } catch (Throwable e) {
            EmptyDisposable.error(e, s);
        }
    }

    public Flowable<U> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCollect<T>(this.source, this.initialSupplier, this.collector));
    }
}
