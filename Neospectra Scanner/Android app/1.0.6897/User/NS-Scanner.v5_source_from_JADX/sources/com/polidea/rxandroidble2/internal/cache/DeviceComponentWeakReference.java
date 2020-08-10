package com.polidea.rxandroidble2.internal.cache;

import com.polidea.rxandroidble2.internal.DeviceComponent;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

class DeviceComponentWeakReference extends WeakReference<DeviceComponent> {

    public interface Provider {
        DeviceComponentWeakReference provide(DeviceComponent deviceComponent);
    }

    DeviceComponentWeakReference(DeviceComponent device) {
        super(device);
    }

    DeviceComponentWeakReference(DeviceComponent r, ReferenceQueue<? super DeviceComponent> q) {
        super(r, q);
    }

    /* access modifiers changed from: 0000 */
    public boolean contains(Object object) {
        DeviceComponent thisDevice = (DeviceComponent) get();
        return (object instanceof DeviceComponent) && thisDevice != null && thisDevice.provideDevice() == ((DeviceComponent) object).provideDevice();
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof WeakReference)) {
            return false;
        }
        DeviceComponent thisComponent = (DeviceComponent) get();
        Object otherThing = ((WeakReference) o).get();
        if (thisComponent != null && (otherThing instanceof DeviceComponent) && thisComponent.provideDevice().equals(((DeviceComponent) otherThing).provideDevice())) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        if (get() != null) {
            return ((DeviceComponent) get()).hashCode();
        }
        return 0;
    }

    public boolean isEmpty() {
        return get() == null;
    }
}
