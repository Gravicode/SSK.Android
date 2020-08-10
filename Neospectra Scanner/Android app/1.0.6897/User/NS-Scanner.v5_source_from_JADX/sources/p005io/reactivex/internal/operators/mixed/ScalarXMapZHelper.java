package p005io.reactivex.internal.operators.mixed;

import java.util.concurrent.Callable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.operators.maybe.MaybeToObservable;
import p005io.reactivex.internal.operators.single.SingleToObservable;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.ScalarXMapZHelper */
final class ScalarXMapZHelper {
    private ScalarXMapZHelper() {
        throw new IllegalStateException("No instances!");
    }

    static <T> boolean tryAsCompletable(Object source, Function<? super T, ? extends CompletableSource> mapper, CompletableObserver observer) {
        if (!(source instanceof Callable)) {
            return false;
        }
        CompletableSource cs = null;
        try {
            T item = ((Callable) source).call();
            if (item != null) {
                cs = (CompletableSource) ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null CompletableSource");
            }
            if (cs == null) {
                EmptyDisposable.complete(observer);
            } else {
                cs.subscribe(observer);
            }
            return true;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }
    }

    static <T, R> boolean tryAsMaybe(Object source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, Observer<? super R> observer) {
        if (!(source instanceof Callable)) {
            return false;
        }
        MaybeSource maybeSource = null;
        try {
            T item = ((Callable) source).call();
            if (item != null) {
                maybeSource = (MaybeSource) ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null MaybeSource");
            }
            if (maybeSource == null) {
                EmptyDisposable.complete(observer);
            } else {
                maybeSource.subscribe(MaybeToObservable.create(observer));
            }
            return true;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }
    }

    static <T, R> boolean tryAsSingle(Object source, Function<? super T, ? extends SingleSource<? extends R>> mapper, Observer<? super R> observer) {
        if (!(source instanceof Callable)) {
            return false;
        }
        SingleSource singleSource = null;
        try {
            T item = ((Callable) source).call();
            if (item != null) {
                singleSource = (SingleSource) ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null SingleSource");
            }
            if (singleSource == null) {
                EmptyDisposable.complete(observer);
            } else {
                singleSource.subscribe(SingleToObservable.create(observer));
            }
            return true;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return true;
        }
    }
}
