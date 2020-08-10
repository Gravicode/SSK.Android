package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.flowables.ConnectableFlowable;
import p005io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableAutoConnect */
public final class FlowableAutoConnect<T> extends Flowable<T> {
    final AtomicInteger clients = new AtomicInteger();
    final Consumer<? super Disposable> connection;
    final int numberOfSubscribers;
    final ConnectableFlowable<? extends T> source;

    public FlowableAutoConnect(ConnectableFlowable<? extends T> source2, int numberOfSubscribers2, Consumer<? super Disposable> connection2) {
        this.source = source2;
        this.numberOfSubscribers = numberOfSubscribers2;
        this.connection = connection2;
    }

    public void subscribeActual(Subscriber<? super T> child) {
        this.source.subscribe(child);
        if (this.clients.incrementAndGet() == this.numberOfSubscribers) {
            this.source.connect(this.connection);
        }
    }
}
