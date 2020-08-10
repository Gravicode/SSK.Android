package p005io.reactivex.internal.observers;

/* renamed from: io.reactivex.internal.observers.BlockingLastObserver */
public final class BlockingLastObserver<T> extends BlockingBaseObserver<T> {
    public void onNext(T t) {
        this.value = t;
    }

    public void onError(Throwable t) {
        this.value = null;
        this.error = t;
        countDown();
    }
}
