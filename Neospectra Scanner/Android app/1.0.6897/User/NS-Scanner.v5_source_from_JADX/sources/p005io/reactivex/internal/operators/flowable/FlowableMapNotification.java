package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableMapNotification */
public final class FlowableMapNotification<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Callable<? extends R> onCompleteSupplier;
    final Function<? super Throwable, ? extends R> onErrorMapper;
    final Function<? super T, ? extends R> onNextMapper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableMapNotification$MapNotificationSubscriber */
    static final class MapNotificationSubscriber<T, R> extends SinglePostCompleteSubscriber<T, R> {
        private static final long serialVersionUID = 2757120512858778108L;
        final Callable<? extends R> onCompleteSupplier;
        final Function<? super Throwable, ? extends R> onErrorMapper;
        final Function<? super T, ? extends R> onNextMapper;

        MapNotificationSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends R> onNextMapper2, Function<? super Throwable, ? extends R> onErrorMapper2, Callable<? extends R> onCompleteSupplier2) {
            super(actual);
            this.onNextMapper = onNextMapper2;
            this.onErrorMapper = onErrorMapper2;
            this.onCompleteSupplier = onCompleteSupplier2;
        }

        public void onNext(T t) {
            try {
                R p = ObjectHelper.requireNonNull(this.onNextMapper.apply(t), "The onNext publisher returned is null");
                this.produced++;
                this.actual.onNext(p);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }

        public void onError(Throwable t) {
            try {
                complete(ObjectHelper.requireNonNull(this.onErrorMapper.apply(t), "The onError publisher returned is null"));
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(t, e));
            }
        }

        public void onComplete() {
            try {
                complete(ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onComplete publisher returned is null"));
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }
    }

    public FlowableMapNotification(Flowable<T> source, Function<? super T, ? extends R> onNextMapper2, Function<? super Throwable, ? extends R> onErrorMapper2, Callable<? extends R> onCompleteSupplier2) {
        super(source);
        this.onNextMapper = onNextMapper2;
        this.onErrorMapper = onErrorMapper2;
        this.onCompleteSupplier = onCompleteSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new MapNotificationSubscriber<Object>(s, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
    }
}
