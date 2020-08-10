package p008rx.subjects;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Actions;
import p008rx.internal.operators.NotificationLite;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.subjects.SubjectSubscriptionManager */
final class SubjectSubscriptionManager<T> extends AtomicReference<State<T>> implements OnSubscribe<T> {
    private static final long serialVersionUID = 6035251036011671568L;
    boolean active = true;
    volatile Object latest;
    Action1<SubjectObserver<T>> onAdded = Actions.empty();
    Action1<SubjectObserver<T>> onStart = Actions.empty();
    Action1<SubjectObserver<T>> onTerminated = Actions.empty();

    /* renamed from: rx.subjects.SubjectSubscriptionManager$State */
    protected static final class State<T> {
        static final State EMPTY = new State(false, NO_OBSERVERS);
        static final SubjectObserver[] NO_OBSERVERS = new SubjectObserver[0];
        static final State TERMINATED = new State(true, NO_OBSERVERS);
        final SubjectObserver[] observers;
        final boolean terminated;

        public State(boolean terminated2, SubjectObserver[] observers2) {
            this.terminated = terminated2;
            this.observers = observers2;
        }

        public State add(SubjectObserver o) {
            int n = this.observers.length;
            SubjectObserver[] b = new SubjectObserver[(n + 1)];
            System.arraycopy(this.observers, 0, b, 0, n);
            b[n] = o;
            return new State(this.terminated, b);
        }

        public State remove(SubjectObserver o) {
            SubjectObserver[] a = this.observers;
            if (n == 1 && a[0] == o) {
                return EMPTY;
            }
            if (n == 0) {
                return this;
            }
            SubjectObserver[] b = new SubjectObserver[(n - 1)];
            int j = 0;
            for (SubjectObserver ai : a) {
                if (ai != o) {
                    if (j == n - 1) {
                        return this;
                    }
                    int j2 = j + 1;
                    b[j] = ai;
                    j = j2;
                }
            }
            if (j == 0) {
                return EMPTY;
            }
            if (j < n - 1) {
                SubjectObserver[] c = new SubjectObserver[j];
                System.arraycopy(b, 0, c, 0, j);
                b = c;
            }
            return new State(this.terminated, b);
        }
    }

    /* renamed from: rx.subjects.SubjectSubscriptionManager$SubjectObserver */
    protected static final class SubjectObserver<T> implements Observer<T> {
        final Subscriber<? super T> actual;
        volatile boolean caughtUp;
        boolean emitting;
        boolean fastPath;
        boolean first = true;
        private volatile Object index;
        List<Object> queue;

