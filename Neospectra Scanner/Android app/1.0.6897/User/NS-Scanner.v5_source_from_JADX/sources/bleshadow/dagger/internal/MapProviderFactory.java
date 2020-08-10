package bleshadow.dagger.internal;

import bleshadow.dagger.Lazy;
import bleshadow.javax.inject.Provider;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MapProviderFactory<K, V> implements Factory<Map<K, Provider<V>>>, Lazy<Map<K, Provider<V>>> {
    private final Map<K, Provider<V>> contributingMap;

    public static final class Builder<K, V> {
        private final LinkedHashMap<K, Provider<V>> map;

        private Builder(int size) {
            this.map = DaggerCollections.newLinkedHashMapWithExpectedSize(size);
        }

        public Builder<K, V> put(K key, Provider<V> providerOfValue) {
            this.map.put(Preconditions.checkNotNull(key, "key"), Preconditions.checkNotNull(providerOfValue, "provider"));
            return this;
        }

        public MapProviderFactory<K, V> build() {
            return new MapProviderFactory<>(this.map);
        }
    }

    public static <K, V> Builder<K, V> builder(int size) {
        return new Builder<>(size);
    }

    private MapProviderFactory(Map<K, Provider<V>> contributingMap2) {
        this.contributingMap = Collections.unmodifiableMap(contributingMap2);
    }

    public Map<K, Provider<V>> get() {
        return this.contributingMap;
    }
}
