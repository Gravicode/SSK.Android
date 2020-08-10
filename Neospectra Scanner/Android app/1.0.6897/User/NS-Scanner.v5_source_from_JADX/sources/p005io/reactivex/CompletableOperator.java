package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableOperator */
public interface CompletableOperator {
    @NonNull
    CompletableObserver apply(@NonNull CompletableObserver completableObserver) throws Exception;
}
