package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.ObservableOperator */
public interface ObservableOperator<Downstream, Upstream> {
    @NonNull
    Observer<? super Upstream> apply(@NonNull Observer<? super Downstream> observer) throws Exception;
}
