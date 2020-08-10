package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.AsyncEmitter.Cancellable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableEmitter;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action1;
import p008rx.internal.subscriptions.SequentialSubscription;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.CompletableFromEmitter */
public final class CompletableFromEmitter implements OnSubscribe {
    final Action1<CompletableEmitter> producer;

    /* renamed from: rx.internal.operators.CompletableFromEmitter$FromEmitter */
    static final class FromEmitter extends AtomicBoolean implements CompletableEmitter, Subscription {
        private static final long serialVersionUID = 5539301318568668881L;
        final CompletableSubscriber actual;
        final SequentialSubscription resource = new SequentialSubscription();

        public FromEmitter(CompletableSubscriber actual2) {
            this.actual = actual2;
        }

        public void onCompleted() {
            if (compareAndSet(false, true)) {
                try {
                    this.actual.onCompleted();
                } finally {
                    this.resource.unsubscribe();
                }
            }
        }

        public void onError(Throwable t) {
            if (compareAndSet(false, true)) {
                try {
                    this.actual.onError(t);
                } finally {
                    this.resource.unsubscribe();
                }
            } else {
                RxJavaHooks.onError(t);
            }
        }

        public void setSubscription(Subscription s) {
            this.resource.update(s);
        }

        public void setCancellation(Cancellable c) {
            setSubscription(new CancellableSubscription(c));
        }

        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                this.resource.unsubscribe();
            }
        }

        public boolean isUnsubscribed() {
            return get();
        }
    }

    public CompletableFromEmitter(Action1<CompletableEmitter> producer2) {
        this.producer = producer2;
    }

    public void call(CompletableSubscriber t) {
        FromEmitter emitter = new FromEmitter(t);
        t.onSubscribe(emitter);
        try {
            this.producer.call(emitter);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            emitter.onError(ex);
        }
    }
}
