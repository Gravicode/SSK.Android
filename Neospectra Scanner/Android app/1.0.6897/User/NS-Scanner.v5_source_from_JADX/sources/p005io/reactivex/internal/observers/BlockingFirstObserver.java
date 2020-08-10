package p005io.reactivex.internal.observers;

/* renamed from: io.reactivex.internal.observers.BlockingFirstObserver */
public final class BlockingFirstObserver<T> extends BlockingBaseObserver<T> {
    public void onNext(T t) {
        if (this.value == null) {
            this.value = t;
            this.f79d.dispose();
            countDown();
        }
    }

    public void onError(Throwable t) {
        if (this.value == null) {
            this.error = t;
        }
        countDown();
    }
}
