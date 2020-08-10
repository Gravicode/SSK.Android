package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.MaybeTransformer */
public interface MaybeTransformer<Upstream, Downstream> {
    @NonNull
    MaybeSource<Downstream> apply(@NonNull Maybe<Upstream> maybe);
}
