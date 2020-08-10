package p005io.reactivex.internal.operators.single;

import java.util.Arrays;
import java.util.NoSuchElementException;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleZipIterable */
public final class SingleZipIterable<T, R> extends Single<R> {
    final Iterable<? extends SingleSource<? extends T>> sources;
    final Function<? super Object[], ? extends R> zipper;

    /* renamed from: io.reactivex.internal.operators.single.SingleZipIterable$SingletonArrayFunc */
    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(SingleZipIterable.this.zipper.apply(new Object[]{t}), "The zipper returned a null value");
        }
    }

    public SingleZipIterable(Iterable<? extends SingleSource<? extends T>> sources2, Function<? super Object[], ? extends R> zipper2) {
        this.sources = sources2;
        this.zipper = zipper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> observer) {
        SingleSource[] singleSourceArr = new SingleSource[8];
        int n = 0;
        try {
            for (SingleSource<? extends T> source : this.sources) {
                if (source == null) {
                    EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), observer);
                    return;
                }
                if (n == singleSourceArr.length) {
                    singleSourceArr = (SingleSource[]) Arrays.copyOf(singleSourceArr, (n >> 2) + n);
                }
                int n2 = n + 1;
                try {
                    singleSourceArr[n] = source;
                    n = n2;
                } catch (Throwable th) {
                    ex = th;
                    int i = n2;
                    Exceptions.throwIfFatal(ex);
                    EmptyDisposable.error(ex, observer);
                }
            }
            if (n == 0) {
                EmptyDisposable.error((Throwable) new NoSuchElementException(), observer);
            } else if (n == 1) {
                singleSourceArr[0].subscribe(new MapSingleObserver(observer, new SingletonArrayFunc()));
            } else {
                ZipCoordinator<T, R> parent = new ZipCoordinator<>(observer, n, this.zipper);
                observer.onSubscribe(parent);
                for (int i2 = 0; i2 < n && !parent.isDisposed(); i2++) {
                    singleSourceArr[i2].subscribe(parent.observers[i2]);
                }
            }
        } catch (Throwable th2) {
            ex = th2;
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }
}
