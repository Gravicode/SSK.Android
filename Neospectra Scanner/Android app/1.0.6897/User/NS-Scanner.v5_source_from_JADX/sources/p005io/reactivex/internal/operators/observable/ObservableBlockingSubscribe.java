package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.observers.BlockingObserver;
import p005io.reactivex.internal.observers.LambdaObserver;
import p005io.reactivex.internal.util.BlockingHelper;
import p005io.reactivex.internal.util.BlockingIgnoringReceiver;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.internal.util.NotificationLite;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBlockingSubscribe */
public final class ObservableBlockingSubscribe {
    private ObservableBlockingSubscribe() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> void subscribe(ObservableSource<? extends T> o, Observer<? super T> observer) {
        BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        BlockingObserver<T> bs = new BlockingObserver<>(queue);
        observer.onSubscribe(bs);
        o.subscribe(bs);
        while (!bs.isDisposed()) {
            Object v = queue.poll();
            if (v == null) {
                try {
                    v = queue.take();
                } catch (InterruptedException ex) {
                    bs.dispose();
                    observer.onError(ex);
                    return;
                }
            }
            if (!bs.isDisposed() && o != BlockingObserver.TERMINATED) {
                if (NotificationLite.acceptFull(v, observer)) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public static <T> void subscribe(ObservableSource<? extends T> o) {
        BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
        LambdaObserver<T> ls = new LambdaObserver<>(Functions.emptyConsumer(), callback, callback, Functions.emptyConsumer());
        o.subscribe(ls);
        BlockingHelper.awaitForComplete(callback, ls);
        Throwable e = callback.error;
        if (e != null) {
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    public static <T> void subscribe(ObservableSource<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        subscribe(o, new LambdaObserver(onNext, onError, onComplete, Functions.emptyConsumer()));
    }
}
