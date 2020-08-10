package p005io.reactivex.disposables;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.disposables.RunnableDisposable */
final class RunnableDisposable extends ReferenceDisposable<Runnable> {
    private static final long serialVersionUID = -8219729196779211169L;

    RunnableDisposable(Runnable value) {
        super(value);
    }

    /* access modifiers changed from: protected */
    public void onDisposed(@NonNull Runnable value) {
        value.run();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RunnableDisposable(disposed=");
        sb.append(isDisposed());
        sb.append(", ");
        sb.append(get());
        sb.append(")");
        return sb.toString();
    }
}
