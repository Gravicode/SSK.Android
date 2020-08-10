package p005io.reactivex.internal.operators.maybe;

import java.util.Arrays;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeZipIterable */
public final class MaybeZipIterable<T, R> extends Maybe<R> {
    final Iterable<? extends MaybeSource<? extends T>> sources;
    final Function<? super Object[], ? extends R> zipper;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeZipIterable$SingletonArrayFunc */
    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(MaybeZipIterable.this.zipper.apply(new Object[]{t}), "The zipper returned a null value");
        }
    }

    public MaybeZipIterable(Iterable<? extends MaybeSource<? extends T>> sources2, Function<? super Object[], ? extends R> zipper2) {
        this.sources = sources2;
        this.zipper = zipper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> observer) {
        MaybeSource[] maybeSourceArr = new MaybeSource[8];
        int n = 0;
        try {
            for (MaybeSource<? extends T> source : this.sources) {
                if (source == null) {
                    EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), observer);
                    return;
                }
                if (n == maybeSourceArr.length) {
                    maybeSourceArr = (MaybeSource[]) Arrays.copyOf(maybeSourceArr, (n >> 2) + n);
                }
                int n2 = n + 1;
                try {
                    maybeSourceArr[n] = source;
                    n = n2;
                } catch (Throwable th) {
                    ex = th;
                    int i = n2;
                    Exceptions.throwIfFatal(ex);
                    EmptyDisposable.error(ex, observer);
                }
            }
            if (n == 0) {
                EmptyDisposable.complete(observer);
            } else if (n == 1) {
                maybeSourceArr[0].subscribe(new MapMaybeObserver(observer, new SingletonArrayFunc()));
            } else {
                ZipCoordinator<T, R> parent = new ZipCoordinator<>(observer, n, this.zipper);
                observer.onSubscribe(parent);
                for (int i2 = 0; i2 < n && !parent.isDisposed(); i2++) {
                    maybeSourceArr[i2].subscribe(parent.observers[i2]);
                }
            }
        } catch (Throwable th2) {
            ex = th2;
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }
}
