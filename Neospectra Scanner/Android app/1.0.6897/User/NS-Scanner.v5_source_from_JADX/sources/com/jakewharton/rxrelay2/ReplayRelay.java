package com.jakewharton.rxrelay2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;

public final class ReplayRelay<T> extends Relay<T> {
    static final ReplayDisposable[] EMPTY = new ReplayDisposable[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    final ReplayBuffer<T> buffer;
    final AtomicReference<ReplayDisposable<T>[]> observers = new AtomicReference<>(EMPTY);

    static final class Node<T> extends AtomicReference<Node<T>> {
        private static final long serialVersionUID = 6404226426336033100L;
        final T value;

        Node(T value2) {
            this.value = value2;
        }
    }

    interface ReplayBuffer<T> {
        void add(T t);

        T getValue();

        T[] getValues(T[] tArr);

        void replay(ReplayDisposable<T> replayDisposable);

        int size();
    }

    static final class ReplayDisposable<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 466549804534799122L;
        final Observer<? super T> actual;
        volatile boolean cancelled;
        Object index;
        final ReplayRelay<T> state;

        ReplayDisposable(Observer<? super T> actual2, ReplayRelay<T> state2) {
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
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = -8056260896137901749L;
        volatile TimedNode<T> head;
        final long maxAge;
        final int maxSize;
        final Scheduler scheduler;
        int size;
        TimedNode<T> tail;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int maxSize2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            if (maxSize2 <= 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("maxSize > 0 required but it was ");
                sb.append(maxSize2);
                throw new IllegalArgumentException(sb.toString());
            } else if (maxAge2 <= 0) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("maxAge > 0 required but it was ");
                sb2.append(maxAge2);
                throw new IllegalArgumentException(sb2.toString());
            } else if (unit2 == null) {
                throw new NullPointerException("unit == null");
            } else if (scheduler2 == null) {
                throw new NullPointerException("scheduler == null");
            } else {
                this.maxSize = maxSize2;
                this.maxAge = maxAge2;
                this.unit = unit2;
                this.scheduler = scheduler2;
                TimedNode timedNode = new TimedNode(null, 0);
                this.tail = timedNode;
                this.head = timedNode;
            }
        }

        /* access modifiers changed from: 0000 */
        public void trim() {
            if (this.size > this.maxSize) {
                this.size--;
                this.head = (TimedNode) this.head.get();
            }
            long limit = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode<T> h = this.head;
            while (true) {
                TimedNode<T> next = (TimedNode) h.get();
                if (next == null) {
                    this.head = h;
                    return;
                } else if (next.time > limit) {
                    this.head = h;
                    return;
                } else {
                    h = next;
                }
            }
        }

        public void add(T value) {
            TimedNode<T> n = new TimedNode<>(value, this.scheduler.now(this.unit));
            TimedNode<T> t = this.tail;
            this.tail = n;
            this.size++;
            t.set(n);
            trim();
        }

        public T getValue() {
            TimedNode<T> h = this.head;
            while (true) {
                TimedNode<T> next = (TimedNode) h.get();
                if (next == null) {
                    return h.value;
                }
                h = next;
            }
        }

        public T[] getValues(T[] array) {
            TimedNode<T> h = this.head;
            int s = size();
            int i = 0;
            if (s != 0) {
                if (array.length < s) {
                    array = (Object[]) Array.newInstance(array.getClass().getComponentType(), s);
                }
                while (i != s) {
                    TimedNode<T> next = (TimedNode) h.get();
                    array[i] = next.value;
                    i++;
                    h = next;
                }
                if (array.length > s) {
                    array[s] = null;
                }
            } else if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }

        public void replay(ReplayDisposable<T> rs) {
            if (rs.getAndIncrement() == 0) {
                int missed = 1;
                Observer<? super T> a = rs.actual;
                TimedNode<T> index = (TimedNode) rs.index;
                if (index == null) {
                    index = this.head;
                    long limit = this.scheduler.now(this.unit) - this.maxAge;
                    TimedNode<T> next = (TimedNode) index.get();
                    while (next != null && next.time <= limit) {
                        index = next;
                        next = (TimedNode) index.get();
                    }
                }
                while (!rs.cancelled) {
                    while (!rs.cancelled) {
                        TimedNode<T> n = (TimedNode) index.get();
                        if (n != null) {
                            a.onNext(n.value);
                            index = n;
                        } else if (index.get() == null) {
                            rs.index = index;
                            missed = rs.addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        }
                    }
                    rs.index = null;
                    return;
                }
                rs.index = null;
            }
        }

