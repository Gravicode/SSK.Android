package p008rx.schedulers;

/* renamed from: rx.schedulers.TimeInterval */
public class TimeInterval<T> {
    private final long intervalInMilliseconds;
    private final T value;

    public TimeInterval(long intervalInMilliseconds2, T value2) {
        this.value = value2;
        this.intervalInMilliseconds = intervalInMilliseconds2;
    }

    public long getIntervalInMilliseconds() {
        return this.intervalInMilliseconds;
    }

    public T getValue() {
        return this.value;
    }

    public int hashCode() {
        return (31 * ((31 * 1) + ((int) (this.intervalInMilliseconds ^ (this.intervalInMilliseconds >>> 32))))) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimeInterval<?> other = (TimeInterval) obj;
        if (this.intervalInMilliseconds != other.intervalInMilliseconds) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeInterval [intervalInMilliseconds=");
        sb.append(this.intervalInMilliseconds);
        sb.append(", value=");
        sb.append(this.value);
        sb.append("]");
        return sb.toString();
    }
}
