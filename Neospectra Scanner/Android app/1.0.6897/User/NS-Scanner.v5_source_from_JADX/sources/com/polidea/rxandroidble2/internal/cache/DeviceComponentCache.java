package com.polidea.rxandroidble2.internal.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.ClientScope;
import com.polidea.rxandroidble2.internal.DeviceComponent;
import com.polidea.rxandroidble2.internal.cache.DeviceComponentWeakReference.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@ClientScope
public class DeviceComponentCache implements Map<String, DeviceComponent> {
    private final HashMap<String, DeviceComponentWeakReference> cache;
    private final Provider deviceComponentReferenceProvider;

    @Inject
    public DeviceComponentCache() {
        this(new Provider() {
            public DeviceComponentWeakReference provide(DeviceComponent device) {
                return new DeviceComponentWeakReference(device);
            }
        });
    }

    DeviceComponentCache(Provider provider) {
        this.cache = new HashMap<>();
        this.deviceComponentReferenceProvider = provider;
    }

    public void clear() {
        this.cache.clear();
    }

    public boolean containsKey(Object key) {
        return this.cache.containsKey(key) && get(key) != null;
    }

    public boolean containsValue(Object value) {
        for (DeviceComponentWeakReference weakReference : this.cache.values()) {
            if (weakReference.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public Set<Entry<String, DeviceComponent>> entrySet() {
        HashSet<Entry<String, DeviceComponent>> components = new HashSet<>();
        for (Entry<String, DeviceComponentWeakReference> entry : this.cache.entrySet()) {
            DeviceComponentWeakReference entryValue = (DeviceComponentWeakReference) entry.getValue();
            if (!entryValue.isEmpty()) {
                components.add(new CacheEntry((String) entry.getKey(), this.deviceComponentReferenceProvider.provide((DeviceComponent) entryValue.get())));
            }
        }
        return components;
    }

    @Nullable
    public DeviceComponent get(Object key) {
        DeviceComponentWeakReference deviceComponentWeakReference = (DeviceComponentWeakReference) this.cache.get(key);
        if (deviceComponentWeakReference != null) {
            return (DeviceComponent) deviceComponentWeakReference.get();
        }
        return null;
    }

    public boolean isEmpty() {
        evictEmptyReferences();
        return this.cache.isEmpty();
    }

    @NonNull
    public Set<String> keySet() {
        return this.cache.keySet();
    }

    public DeviceComponent put(String key, DeviceComponent value) {
        this.cache.put(key, this.deviceComponentReferenceProvider.provide(value));
        evictEmptyReferences();
        return value;
    }

    public void putAll(@NonNull Map<? extends String, ? extends DeviceComponent> map) {
        for (Entry<? extends String, ? extends DeviceComponent> entry : map.entrySet()) {
            put((String) entry.getKey(), (DeviceComponent) entry.getValue());
        }
    }

    public DeviceComponent remove(Object key) {
        DeviceComponentWeakReference deviceComponentWeakReference = (DeviceComponentWeakReference) this.cache.remove(key);
        evictEmptyReferences();
        if (deviceComponentWeakReference != null) {
            return (DeviceComponent) deviceComponentWeakReference.get();
        }
        return null;
    }

    public int size() {
        evictEmptyReferences();
        return this.cache.size();
    }

    @NonNull
    public Collection<DeviceComponent> values() {
        ArrayList<DeviceComponent> components = new ArrayList<>();
        for (DeviceComponentWeakReference deviceComponentWeakReference : this.cache.values()) {
            if (!deviceComponentWeakReference.isEmpty()) {
                components.add(deviceComponentWeakReference.get());
            }
        }
        return components;
    }

    private void evictEmptyReferences() {
        Iterator<Entry<String, DeviceComponentWeakReference>> iterator = this.cache.entrySet().iterator();
        while (iterator.hasNext()) {
            if (((DeviceComponentWeakReference) ((Entry) iterator.next()).getValue()).isEmpty()) {
                iterator.remove();
            }
        }
    }
}
