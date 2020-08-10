package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action1;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorOnBackpressureDrop */
public class OperatorOnBackpressureDrop<T> implements Operator<T, T> {
    final Action1<? super T> onDrop;

    /* renamed from: rx.internal.operators.OperatorOnBackpressureDrop$Holder */
    static final class Holder {
        static final OperatorOnBackpressureDrop<Object> INSTANCE = new OperatorOnBackpressureDrop<>();

        Holder() {
        }
    }

    public static <T> OperatorOnBackpressureDrop<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorOnBackpressureDrop() {
        this(null);
    }

    public OperatorOnBackpressureDrop(Action1<? super T> onDrop2) {
        this.onDrop = onDrop2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final AtomicLong requested = new AtomicLong();
        child.setProducer(new Producer() {
            public void request(long n) {
                BackpressureUtils.getAndAddRequest(requested, n);
            }
        });
        return new Subscriber<T>(child) {
            boolean done;

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    child.onCompleted();
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    this.done = true;
                    child.onError(e);
                    return;
                }
                RxJavaHooks.onError(e);
            }

            public void onNext(T t) {
                if (!this.done) {
                    if (requested.get() > 0) {
                        child.onNext(t);
                        requested.decrementAndGet();
                    } else if (OperatorOnBackpressureDrop.this.onDrop != null) {
                        try {
                            OperatorOnBackpressureDrop.this.onDrop.call(t);
                        } catch (Throwable e) {
                            Exceptions.throwOrReport(e, (Observer<?>) this, (Object) t);
                        }
                    }
                }
            }
        };
    }
}
