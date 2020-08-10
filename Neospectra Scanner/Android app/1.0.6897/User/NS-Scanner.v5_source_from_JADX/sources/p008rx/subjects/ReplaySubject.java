package p008rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Scheduler;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.internal.operators.BackpressureUtils;
import p008rx.plugins.RxJavaHooks;
import p008rx.schedulers.Schedulers;

/* renamed from: rx.subjects.ReplaySubject */
public final class ReplaySubject<T> extends Subject<T, T> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    final ReplayState<T> state;

    /* renamed from: rx.subjects.ReplaySubject$ReplayBuffer */
    interface ReplayBuffer<T> {
        void complete();

        void drain(ReplayProducer<T> replayProducer);

        Throwable error();

        void error(Throwable th);

        boolean isComplete();

        boolean isEmpty();

        T last();

        void next(T t);

        int size();

        T[] toArray(T[] tArr);
    }

    /* renamed from: rx.subjects.ReplaySubject$ReplayProducer */
    static final class ReplayProducer<T> extends AtomicInteger implements Producer, Subscription {
        private static final long serialVersionUID = -5006209596735204567L;
        final Subscriber<? super T> actual;
        int index;
        Object node;
        final AtomicLong requested = new AtomicLong();
        final ReplayState<T> state;
        int tailIndex;

        public ReplayProducer(Subscriber<? super T> actual2, ReplayState<T> state2) {
            this.actual = actual2;
            this.state = state2;
        }

        public void unsubscribe() {
            this.state.remove(this);
        }

        public boolean isUnsubscribed() {
            return this.actual.isUnsubscribed();
        }

        public void request(long n) {
            if (n > 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                this.state.buffer.drain(this);
            } else if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    /* renamed from: rx.subjects.ReplaySubject$ReplaySizeAndTimeBoundBuffer */
    static final class ReplaySizeAndTimeBoundBuffer<T> implements ReplayBuffer<T> {
        volatile boolean done;
        Throwable error;
        volatile TimedNode<T> head;
        final int limit;
        final long maxAgeMillis;
        final Scheduler scheduler;
        int size;
        TimedNode<T> tail;

        /* renamed from: rx.subjects.ReplaySubject$ReplaySizeAndTimeBoundBuffer$TimedNode */
        static final class TimedNode<T> extends AtomicReference<TimedNode<T>> {
            private static final long serialVersionUID = 3713592843205853725L;
            final long timestamp;
            final T value;

            public TimedNode(T value2, long timestamp2) {
                this.value = value2;
                this.timestamp = timestamp2;
            }
        }

        public ReplaySizeAndTimeBoundBuffer(int limit2, long maxAgeMillis2, Scheduler scheduler2) {
            this.limit = limit2;
            TimedNode<T> n = new TimedNode<>(null, 0);
            this.tail = n;
            this.head = n;
            this.maxAgeMillis = maxAgeMillis2;
            this.scheduler = scheduler2;
        }

        public void next(T value) {
            long now = this.scheduler.now();
            TimedNode<T> n = new TimedNode<>(value, now);
            this.tail.set(n);
            this.tail = n;
            long now2 = now - this.maxAgeMillis;
            int s = this.size;
            TimedNode<T> h0 = this.head;
            TimedNode<T> h = h0;
            if (s == this.limit) {
                h = (TimedNode) h.get();
            } else {
                s++;
            }
            while (true) {
                TimedNode<T> timedNode = (TimedNode) h.get();
                TimedNode<T> n2 = timedNode;
                if (timedNode == null || n2.timestamp > now2) {
                    this.size = s;
                } else {
                    h = n2;
                    s--;
                }
            }
            this.size = s;
            if (h != h0) {
                this.head = h;
            }
        }

        public void error(Throwable ex) {
            evictFinal();
            this.error = ex;
            this.done = true;
        }

        public void complete() {
            evictFinal();
            this.done = true;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:7:0x0020  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void evictFinal() {
            /*
                r8 = this;
                rx.Scheduler r0 = r8.scheduler
                long r0 = r0.now()
                long r2 = r8.maxAgeMillis
                long r0 = r0 - r2
                rx.subjects.ReplaySubject$ReplaySizeAndTimeBoundBuffer$TimedNode<T> r2 = r8.head
                r3 = r2
            L_0x000c:
                java.lang.Object r4 = r3.get()
                rx.subjects.ReplaySubject$ReplaySizeAndTimeBoundBuffer$TimedNode r4 = (p008rx.subjects.ReplaySubject.ReplaySizeAndTimeBoundBuffer.TimedNode) r4
                r5 = r4
                if (r4 == 0) goto L_0x001e
                long r6 = r5.timestamp
                int r4 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r4 <= 0) goto L_0x001c
                goto L_0x001e
            L_0x001c:
                r3 = r5
                goto L_0x000c
            L_0x001e:
                if (r2 == r3) goto L_0x0022
                r8.head = r3
            L_0x0022:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.ReplaySubject.ReplaySizeAndTimeBoundBuffer.evictFinal():void");
        }

        /* access modifiers changed from: 0000 */
        public TimedNode<T> latestHead() {
            long now = this.scheduler.now() - this.maxAgeMillis;
            TimedNode<T> h = this.head;
            while (true) {
                TimedNode<T> timedNode = (TimedNode) h.get();
                TimedNode<T> n = timedNode;
                if (timedNode == null || n.timestamp > now) {
                    return h;
                }
                h = n;
            }
            return h;
        }

        public void drain(ReplayProducer<T> rp) {
            boolean empty;
            if (rp.getAndIncrement() == 0) {
                Subscriber<? super T> a = rp.actual;
                int missed = 1;
                do {
                    long r = rp.requested.get();
                    long e = 0;
                    TimedNode<T> node = (TimedNode) rp.node;
                    if (node == null) {
                        node = latestHead();
                    }
                    while (true) {
                        empty = false;
                        if (e == r) {
                            break;
                        } else if (a.isUnsubscribed()) {
                            rp.node = null;
                            return;
                        } else {
                            boolean d = this.done;
                            TimedNode<T> next = (TimedNode) node.get();
                            boolean empty2 = next == null;
                            if (d && empty2) {
                                rp.node = null;
                                Throwable ex = this.error;
                                if (ex != null) {
                                    a.onError(ex);
                                } else {
                                    a.onCompleted();
                                }
                                return;
                            } else if (empty2) {
                                break;
                            } else {
                                a.onNext(next.value);
                                e++;
                                node = next;
                            }
                        }
                    }
                    if (e == r) {
                        if (a.isUnsubscribed()) {
                            rp.node = null;
                            return;
                        }
                        boolean d2 = this.done;
                        if (node.get() == null) {
                            empty = true;
                        }
                        if (d2 && empty) {
                            rp.node = null;
                            Throwable ex2 = this.error;
                            if (ex2 != null) {
                                a.onError(ex2);
                            } else {
                                a.onCompleted();
                            }
                            return;
                        }
                    }
                    if (!(e == 0 || r == Long.MAX_VALUE)) {
                        BackpressureUtils.produced(rp.requested, e);
                    }
                    rp.node = node;
                    missed = rp.addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public boolean isComplete() {
            return this.done;
        }

        public Throwable error() {
            return this.error;
        }

        public T last() {
            TimedNode latestHead = latestHead();
            while (true) {
                TimedNode timedNode = (TimedNode) latestHead.get();
                TimedNode timedNode2 = timedNode;
                if (timedNode == null) {
                    return latestHead.value;
                }
                latestHead = timedNode2;
            }
        }

        public int size() {
            int s = 0;
            TimedNode<T> n = (TimedNode) latestHead().get();
            while (n != null && s != Integer.MAX_VALUE) {
                n = (TimedNode) n.get();
                s++;
            }
            return s;
        }

        public boolean isEmpty() {
            return latestHead().get() == null;
        }

        public T[] toArray(T[] a) {
            List<T> list = new ArrayList<>();
            for (TimedNode<T> n = (TimedNode) latestHead().get(); n != null; n = (TimedNode) n.get()) {
                list.add(n.value);
            }
            return list.toArray(a);
        }
    }

    /* renamed from: rx.subjects.ReplaySubject$ReplaySizeBoundBuffer */
    static final class ReplaySizeBoundBuffer<T> implements ReplayBuffer<T> {
        volatile boolean done;
        Throwable error;
        volatile Node<T> head;
        final int limit;
        int size;
        Node<T> tail;

        /* renamed from: rx.subjects.ReplaySubject$ReplaySizeBoundBuffer$Node */
        static final class Node<T> extends AtomicReference<Node<T>> {
            private static final long serialVersionUID = 3713592843205853725L;
            final T value;

            public Node(T value2) {
                this.value = value2;
            }
        }

        public ReplaySizeBoundBuffer(int limit2) {
            this.limit = limit2;
            Node<T> n = new Node<>(null);
            this.tail = n;
            this.head = n;
        }

        public void next(T value) {
            Node<T> n = new Node<>(value);
            this.tail.set(n);
            this.tail = n;
            int s = this.size;
            if (s == this.limit) {
                this.head = (Node) this.head.get();
            } else {
                this.size = s + 1;
            }
        }

        public void error(Throwable ex) {
            this.error = ex;
            this.done = true;
        }

        public void complete() {
            this.done = true;
        }

        public void drain(ReplayProducer<T> rp) {
            boolean empty;
            if (rp.getAndIncrement() == 0) {
                Subscriber<? super T> a = rp.actual;
                int missed = 1;
                do {
                    long r = rp.requested.get();
                    long e = 0;
                    Node<T> node = (Node) rp.node;
                    if (node == null) {
                        node = this.head;
                    }
                    while (true) {
                        empty = false;
                        if (e == r) {
                            break;
                        } else if (a.isUnsubscribed()) {
                            rp.node = null;
                            return;
                        } else {
                            boolean d = this.done;
                            Node<T> next = (Node) node.get();
                            boolean empty2 = next == null;
                            if (d && empty2) {
                                rp.node = null;
                                Throwable ex = this.error;
                                if (ex != null) {
                                    a.onError(ex);
                                } else {
                                    a.onCompleted();
                                }
                                return;
                            } else if (empty2) {
                                break;
                            } else {
                                a.onNext(next.value);
                                e++;
                                node = next;
                            }
                        }
                    }
                    if (e == r) {
                        if (a.isUnsubscribed()) {
                            rp.node = null;
                            return;
                        }
                        boolean d2 = this.done;
                        if (node.get() == null) {
                            empty = true;
                        }
                        if (d2 && empty) {
                            rp.node = null;
                            Throwable ex2 = this.error;
                            if (ex2 != null) {
                                a.onError(ex2);
                            } else {
                                a.onCompleted();
                            }
                            return;
                        }
                    }
                    if (!(e == 0 || r == Long.MAX_VALUE)) {
                        BackpressureUtils.produced(rp.requested, e);
                    }
                    rp.node = node;
                    missed = rp.addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public boolean isComplete() {
            return this.done;
        }

        public Throwable error() {
            return this.error;
        }

        public T last() {
            Node<T> h = this.head;
            while (true) {
                Node<T> node = (Node) h.get();
                Node<T> n = node;
                if (node == null) {
                    return h.value;
                }
                h = n;
            }
        }

        public int size() {
            int s = 0;
            Node<T> n = (Node) this.head.get();
            while (n != null && s != Integer.MAX_VALUE) {
                n = (Node) n.get();
                s++;
            }
            return s;
        }

        public boolean isEmpty() {
            return this.head.get() == null;
        }

        public T[] toArray(T[] a) {
            List<T> list = new ArrayList<>();
            for (Node<T> n = (Node) this.head.get(); n != null; n = (Node) n.get()) {
                list.add(n.value);
            }
            return list.toArray(a);
        }
    }

    /* renamed from: rx.subjects.ReplaySubject$ReplayState */
    static final class ReplayState<T> extends AtomicReference<ReplayProducer<T>[]> implements OnSubscribe<T>, Observer<T> {
        static final ReplayProducer[] EMPTY = new ReplayProducer[0];
        static final ReplayProducer[] TERMINATED = new ReplayProducer[0];
        private static final long serialVersionUID = 5952362471246910544L;
        final ReplayBuffer<T> buffer;

        public ReplayState(ReplayBuffer<T> buffer2) {
            this.buffer = buffer2;
            lazySet(EMPTY);
        }

        public void call(Subscriber<? super T> t) {
            ReplayProducer<T> rp = new ReplayProducer<>(t, this);
            t.add(rp);
            t.setProducer(rp);
            if (!add(rp) || !rp.isUnsubscribed()) {
                this.buffer.drain(rp);
            } else {
                remove(rp);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(ReplayProducer<T> rp) {
            ReplayProducer<T>[] a;
            ReplayProducer<T>[] b;
            do {
                a = (ReplayProducer[]) get();
                if (a == TERMINATED) {
                    return false;
                }
                int n = a.length;
                b = new ReplayProducer[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = rp;
            } while (!compareAndSet(a, b));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(ReplayProducer<T> rp) {
            ReplayProducer<T>[] a;
            ReplayProducer<T>[] b;
            do {
                a = (ReplayProducer[]) get();
                if (a != TERMINATED && a != EMPTY) {
                    int n = a.length;
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (a[i] == rp) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        if (n == 1) {
                            b = EMPTY;
                        } else {
                            ReplayProducer<T>[] b2 = new ReplayProducer[(n - 1)];
                            System.arraycopy(a, 0, b2, 0, j);
                            System.arraycopy(a, j + 1, b2, j, (n - j) - 1);
                            b = b2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(a, b));
        }

        public void onNext(T t) {
            ReplayBuffer<T> b = this.buffer;
            b.next(t);
            for (ReplayProducer<T> rp : (ReplayProducer[]) get()) {
                b.drain(rp);
            }
        }

        public void onError(Throwable e) {
            ReplayBuffer<T> b = this.buffer;
            b.error(e);
            List<Throwable> errors = null;
            for (ReplayProducer<T> rp : (ReplayProducer[]) getAndSet(TERMINATED)) {
                try {
                    b.drain(rp);
                } catch (Throwable ex) {
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(ex);
                }
            }
            Exceptions.throwIfAny(errors);
        }

        public void onCompleted() {
            ReplayBuffer<T> b = this.buffer;
            b.complete();
            for (ReplayProducer<T> rp : (ReplayProducer[]) getAndSet(TERMINATED)) {
                b.drain(rp);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean isTerminated() {
            return get() == TERMINATED;
        }
    }

    /* renamed from: rx.subjects.ReplaySubject$ReplayUnboundedBuffer */
    static final class ReplayUnboundedBuffer<T> implements ReplayBuffer<T> {
        final int capacity;
        volatile boolean done;
        Throwable error;
        final Object[] head;
        volatile int size;
        Object[] tail;
        int tailIndex;

        public ReplayUnboundedBuffer(int capacity2) {
            this.capacity = capacity2;
            Object[] objArr = new Object[(capacity2 + 1)];
            this.head = objArr;
            this.tail = objArr;
        }

        public void next(T t) {
            if (!this.done) {
                int i = this.tailIndex;
                Object[] a = this.tail;
                if (i == a.length - 1) {
                    Object[] b = new Object[a.length];
                    b[0] = t;
                    this.tailIndex = 1;
                    a[i] = b;
                    this.tail = b;
                } else {
                    a[i] = t;
                    this.tailIndex = i + 1;
                }
                this.size++;
            }
        }

        public void error(Throwable e) {
            if (this.done) {
                RxJavaHooks.onError(e);
                return;
            }
            this.error = e;
            this.done = true;
        }

        public void complete() {
            this.done = true;
        }

        public void drain(ReplayProducer<T> rp) {
            boolean z;
            ReplayProducer<T> replayProducer = rp;
            if (rp.getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = replayProducer.actual;
                int n = this.capacity;
                do {
                    long r = replayProducer.requested.get();
                    long e = 0;
                    Object[] node = (Object[]) replayProducer.node;
                    if (node == null) {
                        node = this.head;
                    }
                    int tailIndex2 = replayProducer.tailIndex;
                    int index = replayProducer.index;
                    while (true) {
                        z = true;
                        if (e == r) {
                            break;
                        } else if (a.isUnsubscribed()) {
                            replayProducer.node = null;
                            return;
                        } else {
                            boolean d = this.done;
                            boolean empty = index == this.size;
                            if (d && empty) {
                                replayProducer.node = null;
                                Throwable ex = this.error;
                                if (ex != null) {
                                    a.onError(ex);
                                } else {
                                    a.onCompleted();
                                }
                                return;
                            } else if (empty) {
                                break;
                            } else {
                                if (tailIndex2 == n) {
                                    node = (Object[]) node[tailIndex2];
                                    tailIndex2 = 0;
                                }
                                a.onNext(node[tailIndex2]);
                                e++;
                                tailIndex2++;
                                index++;
                            }
                        }
                    }
                    if (e == r) {
                        if (a.isUnsubscribed()) {
                            replayProducer.node = null;
                            return;
                        }
                        boolean d2 = this.done;
                        if (index != this.size) {
                            z = false;
                        }
                        boolean empty2 = z;
                        if (d2 && empty2) {
                            replayProducer.node = null;
                            Throwable ex2 = this.error;
                            if (ex2 != null) {
                                a.onError(ex2);
                            } else {
                                a.onCompleted();
                            }
                            return;
                        }
                    }
                    if (!(e == 0 || r == Long.MAX_VALUE)) {
                        BackpressureUtils.produced(replayProducer.requested, e);
                    }
                    replayProducer.index = index;
                    replayProducer.tailIndex = tailIndex2;
                    replayProducer.node = node;
                    missed = replayProducer.addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public boolean isComplete() {
            return this.done;
        }

        public Throwable error() {
            return this.error;
        }

        public T last() {
            int s = this.size;
            if (s == 0) {
                return null;
            }
            Object[] h = this.head;
            int n = this.capacity;
            while (s >= n) {
                h = (Object[]) h[n];
                s -= n;
            }
            return h[s - 1];
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public T[] toArray(T[] a) {
            int s = this.size;
            if (a.length < s) {
                a = (Object[]) Array.newInstance(a.getClass().getComponentType(), s);
            }
            Object[] h = this.head;
            int n = this.capacity;
            Object[] h2 = h;
            int j = 0;
            while (j + n < s) {
                System.arraycopy(h2, 0, a, j, n);
                j += n;
                h2 = h2[n];
            }
            System.arraycopy(h2, 0, a, j, s - j);
            if (a.length > s) {
                a[s] = null;
            }
            return a;
        }
    }

    public static <T> ReplaySubject<T> create() {
        return create(16);
    }

    public static <T> ReplaySubject<T> create(int capacity) {
        if (capacity > 0) {
            return new ReplaySubject<>(new ReplayState<>(new ReplayUnboundedBuffer<>(capacity)));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("capacity > 0 required but it was ");
        sb.append(capacity);
        throw new IllegalArgumentException(sb.toString());
    }

    static <T> ReplaySubject<T> createUnbounded() {
        return new ReplaySubject<>(new ReplayState<>(new ReplaySizeBoundBuffer<>(Integer.MAX_VALUE)));
    }

    static <T> ReplaySubject<T> createUnboundedTime() {
        return new ReplaySubject<>(new ReplayState<>(new ReplaySizeAndTimeBoundBuffer<>(Integer.MAX_VALUE, Long.MAX_VALUE, Schedulers.immediate())));
    }

    public static <T> ReplaySubject<T> createWithSize(int size) {
        return new ReplaySubject<>(new ReplayState<>(new ReplaySizeBoundBuffer<>(size)));
    }

    public static <T> ReplaySubject<T> createWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        return createWithTimeAndSize(time, unit, Integer.MAX_VALUE, scheduler);
    }

    public static <T> ReplaySubject<T> createWithTimeAndSize(long time, TimeUnit unit, int size, Scheduler scheduler) {
        return new ReplaySubject<>(new ReplayState<>(new ReplaySizeAndTimeBoundBuffer<>(size, unit.toMillis(time), scheduler)));
    }

    ReplaySubject(ReplayState<T> state2) {
        super(state2);
        this.state = state2;
    }

    public void onNext(T t) {
        this.state.onNext(t);
    }

    public void onError(Throwable e) {
        this.state.onError(e);
    }

    public void onCompleted() {
        this.state.onCompleted();
    }

    /* access modifiers changed from: 0000 */
    public int subscriberCount() {
        return ((ReplayProducer[]) this.state.get()).length;
    }

    public boolean hasObservers() {
        return ((ReplayProducer[]) this.state.get()).length != 0;
    }

    public boolean hasThrowable() {
        return this.state.isTerminated() && this.state.buffer.error() != null;
    }

    public boolean hasCompleted() {
        return this.state.isTerminated() && this.state.buffer.error() == null;
    }

    public Throwable getThrowable() {
        if (this.state.isTerminated()) {
            return this.state.buffer.error();
        }
        return null;
    }

    public int size() {
        return this.state.buffer.size();
    }

    public boolean hasAnyValue() {
        return !this.state.buffer.isEmpty();
    }

    public boolean hasValue() {
        return hasAnyValue();
    }

    public T[] getValues(T[] a) {
        return this.state.buffer.toArray(a);
    }

    public Object[] getValues() {
        T[] r = getValues(EMPTY_ARRAY);
        if (r == EMPTY_ARRAY) {
            return new Object[0];
        }
        return r;
    }

    public T getValue() {
        return this.state.buffer.last();
    }
}
