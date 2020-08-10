package p005io.reactivex.internal.operators.parallel;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p005io.reactivex.parallel.ParallelFlowable;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelFromArray */
public final class ParallelFromArray<T> extends ParallelFlowable<T> {
    final Publisher<T>[] sources;

    public ParallelFromArray(Publisher<T>[] sources2) {
        this.sources = sources2;
    }

    public int parallelism() {
        return this.sources.length;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            for (int i = 0; i < n; i++) {
                this.sources[i].subscribe(subscribers[i]);
            }
        }
    }
}
