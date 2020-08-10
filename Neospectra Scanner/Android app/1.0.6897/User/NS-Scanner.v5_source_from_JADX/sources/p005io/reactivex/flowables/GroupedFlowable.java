package p005io.reactivex.flowables;

import p005io.reactivex.Flowable;
import p005io.reactivex.annotations.Nullable;

/* renamed from: io.reactivex.flowables.GroupedFlowable */
public abstract class GroupedFlowable<K, T> extends Flowable<T> {
    final K key;

    protected GroupedFlowable(@Nullable K key2) {
        this.key = key2;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }
}
