package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.SingleOnSubscribe */
public interface SingleOnSubscribe<T> {
    void subscribe(@NonNull SingleEmitter<T> singleEmitter) throws Exception;
}
