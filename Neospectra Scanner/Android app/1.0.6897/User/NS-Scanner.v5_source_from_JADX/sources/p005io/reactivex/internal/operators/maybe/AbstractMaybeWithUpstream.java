package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/* renamed from: io.reactivex.internal.operators.maybe.AbstractMaybeWithUpstream */
abstract class AbstractMaybeWithUpstream<T, R> extends Maybe<R> implements HasUpstreamMaybeSource<T> {
    protected final MaybeSource<T> source;

    AbstractMaybeWithUpstream(MaybeSource<T> source2) {
        this.source = source2;
    }

    public final MaybeSource<T> source() {
        return this.source;
    }
}
