package com.trello.rxlifecycle2;

import java.util.concurrent.CancellationException;
import p005io.reactivex.Completable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

final class Functions {
    static final Function<Object, Completable> CANCEL_COMPLETABLE = new Function<Object, Completable>() {
        public Completable apply(Object ignore) throws Exception {
            return Completable.error((Throwable) new CancellationException());
        }
    };
    static final Function<Throwable, Boolean> RESUME_FUNCTION = new Function<Throwable, Boolean>() {
        public Boolean apply(Throwable throwable) throws Exception {
            if (throwable instanceof OutsideLifecycleException) {
                return Boolean.valueOf(true);
            }
            Exceptions.propagate(throwable);
            return Boolean.valueOf(false);
        }
    };
    static final Predicate<Boolean> SHOULD_COMPLETE = new Predicate<Boolean>() {
        public boolean test(Boolean shouldComplete) throws Exception {
            return shouldComplete.booleanValue();
        }
    };

    private Functions() {
        throw new AssertionError("No instances!");
    }
}
