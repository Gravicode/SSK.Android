package p005io.reactivex.observables;

import p005io.reactivex.Observable;
import p005io.reactivex.annotations.Nullable;

/* renamed from: io.reactivex.observables.GroupedObservable */
public abstract class GroupedObservable<K, T> extends Observable<T> {
    final K key;

    protected GroupedObservable(@Nullable K key2) {
        this.key = key2;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }
}
