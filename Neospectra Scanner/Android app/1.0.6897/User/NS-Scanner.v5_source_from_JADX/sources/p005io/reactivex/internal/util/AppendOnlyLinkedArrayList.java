package p005io.reactivex.internal.util;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Observer;
import p005io.reactivex.functions.BiPredicate;
import p005io.reactivex.functions.Predicate;

/* renamed from: io.reactivex.internal.util.AppendOnlyLinkedArrayList */
public class AppendOnlyLinkedArrayList<T> {
    final int capacity;
    final Object[] head;
    int offset;
    Object[] tail = this.head;

    /* renamed from: io.reactivex.internal.util.AppendOnlyLinkedArrayList$NonThrowingPredicate */
    public interface NonThrowingPredicate<T> extends Predicate<T> {
        boolean test(T t);
    }

    public AppendOnlyLinkedArrayList(int capacity2) {
        this.capacity = capacity2;
        this.head = new Object[(capacity2 + 1)];
    }

    public void add(T value) {
        int c = this.capacity;
        int o = this.offset;
        if (o == c) {
            Object[] next = new Object[(c + 1)];
            this.tail[c] = next;
            this.tail = next;
            o = 0;
        }
        this.tail[o] = value;
        this.offset = o + 1;
    }

    public void setFirst(T value) {
        this.head[0] = value;
    }

    public void forEachWhile(NonThrowingPredicate<? super T> consumer) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = a[c]) {
            int i = 0;
            while (i < c) {
                Object[] objArr = a[i];
                if (objArr == null) {
                    continue;
                    break;
                } else if (!consumer.test(objArr)) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public <U> boolean accept(Subscriber<? super U> subscriber) {
        Object[] a = this.head;
        int c = this.capacity;
        while (true) {
            int i = 0;
            if (a == null) {
                return false;
            }
            while (i < c) {
                Object[] objArr = a[i];
                if (objArr == null) {
                    continue;
                    break;
                } else if (NotificationLite.acceptFull((Object) objArr, subscriber)) {
                    return true;
                } else {
                    i++;
                }
            }
            a = a[c];
        }
    }

    public <U> boolean accept(Observer<? super U> observer) {
        Object[] a = this.head;
        int c = this.capacity;
        while (true) {
            int i = 0;
            if (a == null) {
                return false;
            }
            while (i < c) {
                Object[] objArr = a[i];
                if (objArr == null) {
                    continue;
                    break;
                } else if (NotificationLite.acceptFull((Object) objArr, observer)) {
                    return true;
                } else {
                    i++;
                }
            }
            a = a[c];
        }
    }

    public <S> void forEachWhile(S state, BiPredicate<? super S, ? super T> consumer) throws Exception {
        Object[] a = this.head;
        int c = this.capacity;
        while (true) {
            int i = 0;
            while (i < c) {
                Object[] objArr = a[i];
                if (objArr != null && !consumer.test(state, objArr)) {
                    i++;
                } else {
                    return;
                }
            }
            a = a[c];
        }
    }
}
