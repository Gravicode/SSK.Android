package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableResumeNext */
public final class CompletableResumeNext extends Completable {
    final Function<? super Throwable, ? extends CompletableSource> errorMapper;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableResumeNext$ResumeNext */
    final class ResumeNext implements CompletableObserver {

        /* renamed from: s */
        final CompletableObserver f113s;

        /* renamed from: sd */
        final SequentialDisposable f114sd;

        /* renamed from: io.reactivex.internal.operators.completable.CompletableResumeNext$ResumeNext$OnErrorObserver */
        final class OnErrorObserver implements CompletableObserver {
            OnErrorObserver() {
            }

            public void onComplete() {
                ResumeNext.this.f113s.onComplete();
            }

            public void onError(Throwable e) {
                ResumeNext.this.f113s.onError(e);
            }

            public void onSubscribe(Disposable d) {
                ResumeNext.this.f114sd.update(d);
            }
        }

        ResumeNext(CompletableObserver s, SequentialDisposable sd) {
            this.f113s = s;
            this.f114sd = sd;
        }

        public void onComplete() {
            this.f113s.onComplete();
        }

        public void onError(Throwable e) {
            try {
                CompletableSource c = (CompletableSource) CompletableResumeNext.this.errorMapper.apply(e);
                if (c == null) {
                    NullPointerException npe = new NullPointerException("The CompletableConsumable returned is null");
                    npe.initCause(e);
                    this.f113s.onError(npe);
                    return;
                }
                c.subscribe(new OnErrorObserver());
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f113s.onError(new CompositeException(ex, e));
            }
        }

        public void onSubscribe(Disposable d) {
            this.f114sd.update(d);
        }
    }

    public CompletableResumeNext(CompletableSource source2, Function<? super Throwable, ? extends CompletableSource> errorMapper2) {
        this.source = source2;
        this.errorMapper = errorMapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        SequentialDisposable sd = new SequentialDisposable();
        s.onSubscribe(sd);
        this.source.subscribe(new ResumeNext(s, sd));
    }
}
