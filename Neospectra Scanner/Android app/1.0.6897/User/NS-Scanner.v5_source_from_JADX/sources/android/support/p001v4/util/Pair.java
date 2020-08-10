package android.support.p001v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.apache.commons.math3.geometry.VectorFormat;

/* renamed from: android.support.v4.util.Pair */
public class Pair<F, S> {
    @Nullable
    public final F first;
    @Nullable
    public final S second;

    public Pair(@Nullable F first2, @Nullable S second2) {
        this.first = first2;
        this.second = second2;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair) o;
        if (objectsEqual(p.first, this.first) && objectsEqual(p.second, this.second)) {
            z = true;
        }
        return z;
    }

    private static boolean objectsEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.first == null ? 0 : this.first.hashCode();
        if (this.second != null) {
            i = this.second.hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append(VectorFormat.DEFAULT_SUFFIX);
        return sb.toString();
    }

    @NonNull
    public static <A, B> Pair<A, B> create(@Nullable A a, @Nullable B b) {
        return new Pair<>(a, b);
    }
}
