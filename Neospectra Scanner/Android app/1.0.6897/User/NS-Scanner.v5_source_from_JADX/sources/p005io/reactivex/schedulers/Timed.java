package p005io.reactivex.schedulers;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.schedulers.Timed */
public final class Timed<T> {
    final long time;
    final TimeUnit unit;
    final T value;

    public Timed(@NonNull T value2, long time2, @NonNull TimeUnit unit2) {
        this.value = value2;
        this.time = time2;
        this.unit = (TimeUnit) ObjectHelper.requireNonNull(unit2, "unit is null");
    }

    @NonNull
    public T value() {
        return this.value;
    }

    @NonNull
    public TimeUnit unit() {
        return this.unit;
    }

    public long time() {
        return this.time;
    }

    public long time(@NonNull TimeUnit unit2) {
        return unit2.convert(this.time, this.unit);
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof Timed)) {
            return false;
        }
        Timed<?> o = (Timed) other;
        if (ObjectHelper.equals(this.value, o.value) && this.time == o.time && ObjectHelper.equals(this.unit, o.unit)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return ((((this.value != null ? this.value.hashCode() : 0) * 31) + ((int) ((this.time >>> 31) ^ this.time))) * 31) + this.unit.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Timed[time=");
        sb.append(this.time);
        sb.append(", unit=");
        sb.append(this.unit);
        sb.append(", value=");
        sb.append(this.value);
        sb.append("]");
        return sb.toString();
    }
}
