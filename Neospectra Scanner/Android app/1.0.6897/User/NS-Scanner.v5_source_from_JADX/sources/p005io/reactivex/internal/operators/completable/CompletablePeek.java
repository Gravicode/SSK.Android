package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletablePeek */
public final class CompletablePeek extends Completable {
    final Action onAfterTerminate;
    final Action onComplete;
    final Action onDispose;
    final Consumer<? super Throwable> onError;
    final Consumer<? super Disposable> onSubscribe;
    final Action onTerminate;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletablePeek$CompletableObserverImplementation */
    final class CompletableObserverImplementation implements CompletableObserver, Disposable {
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f112d;

        CompletableObserverImplementation(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            try {
                CompletablePeek.this.onSubscribe.accept(d);
                if (DisposableHelper.validate(this.f112d, d)) {
                    this.f112d = d;
                    this.actual.onSubscribe(this);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                d.dispose();
                this.f112d = DisposableHelper.DISPOSED;
                EmptyDisposable.error(ex, this.actual);
            }
        }

        public void onError(Throwable e) {
            if (this.f112d == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(e);
                return;
            }
            try {
                CompletablePeek.this.onError.accept(e);
                CompletablePeek.this.onTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.actual.onError(e);
            doAfter();
        }

        public void onComplete() {
            if (this.f112d != DisposableHelper.DISPOSED) {
                try {
                    CompletablePeek.this.onComplete.run();
                    CompletablePeek.this.onTerminate.run();
                    this.actual.onComplete();
                    doAfter();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.actual.onError(e);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void doAfter() {
            try {
                CompletablePeek.this.onAfterTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void dispose() {
            try {
                CompletablePeek.this.onDispose.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            this.f112d.dispose();
        }

        public boolean isDisposed() {
            return this.f112d.isDisposed();
        }
    }

    public CompletablePeek(CompletableSource source2, Consumer<? super Disposable> onSubscribe2, Consumer<? super Throwable> onError2, Action onComplete2, Action onTerminate2, Action onAfterTerminate2, Action onDispose2) {
        this.source = source2;
        this.onSubscribe = onSubscribe2;
        this.onError = onError2;
        this.onComplete = onComplete2;
        this.onTerminate = onTerminate2;
        this.onAfterTerminate = onAfterTerminate2;
        this.onDispose = onDispose2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new CompletableObserverImplementation(s));
    }
}
