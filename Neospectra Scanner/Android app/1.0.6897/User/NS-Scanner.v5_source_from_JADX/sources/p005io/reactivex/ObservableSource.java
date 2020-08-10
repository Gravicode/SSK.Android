package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.ObservableSource */
public interface ObservableSource<T> {
    void subscribe(@NonNull Observer<? super T> observer);
}
