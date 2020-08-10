package p005io.reactivex.observables;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.Observable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.SchedulerSupport;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.operators.observable.ObservableAutoConnect;
import p005io.reactivex.internal.operators.observable.ObservableRefCount;
import p005io.reactivex.internal.util.ConnectConsumer;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.observables.ConnectableObservable */
public abstract class ConnectableObservable<T> extends Observable<T> {
    public abstract void connect(@NonNull Consumer<? super Disposable> consumer);

    public final Disposable connect() {
        ConnectConsumer cc = new ConnectConsumer();
        connect(cc);
        return cc.disposable;
    }

    @CheckReturnValue
    @NonNull
    @SchedulerSupport("none")
    public Observable<T> refCount() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRefCount<T>(this));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Observable<T> refCount(int subscriberCount) {
        return refCount(subscriberCount, 0, TimeUnit.NANOSECONDS, Schedulers.trampoline());
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> refCount(long timeout, TimeUnit unit) {
        return refCount(1, timeout, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("custom")
    public final Observable<T> refCount(long timeout, TimeUnit unit, Scheduler scheduler) {
        return refCount(1, timeout, unit, scheduler);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> refCount(int subscriberCount, long timeout, TimeUnit unit) {
        return refCount(subscriberCount, timeout, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("custom")
    public final Observable<T> refCount(int subscriberCount, long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(subscriberCount, "subscriberCount");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableRefCount observableRefCount = new ObservableRefCount(this, subscriberCount, timeout, unit, scheduler);
        return RxJavaPlugins.onAssembly((Observable<T>) observableRefCount);
    }

    @NonNull
    public Observable<T> autoConnect() {
        return autoConnect(1);
    }

    @NonNull
    public Observable<T> autoConnect(int numberOfSubscribers) {
        return autoConnect(numberOfSubscribers, Functions.emptyConsumer());
    }

    @NonNull
    public Observable<T> autoConnect(int numberOfSubscribers, @NonNull Consumer<? super Disposable> connection) {
        if (numberOfSubscribers > 0) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableAutoConnect<T>(this, numberOfSubscribers, connection));
        }
        connect(connection);
        return RxJavaPlugins.onAssembly(this);
    }
}
