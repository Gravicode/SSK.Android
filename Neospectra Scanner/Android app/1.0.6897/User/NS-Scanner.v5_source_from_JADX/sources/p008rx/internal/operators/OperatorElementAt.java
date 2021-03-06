package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorElementAt */
public final class OperatorElementAt<T> implements Operator<T, T> {
    final T defaultValue;
    final boolean hasDefault;
    final int index;

    /* renamed from: rx.internal.operators.OperatorElementAt$InnerProducer */
    static class InnerProducer extends AtomicBoolean implements Producer {
        private static final long serialVersionUID = 1;
        final Producer actual;

        public InnerProducer(Producer actual2) {
            this.actual = actual2;
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (n > 0 && compareAndSet(false, true)) {
                this.actual.request(Long.MAX_VALUE);
            }
        }
    }

    public OperatorElementAt(int index2) {
        this(index2, null, false);
    }

    public OperatorElementAt(int index2, T defaultValue2) {
        this(index2, defaultValue2, true);
    }

    private OperatorElementAt(int index2, T defaultValue2, boolean hasDefault2) {
        if (index2 < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(index2);
            sb.append(" is out of bounds");
            throw new IndexOutOfBoundsException(sb.toString());
        }
        this.index = index2;
        this.defaultValue = defaultValue2;
        this.hasDefault = hasDefault2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            private int currentIndex;

            public void onNext(T value) {
                int i = this.currentIndex;
                this.currentIndex = i + 1;
                if (i == OperatorElementAt.this.index) {
                    child.onNext(value);
                    child.onCompleted();
                    unsubscribe();
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                if (this.currentIndex > OperatorElementAt.this.index) {
                    return;
                }
                if (OperatorElementAt.this.hasDefault) {
                    child.onNext(OperatorElementAt.this.defaultValue);
                    child.onCompleted();
                    return;
                }
                Subscriber subscriber = child;
                StringBuilder sb = new StringBuilder();
                sb.append(OperatorElementAt.this.index);
                sb.append(" is out of bounds");
                subscriber.onError(new IndexOutOfBoundsException(sb.toString()));
            }

            public void setProducer(Producer p) {
                child.setProducer(new InnerProducer(p));
            }
        };
        child.add(parent);
        return parent;
    }
}
