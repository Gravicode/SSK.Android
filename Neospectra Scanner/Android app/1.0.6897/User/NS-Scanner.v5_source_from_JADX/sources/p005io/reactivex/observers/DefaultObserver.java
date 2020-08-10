package p005io.reactivex.observers;

import p005io.reactivex.Observer;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.observers.DefaultObserver */
public abstract class DefaultObserver<T> implements Observer<T> {

    /* renamed from: s */
    private Disposable f458s;

    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.validate(this.f458s, s, getClass())) {
            this.f458s = s;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Disposable s = this.f458s;
        this.f458s = DisposableHelper.DISPOSED;
        s.dispose();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }
}
