package p008rx.observers;

import p008rx.Observer;
import p008rx.exceptions.OnErrorNotImplementedException;
import p008rx.functions.Action0;
import p008rx.functions.Action1;

/* renamed from: rx.observers.Observers */
public final class Observers {
    private static final Observer<Object> EMPTY = new Observer<Object>() {
        public final void onCompleted() {
        }

        public final void onError(Throwable e) {
            throw new OnErrorNotImplementedException(e);
        }

        public final void onNext(Object args) {
        }
    };

    private Observers() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Observer<T> empty() {
        return EMPTY;
    }

    public static <T> Observer<T> create(final Action1<? super T> onNext) {
        if (onNext != null) {
            return new Observer<T>() {
                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    throw new OnErrorNotImplementedException(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            };
        }
        throw new IllegalArgumentException("onNext can not be null");
    }

    public static <T> Observer<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError != null) {
            return new Observer<T>() {
                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            };
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public static <T> Observer<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        } else if (onComplete != null) {
            return new Observer<T>() {
                public final void onCompleted() {
                    onComplete.call();
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            };
        } else {
            throw new IllegalArgumentException("onComplete can not be null");
        }
    }
}
