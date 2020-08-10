package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.OperatorTakeUntilPredicate */
public final class OperatorTakeUntilPredicate<T> implements Operator<T, T> {
    final Func1<? super T, Boolean> stopPredicate;

    /* renamed from: rx.internal.operators.OperatorTakeUntilPredicate$ParentSubscriber */
    final class ParentSubscriber extends Subscriber<T> {
        private final Subscriber<? super T> child;
        private boolean done;

        ParentSubscriber(Subscriber<? super T> child2) {
            this.child = child2;
        }

        public void onNext(T t) {
            this.child.onNext(t);
            try {
                if (((Boolean) OperatorTakeUntilPredicate.this.stopPredicate.call(t)).booleanValue()) {
                    this.done = true;
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable e) {
                this.done = true;
                Exceptions.throwOrReport(e, (Observer<?>) this.child, (Object) t);
                unsubscribe();
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.child.onError(e);
            }
        }

        /* access modifiers changed from: 0000 */
        public void downstreamRequest(long n) {
            request(n);
        }
    }

    public OperatorTakeUntilPredicate(Func1<? super T, Boolean> stopPredicate2) {
        this.stopPredicate = stopPredicate2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final ParentSubscriber parent = new ParentSubscriber<>(child);
        child.add(parent);
        child.setProducer(new Producer() {
            public void request(long n) {
                parent.downstreamRequest(n);
            }
        });
        return parent;
    }
}
