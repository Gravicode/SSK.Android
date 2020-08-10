package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorSkip */
public final class OperatorSkip<T> implements Operator<T, T> {
    final int toSkip;

    public OperatorSkip(int n) {
        if (n < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("n >= 0 required but it was ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.toSkip = n;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            int skipped;

            public void onCompleted() {
                child.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T t) {
                if (this.skipped >= OperatorSkip.this.toSkip) {
                    child.onNext(t);
                } else {
                    this.skipped++;
                }
            }

            public void setProducer(Producer producer) {
                child.setProducer(producer);
                producer.request((long) OperatorSkip.this.toSkip);
            }
        };
    }
}
