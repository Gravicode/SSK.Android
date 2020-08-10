package p008rx.schedulers;

/* renamed from: rx.schedulers.Timestamped */
public final class Timestamped<T> {
    private final long timestampMillis;
    private final T value;

    public Timestamped(long timestampMillis2, T value2) {
        this.value = value2;
        this.timestampMillis = timestampMillis2;
    }

    public long getTimestampMillis() {
        return this.timestampMillis;
    }

    public T getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Timestamped)) {
            return false;
        }
        Timestamped<?> other = (Timestamped) obj;
        if (this.timestampMillis != other.timestampMillis || (this.value != other.value && (this.value == null || !this.value.equals(other.value)))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (31 * ((31 * 1) + ((int) (this.timestampMillis ^ (this.timestampMillis >>> 32))))) + (this.value == null ? 0 : this.value.hashCode());
    }

    public String toString() {
        return String.format("Timestamped(timestampMillis = %d, value = %s)", new Object[]{Long.valueOf(this.timestampMillis), this.value.toString()});
    }
}
