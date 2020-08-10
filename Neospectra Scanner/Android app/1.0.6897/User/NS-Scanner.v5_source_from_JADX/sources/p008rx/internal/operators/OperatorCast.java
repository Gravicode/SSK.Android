package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorThrowable;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorCast */
public class OperatorCast<T, R> implements Operator<R, T> {
    final Class<R> castClass;

    /* renamed from: rx.internal.operators.OperatorCast$CastSubscriber */
    static final class CastSubscriber<T, R> extends Subscriber<T> {
        final Subscriber<? super R> actual;
        final Class<R> castClass;
        boolean done;

        public CastSubscriber(Subscriber<? super R> actual2, Class<R> castClass2) {
            this.actual = actual2;
            this.castClass = castClass2;
        }

        public void onNext(T t) {
            try {
                this.actual.onNext(this.castClass.cast(t));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(ex, t));
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaHooks.onError(e);
                return;
            }
            this.done = true;
            this.actual.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.actual.onCompleted();
            }
        }

        public void setProducer(Producer p) {
            this.actual.setProducer(p);
        }
    }

    public OperatorCast(Class<R> castClass2) {
        this.castClass = castClass2;
    }

    public Subscriber<? super T> call(Subscriber<? super R> o) {
        CastSubscriber<T, R> parent = new CastSubscriber<>(o, this.castClass);
        o.add(parent);
        return parent;
    }
}
