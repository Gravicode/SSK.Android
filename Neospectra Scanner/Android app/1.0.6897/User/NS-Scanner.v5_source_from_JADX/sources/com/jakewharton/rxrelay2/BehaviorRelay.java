package com.jakewharton.rxrelay2;

import com.jakewharton.rxrelay2.AppendOnlyLinkedArrayList.NonThrowingPredicate;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;

public final class BehaviorRelay<T> extends Relay<T> {
    private static final BehaviorDisposable[] EMPTY = new BehaviorDisposable[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    long index;
    final Lock readLock;
    private final AtomicReference<BehaviorDisposable<T>[]> subscribers;
    final AtomicReference<T> value;
    private final Lock writeLock;

    static final class BehaviorDisposable<T> implements Disposable, NonThrowingPredicate<T> {
        final Observer<? super T> actual;
        volatile boolean cancelled;
        boolean emitting;
        boolean fastPath;
        long index;
        boolean next;
        AppendOnlyLinkedArrayList<T> queue;
        final BehaviorRelay<T> state;

        BehaviorDisposable(Observer<? super T> actual2, BehaviorRelay<T> state2) {
            this.actual = actual2;
            this.state = state2;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
            if (r2 == null) goto L_0x0039;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0033, code lost:
            test(r2);
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0039, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst() {
            /*
                r5 = this;
                boolean r0 = r5.cancelled
                if (r0 == 0) goto L_0x0005
                return
            L_0x0005:
                monitor-enter(r5)
                boolean r0 = r5.cancelled     // Catch:{ all -> 0x003a }
                if (r0 == 0) goto L_0x000c
                monitor-exit(r5)     // Catch:{ all -> 0x003a }
                return
            L_0x000c:
                boolean r0 = r5.next     // Catch:{ all -> 0x003a }
                if (r0 == 0) goto L_0x0012
                monitor-exit(r5)     // Catch:{ all -> 0x003a }
                return
            L_0x0012:
                com.jakewharton.rxrelay2.BehaviorRelay<T> r0 = r5.state     // Catch:{ all -> 0x003a }
                java.util.concurrent.locks.Lock r1 = r0.readLock     // Catch:{ all -> 0x003a }
                r1.lock()     // Catch:{ all -> 0x003a }
                long r2 = r0.index     // Catch:{ all -> 0x003a }
                r5.index = r2     // Catch:{ all -> 0x003a }
                java.util.concurrent.atomic.AtomicReference<T> r2 = r0.value     // Catch:{ all -> 0x003a }
                java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x003a }
                r1.unlock()     // Catch:{ all -> 0x003a }
                r3 = 1
                if (r2 == 0) goto L_0x002b
                r4 = 1
                goto L_0x002c
            L_0x002b:
                r4 = 0
            L_0x002c:
                r5.emitting = r4     // Catch:{ all -> 0x003a }
                r5.next = r3     // Catch:{ all -> 0x003a }
                monitor-exit(r5)     // Catch:{ all -> 0x003a }
                if (r2 == 0) goto L_0x0039
                r5.test(r2)
                r5.emitLoop()
            L_0x0039:
                return
            L_0x003a:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x003a }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jakewharton.rxrelay2.BehaviorRelay.BehaviorDisposable.emitFirst():void");
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0032, code lost:
            r3.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(T r4, long r5) {
            /*
                r3 = this;
                boolean r0 = r3.cancelled
                if (r0 == 0) goto L_0x0005
                return
            L_0x0005:
                boolean r0 = r3.fastPath
                if (r0 != 0) goto L_0x0038
                monitor-enter(r3)
                boolean r0 = r3.cancelled     // Catch:{ all -> 0x0035 }
                if (r0 == 0) goto L_0x0010
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                return
            L_0x0010:
                long r0 = r3.index     // Catch:{ all -> 0x0035 }
                int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
                if (r0 != 0) goto L_0x0018
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                return
            L_0x0018:
                boolean r0 = r3.emitting     // Catch:{ all -> 0x0035 }
                if (r0 == 0) goto L_0x002e
                com.jakewharton.rxrelay2.AppendOnlyLinkedArrayList<T> r0 = r3.queue     // Catch:{ all -> 0x0035 }
                if (r0 != 0) goto L_0x0029
                com.jakewharton.rxrelay2.AppendOnlyLinkedArrayList r1 = new com.jakewharton.rxrelay2.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0035 }
                r2 = 4
                r1.<init>(r2)     // Catch:{ all -> 0x0035 }
                r0 = r1
                r3.queue = r0     // Catch:{ all -> 0x0035 }
            L_0x0029:
                r0.add(r4)     // Catch:{ all -> 0x0035 }
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                return
            L_0x002e:
                r0 = 1
                r3.next = r0     // Catch:{ all -> 0x0035 }
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                r3.fastPath = r0
                goto L_0x0038
            L_0x0035:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                throw r0
            L_0x0038:
                r3.test(r4)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jakewharton.rxrelay2.BehaviorRelay.BehaviorDisposable.emitNext(java.lang.Object, long):void");
        }

        public boolean test(T o) {
            if (!this.cancelled) {
                this.actual.onNext(o);
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0013, code lost:
            r0.forEachWhile(r2);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            /*
                r2 = this;
            L_0x0000:
                boolean r0 = r2.cancelled
                if (r0 == 0) goto L_0x0005
                return
            L_0x0005:
                monitor-enter(r2)
                com.jakewharton.rxrelay2.AppendOnlyLinkedArrayList<T> r0 = r2.queue     // Catch:{ all -> 0x0017 }
                if (r0 != 0) goto L_0x000f
                r1 = 0
                r2.emitting = r1     // Catch:{ all -> 0x0017 }
                monitor-exit(r2)     // Catch:{ all -> 0x0017 }
                return
            L_0x000f:
                r1 = 0
                r2.queue = r1     // Catch:{ all -> 0x0017 }
                monitor-exit(r2)     // Catch:{ all -> 0x0017 }
                r0.forEachWhile(r2)
                goto L_0x0000
            L_0x0017:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0017 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jakewharton.rxrelay2.BehaviorRelay.BehaviorDisposable.emitLoop():void");
        }
    }

    public static <T> BehaviorRelay<T> create() {
        return new BehaviorRelay<>();
    }

    public static <T> BehaviorRelay<T> createDefault(T defaultValue) {
        return new BehaviorRelay<>(defaultValue);
    }

    private BehaviorRelay() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
        this.subscribers = new AtomicReference<>(EMPTY);
        this.value = new AtomicReference<>();
    }

    private BehaviorRelay(T defaultValue) {
        this();
        if (defaultValue == null) {
            throw new NullPointerException("defaultValue == null");
        }
        this.value.lazySet(defaultValue);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        BehaviorDisposable<T> bs = new BehaviorDisposable<>(observer, this);
        observer.onSubscribe(bs);
        add(bs);
        if (bs.cancelled) {
            remove(bs);
        } else {
            bs.emitFirst();
        }
    }

    public void accept(T value2) {
        if (value2 == null) {
            throw new NullPointerException("value == null");
        }
        setCurrent(value2);
        for (BehaviorDisposable<T> bs : (BehaviorDisposable[]) this.subscribers.get()) {
            bs.emitNext(value2, this.index);
        }
    }

    public boolean hasObservers() {
        return ((BehaviorDisposable[]) this.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: 0000 */
    public int subscriberCount() {
        return ((BehaviorDisposable[]) this.subscribers.get()).length;
    }

    public T getValue() {
        return this.value.get();
    }

    public Object[] getValues() {
        T[] b = getValues((Object[]) EMPTY_ARRAY);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;
    }

    public T[] getValues(T[] array) {
        T o = this.value.get();
        if (o == null) {
            if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }
        if (array.length != 0) {
            array[0] = o;
            if (array.length != 1) {
                array[1] = null;
            }
        } else {
            array = (Object[]) Array.newInstance(array.getClass().getComponentType(), 1);
            array[0] = o;
        }
        return array;
    }

    public boolean hasValue() {
        return this.value.get() != null;
    }

    private void add(BehaviorDisposable<T> rs) {
        BehaviorDisposable<T>[] a;
        BehaviorDisposable<T>[] b;
        do {
            a = (BehaviorDisposable[]) this.subscribers.get();
            int len = a.length;
            b = new BehaviorDisposable[(len + 1)];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
        } while (!this.subscribers.compareAndSet(a, b));
    }

    /* access modifiers changed from: 0000 */
    public void remove(BehaviorDisposable<T> rs) {
        BehaviorDisposable<T>[] a;
        BehaviorDisposable<T>[] b;
        do {
            a = (BehaviorDisposable[]) this.subscribers.get();
            if (a != EMPTY) {
                int len = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= len) {
                        break;
                    } else if (a[i] == rs) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (len == 1) {
                        b = EMPTY;
                    } else {
                        BehaviorDisposable<T>[] b2 = new BehaviorDisposable[(len - 1)];
                        System.arraycopy(a, 0, b2, 0, j);
                        System.arraycopy(a, j + 1, b2, j, (len - j) - 1);
                        b = b2;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(a, b));
    }

    private void setCurrent(T current) {
        this.writeLock.lock();
        try {
            this.index++;
            this.value.lazySet(current);
        } finally {
            this.writeLock.unlock();
        }
    }
}
