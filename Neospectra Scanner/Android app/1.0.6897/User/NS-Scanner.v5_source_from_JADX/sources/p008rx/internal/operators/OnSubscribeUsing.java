package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeUsing */
public final class OnSubscribeUsing<T, Resource> implements OnSubscribe<T> {
    private final Action1<? super Resource> dispose;
    private final boolean disposeEagerly;
    private final Func1<? super Resource, ? extends Observable<? extends T>> observableFactory;
    private final Func0<Resource> resourceFactory;

    /* renamed from: rx.internal.operators.OnSubscribeUsing$DisposeAction */
    static final class DisposeAction<Resource> extends AtomicBoolean implements Action0, Subscription {
        private static final long serialVersionUID = 4262875056400218316L;
        private Action1<? super Resource> dispose;
        private Resource resource;

        DisposeAction(Action1<? super Resource> dispose2, Resource resource2) {
            this.dispose = dispose2;
            this.resource = resource2;
            lazySet(false);
        }

        public void call() {
            if (compareAndSet(false, true)) {
                try {
                    this.dispose.call(this.resource);
                } finally {
                    this.resource = null;
                    this.dispose = null;
                }
            }
        }

        public boolean isUnsubscribed() {
            return get();
        }

        public void unsubscribe() {
            call();
        }
    }

    public OnSubscribeUsing(Func0<Resource> resourceFactory2, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory2, Action1<? super Resource> dispose2, boolean disposeEagerly2) {
        this.resourceFactory = resourceFactory2;
        this.observableFactory = observableFactory2;
        this.dispose = dispose2;
        this.disposeEagerly = disposeEagerly2;
    }

    public void call(Subscriber<? super T> subscriber) {
        DisposeAction<Resource> disposeOnceOnly;
        Observable<? extends T> observable;
        try {
            Resource resource = this.resourceFactory.call();
            disposeOnceOnly = new DisposeAction<>(this.dispose, resource);
            subscriber.add(disposeOnceOnly);
            try {
                Observable<? extends T> source = (Observable) this.observableFactory.call(resource);
                if (this.disposeEagerly) {
                    observable = source.doOnTerminate(disposeOnceOnly);
                } else {
                    observable = source.doAfterTerminate(disposeOnceOnly);
                }
                observable.unsafeSubscribe(Subscribers.wrap(subscriber));
            } catch (Throwable e) {
                Throwable disposeError = dispose(disposeOnceOnly);
                Exceptions.throwIfFatal(e);
                Exceptions.throwIfFatal(disposeError);
                if (disposeError != null) {
                    subscriber.onError(new CompositeException(e, disposeError));
                } else {
                    subscriber.onError(e);
                }
            }
        } catch (Throwable e2) {
            Exceptions.throwOrReport(e2, (Observer<?>) subscriber);
        }
    }

    private Throwable dispose(Action0 disposeOnceOnly) {
        try {
            disposeOnceOnly.call();
            return null;
        } catch (Throwable e) {
            return e;
        }
    }
}
