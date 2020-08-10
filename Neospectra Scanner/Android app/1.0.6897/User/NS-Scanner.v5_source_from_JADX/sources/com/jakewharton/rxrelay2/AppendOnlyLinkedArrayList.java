package com.jakewharton.rxrelay2;

import p005io.reactivex.functions.Predicate;

class AppendOnlyLinkedArrayList<T> {
    private final int capacity;
    private final Object[] head;
    private int offset;
    private Object[] tail = this.head;

    public interface NonThrowingPredicate<T> extends Predicate<T> {
        boolean test(T t);
    }

    AppendOnlyLinkedArrayList(int capacity2) {
        this.capacity = capacity2;
        this.head = new Object[(capacity2 + 1)];
    }

    /* access modifiers changed from: 0000 */
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

    /* access modifiers changed from: 0000 */
    public void forEachWhile(NonThrowingPredicate<? super T> consumer) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = a[c]) {
            for (int i = 0; i < c; i++) {
                Object[] objArr = a[i];
                if (objArr == null || consumer.test(objArr)) {
                    break;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean accept(Relay<? super T> observer) {
        Object[] a = this.head;
        int c = this.capacity;
        while (true) {
            if (a == null) {
                return false;
            }
            for (int i = 0; i < c; i++) {
                Object[] objArr = a[i];
                if (objArr == null) {
                    break;
                }
                observer.accept(objArr);
            }
            a = a[c];
        }
    }
}
