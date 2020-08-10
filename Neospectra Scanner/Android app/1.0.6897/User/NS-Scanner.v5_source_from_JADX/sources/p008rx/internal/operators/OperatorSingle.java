package p008rx.internal.operators;

import java.util.NoSuchElementException;
import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.internal.producers.SingleProducer;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorSingle */
public final class OperatorSingle<T> implements Operator<T, T> {
    private final T defaultValue;
    private final boolean hasDefaultValue;

    /* renamed from: rx.internal.operators.OperatorSingle$Holder */
    static final class Holder {
        static final OperatorSingle<?> INSTANCE = new OperatorSingle<>();

        Holder() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorSingle$ParentSubscriber */
    static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;
        private final T defaultValue;
        private final boolean hasDefaultValue;
        private boolean hasTooManyElements;
        private boolean isNonEmpty;
        private T value;

        ParentSubscriber(Subscriber<? super T> child2, boolean hasDefaultValue2, T defaultValue2) {
            this.child = child2;
            this.hasDefaultValue = hasDefaultValue2;
            this.defaultValue = defaultValue2;
            request(2);
        }

        public void onNext(T value2) {
            if (this.hasTooManyElements) {
                return;
            }
            if (this.isNonEmpty) {
                this.hasTooManyElements = true;
                this.child.onError(new IllegalArgumentException("Sequence contains too many elements"));
                unsubscribe();
                return;
            }
            this.value = value2;
            this.isNonEmpty = true;
        }

        public void onCompleted() {
            if (this.hasTooManyElements) {
                return;
            }
            if (this.isNonEmpty) {
                this.child.setProducer(new SingleProducer(this.child, this.value));
            } else if (this.hasDefaultValue) {
                this.child.setProducer(new SingleProducer(this.child, this.defaultValue));
            } else {
                this.child.onError(new NoSuchElementException("Sequence contains no elements"));
            }
        }

        public void onError(Throwable e) {
            if (this.hasTooManyElements) {
                RxJavaHooks.onError(e);
            } else {
                this.child.onError(e);
            }
        }
    }

    public static <T> OperatorSingle<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorSingle() {
        this(false, null);
    }

    public OperatorSingle(T defaultValue2) {
        this(true, defaultValue2);
    }

    private OperatorSingle(boolean hasDefaultValue2, T defaultValue2) {
        this.hasDefaultValue = hasDefaultValue2;
        this.defaultValue = defaultValue2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ParentSubscriber<T> parent = new ParentSubscriber<>(child, this.hasDefaultValue, this.defaultValue);
        child.add(parent);
        return parent;
    }
}
