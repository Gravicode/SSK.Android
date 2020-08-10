package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeAmb */
public final class MaybeAmb<T> extends Maybe<T> {
    private final MaybeSource<? extends T>[] sources;
    private final Iterable<? extends MaybeSource<? extends T>> sourcesIterable;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeAmb$AmbMaybeObserver */
    static final class AmbMaybeObserver<T> extends AtomicBoolean implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = -7044685185359438206L;
        final MaybeObserver<? super T> actual;
        final CompositeDisposable set = new CompositeDisposable();

        AmbMaybeObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.set.dispose();
            }
        }

        public boolean isDisposed() {
            return get();
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onSuccess(T value) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.actual.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.actual.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.actual.onComplete();
            }
        }
    }

    public MaybeAmb(MaybeSource<? extends T>[] sources2, Iterable<? extends MaybeSource<? extends T>> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        MaybeSource<? extends T>[] sources2 = this.sources;
        int count = 0;
        int i = 0;
        if (sources2 == null) {
            sources2 = new MaybeSource[8];
            try {
                for (MaybeSource<? extends T> element : this.sourcesIterable) {
                    if (element == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), observer);
                        return;
                    }
                    if (count == sources2.length) {
                        MaybeSource<? extends T>[] b = new MaybeSource[((count >> 2) + count)];
                        System.arraycopy(sources2, 0, b, 0, count);
                        sources2 = b;
                    }
                    int count2 = count + 1;
                    try {
                        sources2[count] = element;
                        count = count2;
                    } catch (Throwable th) {
                        e = th;
                        int i2 = count2;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, observer);
                        return;
                    }
                }
            } catch (Throwable th2) {
                e = th2;
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, observer);
                return;
            }
        } else {
            count = sources2.length;
        }
        AmbMaybeObserver<T> parent = new AmbMaybeObserver<>(observer);
        observer.onSubscribe(parent);
        while (i < count) {
            MaybeSource<? extends T> s = sources2[i];
            if (!parent.isDisposed()) {
                if (s == null) {
                    parent.onError(new NullPointerException("One of the MaybeSources is null"));
                    return;
                } else {
                    s.subscribe(parent);
                    i++;
                }
            } else {
                return;
            }
        }
        if (count == 0) {
            observer.onComplete();
        }
    }
}
