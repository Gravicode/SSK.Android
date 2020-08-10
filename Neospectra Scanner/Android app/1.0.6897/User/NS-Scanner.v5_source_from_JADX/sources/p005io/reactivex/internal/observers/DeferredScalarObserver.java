package p005io.reactivex.internal.observers;

import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.observers.DeferredScalarObserver */
public abstract class DeferredScalarObserver<T, R> extends DeferredScalarDisposable<R> implements Observer<T> {
    private static final long serialVersionUID = -266195175408988651L;

    /* renamed from: s */
    protected Disposable f81s;

    public DeferredScalarObserver(Observer<? super R> actual) {
        super(actual);
    }

    public void onSubscribe(Disposable s) {
        if (DisposableHelper.validate(this.f81s, s)) {
            this.f81s = s;
            this.actual.onSubscribe(this);
        }
    }

    public void onError(Throwable t) {
        this.value = null;
        error(t);
    }

    public void onComplete() {
        R v = this.value;
        if (v != null) {
            this.value = null;
            complete(v);
            return;
        }
        complete();
    }

    public void dispose() {
        super.dispose();
        this.f81s.dispose();
    }
}