        public int size() {
            int s = 0;
            TimedNode<T> h = this.head;
            while (s != Integer.MAX_VALUE) {
                TimedNode<T> next = (TimedNode) h.get();
                if (next == null) {
                    break;
                }
                s++;
                h = next;
            }
            return s;
        }
    }

    static final class SizeBoundReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 1107649250281456395L;
        volatile Node<T> head;
        final int maxSize;
        int size;
        Node<T> tail;

        SizeBoundReplayBuffer(int maxSize2) {
            if (maxSize2 <= 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("maxSize > 0 required but it was ");
                sb.append(maxSize2);
                throw new IllegalArgumentException(sb.toString());
            }
            this.maxSize = maxSize2;
            Node<T> h = new Node<>(null);
            this.tail = h;
            this.head = h;
        }

        /* access modifiers changed from: 0000 */
        public void trim() {
            if (this.size > this.maxSize) {
                this.size--;
                this.head = (Node) this.head.get();
            }
        }

        public void add(T value) {
            Node<T> n = new Node<>(value);
            Node<T> t = this.tail;
            this.tail = n;
            this.size++;
            t.set(n);
            trim();
        }

        public T getValue() {
            Node<T> h = this.head;
            while (true) {
                Node<T> next = (Node) h.get();
                if (next == null) {
                    return h.value;
                }
                h = next;
            }
        }

        public T[] getValues(T[] array) {
            Node<T> h = this.head;
            int s = size();
            int i = 0;
            if (s != 0) {
                if (array.length < s) {
                    array = (Object[]) Array.newInstance(array.getClass().getComponentType(), s);
                }
                while (i != s) {
                    Node<T> next = (Node) h.get();
                    array[i] = next.value;
                    i++;
                    h = next;
                }
                if (array.length > s) {
                    array[s] = null;
                }
            } else if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }

        public void replay(ReplayDisposable<T> rs) {
            if (rs.getAndIncrement() == 0) {
                int missed = 1;
                Observer<? super T> a = rs.actual;
                Node<T> index = (Node) rs.index;
                if (index == null) {
                    index = this.head;
                }
                while (!rs.cancelled) {
                    Node<T> n = (Node) index.get();
                    if (n != null) {
                        a.onNext(n.value);
                        index = n;
                    } else if (index.get() != null) {
                        continue;
                    } else {
                        rs.index = index;
                        missed = rs.addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    }
                }
                rs.index = null;
            }
        }

        public int size() {
            int s = 0;
            Node<T> h = this.head;
            while (s != Integer.MAX_VALUE) {
                Node<T> next = (Node) h.get();
                if (next == null) {
                    break;
                }
                s++;
                h = next;
            }
            return s;
        }
    }

    static final class TimedNode<T> extends AtomicReference<TimedNode<T>> {
        private static final long serialVersionUID = 6404226426336033100L;
        final long time;
        final T value;

        TimedNode(T value2, long time2) {
            this.value = value2;
            this.time = time2;
        }
    }

    static final class UnboundedReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = -733876083048047795L;
        final List<T> buffer;
        volatile int size;

        UnboundedReplayBuffer(int capacityHint) {
            if (capacityHint <= 0) {
                throw new IllegalArgumentException("capacityHint <= 0");
            }
            this.buffer = new ArrayList(capacityHint);
        }

        public void add(T value) {
            this.buffer.add(value);
            this.size++;
        }

        public T getValue() {
            int s = this.size;
            if (s != 0) {
                return this.buffer.get(s - 1);
            }
            return null;
        }

        public T[] getValues(T[] array) {
            int s = this.size;
            if (s == 0) {
                if (array.length != 0) {
                    array[0] = null;
                }
                return array;
            }
            if (array.length < s) {
                array = (Object[]) Array.newInstance(array.getClass().getComponentType(), s);
            }
            List<T> b = this.buffer;
            for (int i = 0; i < s; i++) {
                array[i] = b.get(i);
            }
            if (array.length > s) {
                array[s] = null;
            }
            return array;
        }

        public void replay(ReplayDisposable<T> rs) {
            int index;
            if (rs.getAndIncrement() == 0) {
                int missed = 1;
                List<T> b = this.buffer;
                Observer<? super T> a = rs.actual;
                Integer indexObject = (Integer) rs.index;
                if (indexObject != null) {
                    index = indexObject.intValue();
                } else {
                    index = 0;
                    rs.index = Integer.valueOf(0);
                }
                while (!rs.cancelled) {
                    int s = this.size;
                    while (s != index) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        } else {
                            a.onNext(b.get(index));
                            index++;
                        }
                    }
                    if (index == this.size) {
                        rs.index = Integer.valueOf(index);
                        missed = rs.addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    }
                }
                rs.index = null;
            }
        }

        public int size() {
            int s = this.size;
            if (s != 0) {
                return s;
            }
            return 0;
        }
    }

    public static <T> ReplayRelay<T> create() {
        return new ReplayRelay<>(new UnboundedReplayBuffer(16));
    }

    public static <T> ReplayRelay<T> create(int capacityHint) {
        return new ReplayRelay<>(new UnboundedReplayBuffer(capacityHint));
    }

    public static <T> ReplayRelay<T> createWithSize(int maxSize) {
        return new ReplayRelay<>(new SizeBoundReplayBuffer(maxSize));
    }

    static <T> ReplayRelay<T> createUnbounded() {
        return new ReplayRelay<>(new SizeBoundReplayBuffer(Integer.MAX_VALUE));
    }

    public static <T> ReplayRelay<T> createWithTime(long maxAge, TimeUnit unit, Scheduler scheduler) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(Integer.MAX_VALUE, maxAge, unit, scheduler);
        return new ReplayRelay<>(sizeAndTimeBoundReplayBuffer);
    }

    public static <T> ReplayRelay<T> createWithTimeAndSize(long maxAge, TimeUnit unit, Scheduler scheduler, int maxSize) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(maxSize, maxAge, unit, scheduler);
        return new ReplayRelay<>(sizeAndTimeBoundReplayBuffer);
    }

    ReplayRelay(ReplayBuffer<T> buffer2) {
        this.buffer = buffer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        ReplayDisposable<T> rs = new ReplayDisposable<>(observer, this);
        observer.onSubscribe(rs);
        if (!rs.cancelled) {
            if (!add(rs) || !rs.cancelled) {
                this.buffer.replay(rs);
            } else {
                remove(rs);
            }
        }
    }

    public void accept(T value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        ReplayBuffer<T> b = this.buffer;
        b.add(value);
        for (ReplayDisposable<T> rs : (ReplayDisposable[]) this.observers.get()) {
            b.replay(rs);
        }
    }

    public boolean hasObservers() {
        return ((ReplayDisposable[]) this.observers.get()).length != 0;
    }

    /* access modifiers changed from: 0000 */
    public int observerCount() {
        return ((ReplayDisposable[]) this.observers.get()).length;
    }

    public T getValue() {
        return this.buffer.getValue();
    }

    public Object[] getValues() {
        T[] b = getValues((Object[]) EMPTY_ARRAY);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;
    }

    public T[] getValues(T[] array) {
        return this.buffer.getValues(array);
    }

    public boolean hasValue() {
        return this.buffer.size() != 0;
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        return this.buffer.size();
    }

    /* access modifiers changed from: 0000 */
    public boolean add(ReplayDisposable<T> rs) {
        ReplayDisposable<T>[] a;
        ReplayDisposable<T>[] b;
        do {
            a = (ReplayDisposable[]) this.observers.get();
            int len = a.length;
            b = new ReplayDisposable[(len + 1)];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
        } while (!this.observers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(ReplayDisposable<T> rs) {
        ReplayDisposable<T>[] a;
        ReplayDisposable<T>[] b;
        do {
            a = (ReplayDisposable[]) this.observers.get();
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
                        ReplayDisposable<T>[] b2 = new ReplayDisposable[(len - 1)];
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
        } while (!this.observers.compareAndSet(a, b));
    }
}
