package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;

/* renamed from: io.reactivex.internal.operators.completable.CompletableOnErrorComplete */
public final class CompletableOnErrorComplete extends Completable {
    final Predicate<? super Throwable> predicate;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableOnErrorComplete$OnError */
    final class OnError implements CompletableObserver {

        /* renamed from: s */
        private final CompletableObserver f111s;

        OnError(CompletableObserver s) {
            this.f111s = s;
        }

        public void onComplete() {
            this.f111s.onComplete();
        }

        public void onError(Throwable e) {
            try {
                if (CompletableOnErrorComplete.this.predicate.test(e)) {
                    this.f111s.onComplete();
                } else {
                    this.f111s.onError(e);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f111s.onError(new CompositeException(e, ex));
            }
        }

        public void onSubscribe(Disposable d) {
            this.f111s.onSubscribe(d);
        }
    }

    public CompletableOnErrorComplete(CompletableSource source2, Predicate<? super Throwable> predicate2) {
        this.source = source2;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new OnError(s));
    }
}
