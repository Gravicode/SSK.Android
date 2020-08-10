package p005io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.ListCompositeDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.observers.ResourceMaybeObserver */
public abstract class ResourceMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    private final ListCompositeDisposable resources = new ListCompositeDisposable();

    /* renamed from: s */
    private final AtomicReference<Disposable> f464s = new AtomicReference<>();

    public final void add(@NonNull Disposable resource) {
        ObjectHelper.requireNonNull(resource, "resource is null");
        this.resources.add(resource);
    }

    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.f464s, s, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void dispose() {
        if (DisposableHelper.dispose(this.f464s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) this.f464s.get());
    }
}
