package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.ObservableOnSubscribe */
public interface ObservableOnSubscribe<T> {
    void subscribe(@NonNull ObservableEmitter<T> observableEmitter) throws Exception;
}
