package com.polidea.rxandroidble2.internal.cache;

import com.polidea.rxandroidble2.internal.DeviceComponent;
import java.util.Map.Entry;

class CacheEntry implements Entry<String, DeviceComponent> {
    private final DeviceComponentWeakReference deviceComponentWeakReference;
    private final String string;

    CacheEntry(String string2, DeviceComponentWeakReference deviceComponentWeakReference2) {
        this.string = string2;
        this.deviceComponentWeakReference = deviceComponentWeakReference2;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof CacheEntry)) {
            return false;
        }
        CacheEntry that = (CacheEntry) o;
        if (!this.string.equals(that.getKey()) || !this.deviceComponentWeakReference.equals(that.deviceComponentWeakReference)) {
            z = false;
        }
        return z;
    }

    public String getKey() {
        return this.string;
    }

    public DeviceComponent getValue() {
        return (DeviceComponent) this.deviceComponentWeakReference.get();
    }

    public int hashCode() {
        return (this.string.hashCode() * 31) + this.deviceComponentWeakReference.hashCode();
    }

    public DeviceComponent setValue(DeviceComponent object) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
