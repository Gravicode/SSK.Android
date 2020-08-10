package p008rx.subjects;

import java.util.ArrayList;
import java.util.List;
import p008rx.Observable.OnSubscribe;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action1;
import p008rx.internal.operators.NotificationLite;
import p008rx.internal.producers.SingleProducer;

/* renamed from: rx.subjects.AsyncSubject */
public final class AsyncSubject<T> extends Subject<T, T> {
    volatile Object lastValue;
    final SubjectSubscriptionManager<T> state;

    public static <T> AsyncSubject<T> create() {
        final SubjectSubscriptionManager<T> state2 = new SubjectSubscriptionManager<>();
        state2.onTerminated = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> o) {
                Object v = state2.getLatest();
                if (v == null || NotificationLite.isCompleted(v)) {
                    o.onCompleted();
                } else if (NotificationLite.isError(v)) {
                    o.onError(NotificationLite.getError(v));
                } else {
                    o.actual.setProducer(new SingleProducer(o.actual, NotificationLite.getValue(v)));
                }
            }
        };
        return new AsyncSubject<>(state2, state2);
    }

    protected AsyncSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state2) {
        super(onSubscribe);
        this.state = state2;
    }

    public void onCompleted() {
        SubjectObserver<T>[] arr$;
        if (this.state.active) {
            Object last = this.lastValue;
            if (last == null) {
                last = NotificationLite.completed();
            }
            for (SubjectObserver<T> bo : this.state.terminate(last)) {
                if (last == NotificationLite.completed()) {
                    bo.onCompleted();
                } else {
                    bo.actual.setProducer(new SingleProducer(bo.actual, NotificationLite.getValue(last)));
                }
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.active) {
            List<Throwable> errors = null;
            for (SubjectObserver<T> bo : this.state.terminate(NotificationLite.error(e))) {
                try {
                    bo.onError(e);
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    public void onNext(T v) {
        this.lastValue = NotificationLite.next(v);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    public boolean hasValue() {
        return !NotificationLite.isError(this.state.getLatest()) && NotificationLite.isNext(this.lastValue);
    }

    public boolean hasThrowable() {
        return NotificationLite.isError(this.state.getLatest());
    }

    public boolean hasCompleted() {
        Object o = this.state.getLatest();
        return o != null && !NotificationLite.isError(o);
    }

    public T getValue() {
        Object v = this.lastValue;
        if (NotificationLite.isError(this.state.getLatest()) || !NotificationLite.isNext(v)) {
            return null;
        }
        return NotificationLite.getValue(v);
    }

    public Throwable getThrowable() {
        Object o = this.state.getLatest();
        if (NotificationLite.isError(o)) {
            return NotificationLite.getError(o);
        }
        return null;
    }
}
