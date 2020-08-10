package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableEmitter;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableOnSubscribe;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Cancellable;
import p005io.reactivex.internal.disposables.CancellableDisposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableCreate */
public final class CompletableCreate extends Completable {
    final CompletableOnSubscribe source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableCreate$Emitter */
    static final class Emitter extends AtomicReference<Disposable> implements CompletableEmitter, Disposable {
        private static final long serialVersionUID = -2467358622224974244L;
        final CompletableObserver actual;

        Emitter(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void onComplete() {
            if (get() != DisposableHelper.DISPOSED) {
                Disposable d = (Disposable) getAndSet(DisposableHelper.DISPOSED);
                if (d != DisposableHelper.DISPOSED) {
                    try {
                        this.actual.onComplete();
                    } finally {
                        if (d != null) {
                            d.dispose();
                        }
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (!tryOnError(t)) {
                RxJavaPlugins.onError(t);
            }
        }

        public boolean tryOnError(Throwable t) {
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            if (get() != DisposableHelper.DISPOSED) {
                Disposable d = (Disposable) getAndSet(DisposableHelper.DISPOSED);
                if (d != DisposableHelper.DISPOSED) {
                    try {
                        this.actual.onError(t);
                        return true;
                    } finally {
                        if (d != null) {
                            d.dispose();
                        }
                    }
                }
            }
            return false;
        }

        public void setDisposable(Disposable d) {
            DisposableHelper.set(this, d);
        }

        public void setCancellable(Cancellable c) {
            setDisposable(new CancellableDisposable(c));
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public String toString() {
            return String.format("%s{%s}", new Object[]{getClass().getSimpleName(), super.toString()});
        }
    }

    public CompletableCreate(CompletableOnSubscribe source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        Emitter parent = new Emitter(s);
        s.onSubscribe(parent);
        try {
            this.source.subscribe(parent);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            parent.onError(ex);
        }
    }
}
