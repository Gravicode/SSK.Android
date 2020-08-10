package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableOnSubscribe */
public interface CompletableOnSubscribe {
    void subscribe(@NonNull CompletableEmitter completableEmitter) throws Exception;
}
