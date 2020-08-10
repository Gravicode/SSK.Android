package p008rx.internal.operators;

import p008rx.Notification;
import p008rx.Notification.Kind;
import p008rx.Observable.Operator;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorDematerialize */
public final class OperatorDematerialize<T> implements Operator<T, Notification<T>> {

    /* renamed from: rx.internal.operators.OperatorDematerialize$2 */
    static /* synthetic */ class C16002 {
        static final /* synthetic */ int[] $SwitchMap$rx$Notification$Kind = new int[Kind.values().length];

        static {
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnNext.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnError.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnCompleted.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorDematerialize$Holder */
    static final class Holder {
        static final OperatorDematerialize<Object> INSTANCE = new OperatorDematerialize<>();

        Holder() {
        }
    }

    public static OperatorDematerialize instance() {
        return Holder.INSTANCE;
    }

    OperatorDematerialize() {
    }

    public Subscriber<? super Notification<T>> call(final Subscriber<? super T> child) {
        return new Subscriber<Notification<T>>(child) {
            boolean terminated;

            public void onNext(Notification<T> t) {
                switch (C16002.$SwitchMap$rx$Notification$Kind[t.getKind().ordinal()]) {
                    case 1:
                        if (!this.terminated) {
                            child.onNext(t.getValue());
                            return;
                        }
                        return;
                    case 2:
                        onError(t.getThrowable());
                        return;
                    case 3:
                        onCompleted();
                        return;
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unsupported notification type: ");
                        sb.append(t);
                        onError(new IllegalArgumentException(sb.toString()));
                        return;
                }
            }

            public void onError(Throwable e) {
                if (!this.terminated) {
                    this.terminated = true;
                    child.onError(e);
                }
            }

            public void onCompleted() {
                if (!this.terminated) {
                    this.terminated = true;
                    child.onCompleted();
                }
            }
        };
    }
}
