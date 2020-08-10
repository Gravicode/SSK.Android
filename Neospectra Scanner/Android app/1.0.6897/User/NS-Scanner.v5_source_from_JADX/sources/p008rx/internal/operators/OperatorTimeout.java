package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.OperatorTimeout */
public final class OperatorTimeout<T> extends OperatorTimeoutBase<T> {
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeout(final long timeout, final TimeUnit timeUnit, Observable<? extends T> other, Scheduler scheduler) {
        super(new FirstTimeoutStub<T>() {
            public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Worker inner) {
                return inner.schedule(new Action0() {
                    public void call() {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }
                }, timeout, timeUnit);
            }
        }, new TimeoutStub<T>() {
            public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T t, Worker inner) {
                return inner.schedule(new Action0() {
                    public void call() {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }
                }, timeout, timeUnit);
            }
        }, other, scheduler);
    }
}
