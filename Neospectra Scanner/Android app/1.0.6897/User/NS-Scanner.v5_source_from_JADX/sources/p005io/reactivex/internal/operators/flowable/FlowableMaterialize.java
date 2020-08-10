package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Notification;
import p005io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableMaterialize */
public final class FlowableMaterialize<T> extends AbstractFlowableWithUpstream<T, Notification<T>> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableMaterialize$MaterializeSubscriber */
    static final class MaterializeSubscriber<T> extends SinglePostCompleteSubscriber<T, Notification<T>> {
        private static final long serialVersionUID = -3740826063558713822L;

        MaterializeSubscriber(Subscriber<? super Notification<T>> actual) {
            super(actual);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(Notification.createOnNext(t));
        }

        public void onError(Throwable t) {
            complete(Notification.createOnError(t));
        }

        public void onComplete() {
            complete(Notification.createOnComplete());
        }

        /* access modifiers changed from: protected */
        public void onDrop(Notification<T> n) {
            if (n.isOnError()) {
                RxJavaPlugins.onError(n.getError());
            }
        }
    }

    public FlowableMaterialize(Flowable<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Notification<T>> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new MaterializeSubscriber<Object>(s));
    }
}
