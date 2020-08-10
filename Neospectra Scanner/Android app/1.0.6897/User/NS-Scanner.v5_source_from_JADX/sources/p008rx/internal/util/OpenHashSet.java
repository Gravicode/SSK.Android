package p008rx.internal.util;

import java.util.Arrays;
import p008rx.functions.Action1;
import p008rx.internal.util.unsafe.Pow2;

/* renamed from: rx.internal.util.OpenHashSet */
public final class OpenHashSet<T> {
    private static final int INT_PHI = -1640531527;
    T[] keys;
    final float loadFactor;
    int mask;
    int maxSize;
    int size;

    public OpenHashSet() {
        this(16, 0.75f);
    }

    public OpenHashSet(int capacity) {
        this(capacity, 0.75f);
    }

    public OpenHashSet(int capacity, float loadFactor2) {
        this.loadFactor = loadFactor2;
        int c = Pow2.roundToPowerOfTwo(capacity);
        this.mask = c - 1;
        this.maxSize = (int) (((float) c) * loadFactor2);
        this.keys = (Object[]) new Object[c];
    }

    public boolean add(T value) {
        T curr;
        T[] a = this.keys;
        int m = this.mask;
        int pos = mix(value.hashCode()) & m;
        T curr2 = a[pos];
        if (curr2 != null) {
            if (curr2.equals(value)) {
                return false;
            }
            do {
                pos = (pos + 1) & m;
                curr = a[pos];
                if (curr == null) {
                }
            } while (!curr.equals(value));
            return false;
        }
        a[pos] = value;
        int i = this.size + 1;
        this.size = i;
        if (i >= this.maxSize) {
            rehash();
        }
        return true;
    }

    public boolean remove(T value) {
        T curr;
        T[] a = this.keys;
        int m = this.mask;
        int pos = mix(value.hashCode()) & m;
        T curr2 = a[pos];
        if (curr2 == null) {
            return false;
        }
        if (curr2.equals(value)) {
            return removeEntry(pos, a, m);
        }
        do {
            pos = (pos + 1) & m;
            curr = a[pos];
            if (curr == null) {
                return false;
            }
        } while (!curr.equals(value));
        return removeEntry(pos, a, m);
    }

    /* access modifiers changed from: 0000 */
    public boolean removeEntry(int pos, T[] a, int m) {
        T curr;
        this.size--;
        while (true) {
            int last = pos;
            pos = (pos + 1) & m;
            while (true) {
                curr = a[pos];
                if (curr == null) {
                    a[last] = null;
                    return true;
                }
                int slot = mix(curr.hashCode()) & m;
                if (last <= pos) {
                    if (last >= slot || slot > pos) {
                        break;
                    }
                    pos = (pos + 1) & m;
                } else {
                    if (last >= slot && slot > pos) {
                        break;
                    }
                    pos = (pos + 1) & m;
                }
            }
            a[last] = curr;
        }
    }

    public void clear(Action1<? super T> clearAction) {
        if (this.size != 0) {
            T[] a = this.keys;
            for (T e : a) {
                if (e != null) {
                    clearAction.call(e);
                }
            }
            Arrays.fill(a, null);
            this.size = 0;
        }
    }

    public void terminate() {
        this.size = 0;
        this.keys = (Object[]) new Object[0];
    }

    /* access modifiers changed from: 0000 */
    public void rehash() {
        T[] a = this.keys;
        int i = a.length;
        int newCap = i << 1;
        int m = newCap - 1;
        T[] b = (Object[]) new Object[newCap];
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j != 0) {
                do {
                    i--;
                } while (a[i] == null);
                int pos = mix(a[i].hashCode()) & m;
                if (b[pos] != null) {
                    do {
                        pos = (pos + 1) & m;
                    } while (b[pos] != null);
                }
                b[pos] = a[i];
                j = j2;
            } else {
                this.mask = m;
                this.maxSize = (int) (((float) newCap) * this.loadFactor);
                this.keys = b;
                return;
            }
        }
    }

    static int mix(int x) {
        int h = INT_PHI * x;
        return (h >>> 16) ^ h;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public T[] values() {
        return this.keys;
    }
}
