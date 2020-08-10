package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableSource */
public interface CompletableSource {
    void subscribe(@NonNull CompletableObserver completableObserver);
}
