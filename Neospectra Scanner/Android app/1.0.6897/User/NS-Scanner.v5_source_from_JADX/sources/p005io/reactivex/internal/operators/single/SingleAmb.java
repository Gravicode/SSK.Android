package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleAmb */
public final class SingleAmb<T> extends Single<T> {
    private final SingleSource<? extends T>[] sources;
    private final Iterable<? extends SingleSource<? extends T>> sourcesIterable;

    /* renamed from: io.reactivex.internal.operators.single.SingleAmb$AmbSingleObserver */
    static final class AmbSingleObserver<T> extends AtomicBoolean implements SingleObserver<T> {
        private static final long serialVersionUID = -1944085461036028108L;

        /* renamed from: s */
        final SingleObserver<? super T> f399s;
        final CompositeDisposable set;

        AmbSingleObserver(SingleObserver<? super T> s, CompositeDisposable set2) {
            this.f399s = s;
            this.set = set2;
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onSuccess(T value) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.f399s.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.f399s.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }
    }

    public SingleAmb(SingleSource<? extends T>[] sources2, Iterable<? extends SingleSource<? extends T>> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        SingleSource<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 == null) {
            sources2 = new SingleSource[8];
            try {
                for (SingleSource<? extends T> element : this.sourcesIterable) {
                    if (element == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), s);
                        return;
                    }
                    if (count == sources2.length) {
                        SingleSource<? extends T>[] b = new SingleSource[((count >> 2) + count)];
                        System.arraycopy(sources2, 0, b, 0, count);
                        sources2 = b;
                    }
                    int count2 = count + 1;
                    try {
                        sources2[count] = element;
                        count = count2;
                    } catch (Throwable th) {
                        e = th;
                        int i = count2;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, s);
                        return;
                    }
                }
            } catch (Throwable th2) {
                e = th2;
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, s);
                return;
            }
        } else {
            count = sources2.length;
        }
        CompositeDisposable set = new CompositeDisposable();
        AmbSingleObserver<T> shared = new AmbSingleObserver<>(s, set);
        s.onSubscribe(set);
        int i2 = 0;
        while (i2 < count) {
            SingleSource<? extends T> s1 = sources2[i2];
            if (!shared.get()) {
                if (s1 == null) {
                    set.dispose();
                    Throwable e = new NullPointerException("One of the sources is null");
                    if (shared.compareAndSet(false, true)) {
                        s.onError(e);
                    } else {
                        RxJavaPlugins.onError(e);
                    }
                    return;
                }
                s1.subscribe(shared);
                i2++;
            } else {
                return;
            }
        }
    }
}
