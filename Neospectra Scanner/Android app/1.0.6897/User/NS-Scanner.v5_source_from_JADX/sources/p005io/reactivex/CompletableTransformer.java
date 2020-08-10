package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableTransformer */
public interface CompletableTransformer {
    @NonNull
    CompletableSource apply(@NonNull Completable completable);
}
