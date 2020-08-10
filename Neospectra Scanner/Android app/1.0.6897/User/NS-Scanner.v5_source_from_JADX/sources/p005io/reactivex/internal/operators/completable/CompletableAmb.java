package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableAmb */
public final class CompletableAmb extends Completable {
    private final CompletableSource[] sources;
    private final Iterable<? extends CompletableSource> sourcesIterable;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableAmb$Amb */
    static final class Amb implements CompletableObserver {
        private final AtomicBoolean once;

        /* renamed from: s */
        private final CompletableObserver f95s;
        private final CompositeDisposable set;

        Amb(AtomicBoolean once2, CompositeDisposable set2, CompletableObserver s) {
            this.once = once2;
            this.set = set2;
            this.f95s = s;
        }

        public void onComplete() {
            if (this.once.compareAndSet(false, true)) {
                this.set.dispose();
                this.f95s.onComplete();
            }
        }

        public void onError(Throwable e) {
            if (this.once.compareAndSet(false, true)) {
                this.set.dispose();
                this.f95s.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }
    }

    public CompletableAmb(CompletableSource[] sources2, Iterable<? extends CompletableSource> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    public void subscribeActual(CompletableObserver s) {
        CompletableSource[] sources2 = this.sources;
        int count = 0;
        if (sources2 == null) {
            sources2 = new CompletableSource[8];
            try {
                for (CompletableSource element : this.sourcesIterable) {
                    if (element == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), s);
                        return;
                    }
                    if (count == sources2.length) {
                        CompletableSource[] b = new CompletableSource[((count >> 2) + count)];
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
        s.onSubscribe(set);
        AtomicBoolean once = new AtomicBoolean();
        CompletableObserver inner = new Amb(once, set, s);
        int i2 = 0;
        while (i2 < count) {
            CompletableSource c = sources2[i2];
            if (!set.isDisposed()) {
                if (c == null) {
                    NullPointerException npe = new NullPointerException("One of the sources is null");
                    if (once.compareAndSet(false, true)) {
                        set.dispose();
                        s.onError(npe);
                    } else {
                        RxJavaPlugins.onError(npe);
                    }
                    return;
                }
                c.subscribe(inner);
                i2++;
            } else {
                return;
            }
        }
        if (count == 0) {
            s.onComplete();
        }
    }
}
