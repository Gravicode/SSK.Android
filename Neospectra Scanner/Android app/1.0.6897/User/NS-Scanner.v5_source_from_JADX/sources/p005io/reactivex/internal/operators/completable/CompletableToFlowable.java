package p005io.reactivex.internal.operators.completable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Flowable;
import p005io.reactivex.internal.observers.SubscriberCompletableObserver;

/* renamed from: io.reactivex.internal.operators.completable.CompletableToFlowable */
public final class CompletableToFlowable<T> extends Flowable<T> {
    final CompletableSource source;

    public CompletableToFlowable(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new SubscriberCompletableObserver<>(s));
    }
}
