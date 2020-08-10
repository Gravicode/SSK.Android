package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.functions.Action1;

/* renamed from: rx.internal.operators.OperatorDoOnRequest */
public class OperatorDoOnRequest<T> implements Operator<T, T> {
    final Action1<? super Long> request;

    /* renamed from: rx.internal.operators.OperatorDoOnRequest$ParentSubscriber */
    static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;

        ParentSubscriber(Subscriber<? super T> child2) {
            this.child = child2;
            request(0);
        }

        /* access modifiers changed from: private */
        public void requestMore(long n) {
            request(n);
        }

        public void onCompleted() {
            this.child.onCompleted();
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(T t) {
            this.child.onNext(t);
        }
    }

    public OperatorDoOnRequest(Action1<? super Long> request2) {
        this.request = request2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child);
        child.setProducer(new Producer() {
            public void request(long n) {
                OperatorDoOnRequest.this.request.call(Long.valueOf(n));
                parent.requestMore(n);
            }
        });
        child.add(parent);
        return parent;
    }
}
