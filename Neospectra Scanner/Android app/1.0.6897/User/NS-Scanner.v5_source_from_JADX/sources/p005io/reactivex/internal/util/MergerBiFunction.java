package p005io.reactivex.internal.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import p005io.reactivex.functions.BiFunction;

/* renamed from: io.reactivex.internal.util.MergerBiFunction */
public final class MergerBiFunction<T> implements BiFunction<List<T>, List<T>, List<T>> {
    final Comparator<? super T> comparator;

    public MergerBiFunction(Comparator<? super T> comparator2) {
        this.comparator = comparator2;
    }

    public List<T> apply(List<T> a, List<T> b) throws Exception {
        int n = a.size() + b.size();
        if (n == 0) {
            return new ArrayList();
        }
        List<T> both = new ArrayList<>(n);
        Iterator<T> at = a.iterator();
        Iterator<T> bt = b.iterator();
        Object next = at.hasNext() ? at.next() : null;
        Object next2 = bt.hasNext() ? bt.next() : null;
        while (next != null && next2 != null) {
            if (this.comparator.compare(next, next2) < 0) {
                both.add(next);
                next = at.hasNext() ? at.next() : null;
            } else {
                both.add(next2);
                next2 = bt.hasNext() ? bt.next() : null;
            }
        }
        if (next != null) {
            both.add(next);
            while (at.hasNext()) {
                both.add(at.next());
            }
        } else {
            both.add(next2);
            while (bt.hasNext()) {
                both.add(bt.next());
            }
        }
        return both;
    }
}
