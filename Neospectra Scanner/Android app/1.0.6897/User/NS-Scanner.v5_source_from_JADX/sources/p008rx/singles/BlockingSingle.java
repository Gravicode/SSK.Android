package p008rx.singles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Single;
import p008rx.SingleSubscriber;
import p008rx.annotations.Beta;
import p008rx.exceptions.Exceptions;
import p008rx.internal.operators.BlockingOperatorToFuture;
import p008rx.internal.util.BlockingUtils;

@Beta
/* renamed from: rx.singles.BlockingSingle */
public final class BlockingSingle<T> {
    private final Single<? extends T> single;

    private BlockingSingle(Single<? extends T> single2) {
        this.single = single2;
    }

    public static <T> BlockingSingle<T> from(Single<? extends T> single2) {
        return new BlockingSingle<>(single2);
    }

    public T value() {
        final AtomicReference<T> returnItem = new AtomicReference<>();
        final AtomicReference<Throwable> returnException = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(latch, this.single.subscribe((SingleSubscriber<? super T>) new SingleSubscriber<T>() {
            public void onSuccess(T value) {
                returnItem.set(value);
                latch.countDown();
            }

            public void onError(Throwable error) {
                returnException.set(error);
                latch.countDown();
            }
        }));
        Throwable throwable = (Throwable) returnException.get();
        if (throwable == null) {
            return returnItem.get();
        }
        throw Exceptions.propagate(throwable);
    }

    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.single.toObservable());
    }
}