        public SubjectObserver(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001f, code lost:
            r1.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(java.lang.Object r2) {
            /*
                r1 = this;
                boolean r0 = r1.fastPath
                if (r0 != 0) goto L_0x0026
                monitor-enter(r1)
                r0 = 0
                r1.first = r0     // Catch:{ all -> 0x0023 }
                boolean r0 = r1.emitting     // Catch:{ all -> 0x0023 }
                if (r0 == 0) goto L_0x001e
                java.util.List<java.lang.Object> r0 = r1.queue     // Catch:{ all -> 0x0023 }
                if (r0 != 0) goto L_0x0017
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x0023 }
                r0.<init>()     // Catch:{ all -> 0x0023 }
                r1.queue = r0     // Catch:{ all -> 0x0023 }
            L_0x0017:
                java.util.List<java.lang.Object> r0 = r1.queue     // Catch:{ all -> 0x0023 }
                r0.add(r2)     // Catch:{ all -> 0x0023 }
                monitor-exit(r1)     // Catch:{ all -> 0x0023 }
                return
            L_0x001e:
                monitor-exit(r1)     // Catch:{ all -> 0x0023 }
                r0 = 1
                r1.fastPath = r0
                goto L_0x0026
            L_0x0023:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0023 }
                throw r0
            L_0x0026:
                rx.Subscriber<? super T> r0 = r1.actual
                p008rx.internal.operators.NotificationLite.accept(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitNext(java.lang.Object):void");
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
            if (r2 == null) goto L_0x001a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0016, code lost:
            emitLoop(null, r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst(java.lang.Object r2) {
            /*
                r1 = this;
                monitor-enter(r1)
                boolean r0 = r1.first     // Catch:{ all -> 0x001d }
                if (r0 == 0) goto L_0x001b
                boolean r0 = r1.emitting     // Catch:{ all -> 0x001d }
                if (r0 == 0) goto L_0x000a
                goto L_0x001b
            L_0x000a:
                r0 = 0
                r1.first = r0     // Catch:{ all -> 0x001d }
                if (r2 == 0) goto L_0x0011
                r0 = 1
            L_0x0011:
                r1.emitting = r0     // Catch:{ all -> 0x001d }
                monitor-exit(r1)     // Catch:{ all -> 0x001d }
                if (r2 == 0) goto L_0x001a
                r0 = 0
                r1.emitLoop(r0, r2)
            L_0x001a:
                return
            L_0x001b:
                monitor-exit(r1)     // Catch:{ all -> 0x001d }
                return
            L_0x001d:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x001d }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitFirst(java.lang.Object):void");
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
            if (1 != 0) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop(java.util.List<java.lang.Object> r6, java.lang.Object r7) {
            /*
                r5 = this;
                r0 = 1
                r1 = 0
                r2 = r0
                r0 = r6
                r6 = 0
            L_0x0005:
                if (r0 == 0) goto L_0x001b
                java.util.Iterator r3 = r0.iterator()     // Catch:{ all -> 0x0019 }
            L_0x000b:
                boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0019 }
                if (r4 == 0) goto L_0x001b
                java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0019 }
                r5.accept(r4)     // Catch:{ all -> 0x0019 }
                goto L_0x000b
            L_0x0019:
                r3 = move-exception
                goto L_0x003e
            L_0x001b:
                if (r2 == 0) goto L_0x0021
                r2 = 0
                r5.accept(r7)     // Catch:{ all -> 0x0019 }
            L_0x0021:
                monitor-enter(r5)     // Catch:{ all -> 0x0019 }
                java.util.List<java.lang.Object> r3 = r5.queue     // Catch:{ all -> 0x003b }
                r0 = r3
                r3 = 0
                r5.queue = r3     // Catch:{ all -> 0x003b }
                if (r0 != 0) goto L_0x0039
                r5.emitting = r1     // Catch:{ all -> 0x003b }
                r6 = 1
                monitor-exit(r5)     // Catch:{ all -> 0x003b }
                if (r6 != 0) goto L_0x0038
                monitor-enter(r5)
                r5.emitting = r1     // Catch:{ all -> 0x0035 }
                monitor-exit(r5)     // Catch:{ all -> 0x0035 }
                goto L_0x0038
            L_0x0035:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0035 }
                throw r1
            L_0x0038:
                return
            L_0x0039:
                monitor-exit(r5)     // Catch:{ all -> 0x003b }
                goto L_0x0005
            L_0x003b:
                r3 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x003b }
                throw r3     // Catch:{ all -> 0x0019 }
            L_0x003e:
                if (r6 != 0) goto L_0x0048
                monitor-enter(r5)
                r5.emitting = r1     // Catch:{ all -> 0x0045 }
                monitor-exit(r5)     // Catch:{ all -> 0x0045 }
                goto L_0x0048
            L_0x0045:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0045 }
                throw r1
            L_0x0048:
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitLoop(java.util.List, java.lang.Object):void");
        }

        /* access modifiers changed from: 0000 */
        public void accept(Object n) {
            if (n != null) {
                NotificationLite.accept(this.actual, n);
            }
        }

        /* access modifiers changed from: 0000 */
        public Observer<? super T> getActual() {
            return this.actual;
        }

        public <I> I index() {
            return this.index;
        }

        public void index(Object newIndex) {
            this.index = newIndex;
        }
    }

    public SubjectSubscriptionManager() {
        super(State.EMPTY);
    }

    public void call(Subscriber<? super T> child) {
        SubjectObserver<T> bo = new SubjectObserver<>(child);
        addUnsubscriber(child, bo);
        this.onStart.call(bo);
        if (!child.isUnsubscribed() && add(bo) && child.isUnsubscribed()) {
            remove(bo);
        }
    }

    /* access modifiers changed from: 0000 */
    public void addUnsubscriber(Subscriber<? super T> child, final SubjectObserver<T> bo) {
        child.add(Subscriptions.create(new Action0() {
            public void call() {
                SubjectSubscriptionManager.this.remove(bo);
            }
        }));
    }

    /* access modifiers changed from: 0000 */
    public void setLatest(Object value) {
        this.latest = value;
    }

    /* access modifiers changed from: 0000 */
    public Object getLatest() {
        return this.latest;
    }

    /* access modifiers changed from: 0000 */
    public SubjectObserver<T>[] observers() {
        return ((State) get()).observers;
    }

    /* access modifiers changed from: 0000 */
    public boolean add(SubjectObserver<T> o) {
        State oldState;
        do {
            oldState = (State) get();
            if (oldState.terminated) {
                this.onTerminated.call(o);
                return false;
            }
        } while (!compareAndSet(oldState, oldState.add(o)));
        this.onAdded.call(o);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(SubjectObserver<T> o) {
        State oldState;
        State newState;
        do {
            oldState = (State) get();
            if (!oldState.terminated) {
                newState = oldState.remove(o);
                if (newState == oldState) {
                    break;
                }
            } else {
                return;
            }
        } while (!compareAndSet(oldState, newState));
    }

    /* access modifiers changed from: 0000 */
    public SubjectObserver<T>[] next(Object n) {
        setLatest(n);
        return ((State) get()).observers;
    }

    /* access modifiers changed from: 0000 */
    public SubjectObserver<T>[] terminate(Object n) {
        setLatest(n);
        this.active = false;
        if (((State) get()).terminated) {
            return State.NO_OBSERVERS;
        }
        return ((State) getAndSet(State.TERMINATED)).observers;
    }
}
