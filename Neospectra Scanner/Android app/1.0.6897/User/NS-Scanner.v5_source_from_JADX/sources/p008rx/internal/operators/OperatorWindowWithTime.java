package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.observers.SerializedObserver;
import p008rx.observers.SerializedSubscriber;
import p008rx.subjects.UnicastSubject;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorWindowWithTime */
public final class OperatorWindowWithTime<T> implements Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    final Scheduler scheduler;
    final int size;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject */
    static final class CountedSerializedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSerializedSubject(Observer<T> consumer2, Observable<T> producer2) {
            this.consumer = new SerializedObserver(consumer2);
            this.producer = producer2;
        }
    }

    /* renamed from: rx.internal.operators.OperatorWindowWithTime$ExactSubscriber */
    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        boolean emitting;
        final Object guard = new Object();
        List<Object> queue;
        volatile State<T> state = State.empty();
        final Worker worker;

        public ExactSubscriber(Subscriber<? super Observable<T>> child2, Worker worker2) {
            this.child = new SerializedSubscriber(child2);
            this.worker = worker2;
            child2.add(Subscriptions.create(new Action0(OperatorWindowWithTime.this) {
                public void call() {
                    if (ExactSubscriber.this.state.consumer == null) {
                        ExactSubscriber.this.unsubscribe();
                    }
                }
            }));
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0023, code lost:
            if (emitValue(r7) != false) goto L_0x0032;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0025, code lost:
            if (0 != 0) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0027, code lost:
            r2 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0029, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x002c, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0031, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0036, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
            r3 = r6.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x003a, code lost:
            if (r3 != null) goto L_0x004d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x003c, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x003f, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0040, code lost:
            if (1 != 0) goto L_0x004c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0042, code lost:
            r2 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0044, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0047, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x004c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
            r6.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x004f, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0054, code lost:
            if (drain(r3) != false) goto L_0x0034;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0056, code lost:
            if (0 != 0) goto L_0x0062;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0058, code lost:
            r2 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x005a, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x005d, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x0062, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x0067, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x0068, code lost:
            if (0 == 0) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x006c, code lost:
            monitor-enter(r6.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0074, code lost:
            throw r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r7) {
            /*
                r6 = this;
                java.lang.Object r0 = r6.guard
                monitor-enter(r0)
                boolean r1 = r6.emitting     // Catch:{ all -> 0x0075 }
                if (r1 == 0) goto L_0x0019
                java.util.List<java.lang.Object> r1 = r6.queue     // Catch:{ all -> 0x0075 }
                if (r1 != 0) goto L_0x0012
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x0075 }
                r1.<init>()     // Catch:{ all -> 0x0075 }
                r6.queue = r1     // Catch:{ all -> 0x0075 }
            L_0x0012:
                java.util.List<java.lang.Object> r1 = r6.queue     // Catch:{ all -> 0x0075 }
                r1.add(r7)     // Catch:{ all -> 0x0075 }
                monitor-exit(r0)     // Catch:{ all -> 0x0075 }
                return
            L_0x0019:
                r1 = 1
                r6.emitting = r1     // Catch:{ all -> 0x0075 }
                monitor-exit(r0)     // Catch:{ all -> 0x0075 }
                r0 = 0
                r1 = r0
                boolean r2 = r6.emitValue(r7)     // Catch:{ all -> 0x0067 }
                if (r2 != 0) goto L_0x0032
                if (r1 != 0) goto L_0x0031
                java.lang.Object r2 = r6.guard
                monitor-enter(r2)
                r6.emitting = r0     // Catch:{ all -> 0x002e }
                monitor-exit(r2)     // Catch:{ all -> 0x002e }
                goto L_0x0031
            L_0x002e:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x002e }
                throw r0
            L_0x0031:
                return
            L_0x0032:
                r2 = 0
                r3 = r2
            L_0x0034:
                java.lang.Object r4 = r6.guard     // Catch:{ all -> 0x0067 }
                monitor-enter(r4)     // Catch:{ all -> 0x0067 }
                java.util.List<java.lang.Object> r5 = r6.queue     // Catch:{ all -> 0x0064 }
                r3 = r5
                if (r3 != 0) goto L_0x004d
                r6.emitting = r0     // Catch:{ all -> 0x0064 }
                r1 = 1
                monitor-exit(r4)     // Catch:{ all -> 0x0064 }
                if (r1 != 0) goto L_0x004c
                java.lang.Object r2 = r6.guard
                monitor-enter(r2)
                r6.emitting = r0     // Catch:{ all -> 0x0049 }
                monitor-exit(r2)     // Catch:{ all -> 0x0049 }
                goto L_0x004c
            L_0x0049:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0049 }
                throw r0
            L_0x004c:
                return
            L_0x004d:
                r6.queue = r2     // Catch:{ all -> 0x0064 }
                monitor-exit(r4)     // Catch:{ all -> 0x0064 }
                boolean r4 = r6.drain(r3)     // Catch:{ all -> 0x0067 }
                if (r4 != 0) goto L_0x0063
                if (r1 != 0) goto L_0x0062
                java.lang.Object r2 = r6.guard
                monitor-enter(r2)
                r6.emitting = r0     // Catch:{ all -> 0x005f }
                monitor-exit(r2)     // Catch:{ all -> 0x005f }
                goto L_0x0062
            L_0x005f:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x005f }
                throw r0
            L_0x0062:
                return
            L_0x0063:
                goto L_0x0034
            L_0x0064:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0064 }
                throw r2     // Catch:{ all -> 0x0067 }
            L_0x0067:
                r2 = move-exception
                if (r1 != 0) goto L_0x0074
                java.lang.Object r3 = r6.guard
                monitor-enter(r3)
                r6.emitting = r0     // Catch:{ all -> 0x0071 }
                monitor-exit(r3)     // Catch:{ all -> 0x0071 }
                goto L_0x0074
            L_0x0071:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0071 }
                throw r0
            L_0x0074:
                throw r2
            L_0x0075:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0075 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.onNext(java.lang.Object):void");
        }

        /* access modifiers changed from: 0000 */
        public boolean drain(List<Object> queue2) {
            if (queue2 == null) {
                return true;
            }
            Iterator i$ = queue2.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                Object o = i$.next();
                if (o == OperatorWindowWithTime.NEXT_SUBJECT) {
                    if (!replaceSubject()) {
                        return false;
                    }
                } else if (NotificationLite.isError(o)) {
                    error(NotificationLite.getError(o));
                    break;
                } else if (NotificationLite.isCompleted(o)) {
                    complete();
                    break;
                } else if (!emitValue(o)) {
                    return false;
                }
            }
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean replaceSubject() {
            Observer<T> s = this.state.consumer;
            if (s != null) {
                s.onCompleted();
            }
            if (this.child.isUnsubscribed()) {
                this.state = this.state.clear();
                unsubscribe();
                return false;
            }
            UnicastSubject<T> bus = UnicastSubject.create();
            this.state = this.state.create(bus, bus);
            this.child.onNext(bus);
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean emitValue(T t) {
            State<T> s;
            State<T> s2 = this.state;
            if (s2.consumer == null) {
                if (!replaceSubject()) {
                    return false;
                }
                s2 = this.state;
            }
            s2.consumer.onNext(t);
            if (s2.count == OperatorWindowWithTime.this.size - 1) {
                s2.consumer.onCompleted();
                s = s2.clear();
            } else {
                s = s2.next();
            }
            this.state = s;
            return true;
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(NotificationLite.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable e) {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        public void onCompleted() {
            synchronized (this.guard) {
                try {
                    if (this.emitting) {
                        if (this.queue == null) {
                            this.queue = new ArrayList();
                        }
                        this.queue.add(NotificationLite.completed());
                        return;
                    }
                    List<Object> localQueue = this.queue;
                    try {
                        this.queue = null;
                        this.emitting = true;
                        try {
                            drain(localQueue);
                            complete();
                        } catch (Throwable e) {
                            error(e);
                        }
                    } catch (Throwable th) {
                        List<Object> list = localQueue;
                        th = th;
                        List<Object> list2 = list;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void scheduleExact() {
            this.worker.schedulePeriodically(new Action0() {
                public void call() {
                    ExactSubscriber.this.nextWindow();
                }
            }, 0, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
            if (replaceSubject() != false) goto L_0x0034;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
            if (0 != 0) goto L_0x0033;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0029, code lost:
            r2 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002b, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x002e, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0033, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            r4 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0038, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
            r3 = r6.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x003c, code lost:
            if (r3 != null) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x003e, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0041, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0042, code lost:
            if (1 != 0) goto L_0x004e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0044, code lost:
            r2 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0046, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0049, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x004e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
            r6.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0051, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0056, code lost:
            if (drain(r3) != false) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0058, code lost:
            if (0 != 0) goto L_0x0064;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x005a, code lost:
            r2 = r6.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x005c, code lost:
            monitor-enter(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x005f, code lost:
            monitor-exit(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x0064, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x0069, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x006a, code lost:
            if (0 == 0) goto L_0x006c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x006e, code lost:
            monitor-enter(r6.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:?, code lost:
            r6.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0076, code lost:
            throw r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void nextWindow() {
            /*
                r6 = this;
                java.lang.Object r0 = r6.guard
                monitor-enter(r0)
                boolean r1 = r6.emitting     // Catch:{ all -> 0x0077 }
                if (r1 == 0) goto L_0x001b
                java.util.List<java.lang.Object> r1 = r6.queue     // Catch:{ all -> 0x0077 }
                if (r1 != 0) goto L_0x0012
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x0077 }
                r1.<init>()     // Catch:{ all -> 0x0077 }
                r6.queue = r1     // Catch:{ all -> 0x0077 }
            L_0x0012:
                java.util.List<java.lang.Object> r1 = r6.queue     // Catch:{ all -> 0x0077 }
                java.lang.Object r2 = p008rx.internal.operators.OperatorWindowWithTime.NEXT_SUBJECT     // Catch:{ all -> 0x0077 }
                r1.add(r2)     // Catch:{ all -> 0x0077 }
                monitor-exit(r0)     // Catch:{ all -> 0x0077 }
                return
            L_0x001b:
                r1 = 1
                r6.emitting = r1     // Catch:{ all -> 0x0077 }
                monitor-exit(r0)     // Catch:{ all -> 0x0077 }
                r0 = 0
                r1 = r0
                boolean r2 = r6.replaceSubject()     // Catch:{ all -> 0x0069 }
                if (r2 != 0) goto L_0x0034
                if (r1 != 0) goto L_0x0033
                java.lang.Object r2 = r6.guard
                monitor-enter(r2)
                r6.emitting = r0     // Catch:{ all -> 0x0030 }
                monitor-exit(r2)     // Catch:{ all -> 0x0030 }
                goto L_0x0033
            L_0x0030:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0030 }
                throw r0
            L_0x0033:
                return
            L_0x0034:
                r2 = 0
                r3 = r2
            L_0x0036:
                java.lang.Object r4 = r6.guard     // Catch:{ all -> 0x0069 }
                monitor-enter(r4)     // Catch:{ all -> 0x0069 }
                java.util.List<java.lang.Object> r5 = r6.queue     // Catch:{ all -> 0x0066 }
                r3 = r5
                if (r3 != 0) goto L_0x004f
                r6.emitting = r0     // Catch:{ all -> 0x0066 }
                r1 = 1
                monitor-exit(r4)     // Catch:{ all -> 0x0066 }
                if (r1 != 0) goto L_0x004e
                java.lang.Object r2 = r6.guard
                monitor-enter(r2)
                r6.emitting = r0     // Catch:{ all -> 0x004b }
                monitor-exit(r2)     // Catch:{ all -> 0x004b }
                goto L_0x004e
            L_0x004b:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x004b }
                throw r0
            L_0x004e:
                return
            L_0x004f:
                r6.queue = r2     // Catch:{ all -> 0x0066 }
                monitor-exit(r4)     // Catch:{ all -> 0x0066 }
                boolean r4 = r6.drain(r3)     // Catch:{ all -> 0x0069 }
                if (r4 != 0) goto L_0x0065
                if (r1 != 0) goto L_0x0064
                java.lang.Object r2 = r6.guard
                monitor-enter(r2)
                r6.emitting = r0     // Catch:{ all -> 0x0061 }
                monitor-exit(r2)     // Catch:{ all -> 0x0061 }
                goto L_0x0064
            L_0x0061:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0061 }
                throw r0
            L_0x0064:
                return
            L_0x0065:
                goto L_0x0036
            L_0x0066:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0066 }
                throw r2     // Catch:{ all -> 0x0069 }
            L_0x0069:
                r2 = move-exception
                if (r1 != 0) goto L_0x0076
                java.lang.Object r3 = r6.guard
                monitor-enter(r3)
                r6.emitting = r0     // Catch:{ all -> 0x0073 }
                monitor-exit(r3)     // Catch:{ all -> 0x0073 }
                goto L_0x0076
            L_0x0073:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0073 }
                throw r0
            L_0x0076:
                throw r2
            L_0x0077:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0077 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.nextWindow():void");
        }
    }

    /* renamed from: rx.internal.operators.OperatorWindowWithTime$InexactSubscriber */
    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<CountedSerializedSubject<T>> chunks = new LinkedList();
        boolean done;
        final Object guard = new Object();
        final Worker worker;

        public InexactSubscriber(Subscriber<? super Observable<T>> child2, Worker worker2) {
            super(child2);
            this.child = child2;
            this.worker = worker2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
            r0 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
            if (r0.hasNext() == false) goto L_0x0058;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x003f, code lost:
            r2 = (p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0.next();
            r2.consumer.onNext(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0050, code lost:
            if (r2.count != r6.this$0.size) goto L_0x0039;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0052, code lost:
            r2.consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0058, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r7) {
            /*
                r6 = this;
                java.lang.Object r0 = r6.guard
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r6.done     // Catch:{ all -> 0x0059 }
                if (r2 == 0) goto L_0x000a
                monitor-exit(r0)     // Catch:{ all -> 0x0059 }
                return
            L_0x000a:
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0059 }
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r6.chunks     // Catch:{ all -> 0x0059 }
                r2.<init>(r3)     // Catch:{ all -> 0x0059 }
                r1 = r2
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r2 = r6.chunks     // Catch:{ all -> 0x0059 }
                java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0059 }
            L_0x0018:
                boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0059 }
                if (r3 == 0) goto L_0x0034
                java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x0059 }
                rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r3 = (p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r3     // Catch:{ all -> 0x0059 }
                int r4 = r3.count     // Catch:{ all -> 0x0059 }
                int r4 = r4 + 1
                r3.count = r4     // Catch:{ all -> 0x0059 }
                rx.internal.operators.OperatorWindowWithTime r5 = p008rx.internal.operators.OperatorWindowWithTime.this     // Catch:{ all -> 0x0059 }
                int r5 = r5.size     // Catch:{ all -> 0x0059 }
                if (r4 != r5) goto L_0x0033
                r2.remove()     // Catch:{ all -> 0x0059 }
            L_0x0033:
                goto L_0x0018
            L_0x0034:
                monitor-exit(r0)     // Catch:{ all -> 0x0059 }
                java.util.Iterator r0 = r1.iterator()
            L_0x0039:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L_0x0058
                java.lang.Object r2 = r0.next()
                rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r2 = (p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r2
                rx.Observer<T> r3 = r2.consumer
                r3.onNext(r7)
                int r3 = r2.count
                rx.internal.operators.OperatorWindowWithTime r4 = p008rx.internal.operators.OperatorWindowWithTime.this
                int r4 = r4.size
                if (r3 != r4) goto L_0x0057
                rx.Observer<T> r3 = r2.consumer
                r3.onCompleted()
            L_0x0057:
                goto L_0x0039
            L_0x0058:
                return
            L_0x0059:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0059 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onNext(java.lang.Object):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001b, code lost:
            r0 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0023, code lost:
            if (r0.hasNext() == false) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
            ((p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0.next()).consumer.onError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
            r4.child.onError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0036, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r5) {
            /*
                r4 = this;
                java.lang.Object r0 = r4.guard
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r4.done     // Catch:{ all -> 0x0037 }
                if (r2 == 0) goto L_0x000a
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                return
            L_0x000a:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0037 }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0037 }
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r4.chunks     // Catch:{ all -> 0x0037 }
                r2.<init>(r3)     // Catch:{ all -> 0x0037 }
                r1 = r2
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r2 = r4.chunks     // Catch:{ all -> 0x0037 }
                r2.clear()     // Catch:{ all -> 0x0037 }
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                java.util.Iterator r0 = r1.iterator()
            L_0x001f:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L_0x0031
                java.lang.Object r2 = r0.next()
                rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r2 = (p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r2
                rx.Observer<T> r3 = r2.consumer
                r3.onError(r5)
                goto L_0x001f
            L_0x0031:
                rx.Subscriber<? super rx.Observable<T>> r0 = r4.child
                r0.onError(r5)
                return
            L_0x0037:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001b, code lost:
            r0 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0023, code lost:
            if (r0.hasNext() == false) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
            ((p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0.next()).consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
            r4.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0036, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r4 = this;
                java.lang.Object r0 = r4.guard
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r4.done     // Catch:{ all -> 0x0037 }
                if (r2 == 0) goto L_0x000a
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                return
            L_0x000a:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0037 }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0037 }
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r3 = r4.chunks     // Catch:{ all -> 0x0037 }
                r2.<init>(r3)     // Catch:{ all -> 0x0037 }
                r1 = r2
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r2 = r4.chunks     // Catch:{ all -> 0x0037 }
                r2.clear()     // Catch:{ all -> 0x0037 }
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                java.util.Iterator r0 = r1.iterator()
            L_0x001f:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L_0x0031
                java.lang.Object r2 = r0.next()
                rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r2 = (p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r2
                rx.Observer<T> r3 = r2.consumer
                r3.onCompleted()
                goto L_0x001f
            L_0x0031:
                rx.Subscriber<? super rx.Observable<T>> r0 = r4.child
                r0.onCompleted()
                return
            L_0x0037:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: 0000 */
        public void scheduleChunk() {
            this.worker.schedulePeriodically(new Action0() {
                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.unit);
        }

        /* access modifiers changed from: 0000 */
        public void startNewChunk() {
            final CountedSerializedSubject<T> chunk = createCountedSerializedSubject();
            synchronized (this.guard) {
                if (!this.done) {
                    this.chunks.add(chunk);
                    try {
                        this.child.onNext(chunk.producer);
                        this.worker.schedule(new Action0() {
                            public void call() {
                                InexactSubscriber.this.terminateChunk(chunk);
                            }
                        }, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
                    } catch (Throwable e) {
                        onError(e);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0025, code lost:
            if (r0 == false) goto L_0x002c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0027, code lost:
            r5.consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void terminateChunk(p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject<T> r5) {
            /*
                r4 = this;
                r0 = 0
                java.lang.Object r1 = r4.guard
                monitor-enter(r1)
                boolean r2 = r4.done     // Catch:{ all -> 0x002d }
                if (r2 == 0) goto L_0x000a
                monitor-exit(r1)     // Catch:{ all -> 0x002d }
                return
            L_0x000a:
                java.util.List<rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject<T>> r2 = r4.chunks     // Catch:{ all -> 0x002d }
                java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x002d }
            L_0x0010:
                boolean r3 = r2.hasNext()     // Catch:{ all -> 0x002d }
                if (r3 == 0) goto L_0x0024
                java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x002d }
                rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject r3 = (p008rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r3     // Catch:{ all -> 0x002d }
                if (r3 != r5) goto L_0x0023
                r0 = 1
                r2.remove()     // Catch:{ all -> 0x002d }
                goto L_0x0024
            L_0x0023:
                goto L_0x0010
            L_0x0024:
                monitor-exit(r1)     // Catch:{ all -> 0x002d }
                if (r0 == 0) goto L_0x002c
                rx.Observer<T> r1 = r5.consumer
                r1.onCompleted()
            L_0x002c:
                return
            L_0x002d:
                r2 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x002d }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.terminateChunk(rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject):void");
        }

        /* access modifiers changed from: 0000 */
        public CountedSerializedSubject<T> createCountedSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new CountedSerializedSubject<>(bus, bus);
        }
    }

    /* renamed from: rx.internal.operators.OperatorWindowWithTime$State */
    static final class State<T> {
        static final State<Object> EMPTY = new State<>(null, null, 0);
        final Observer<T> consumer;
        final int count;
        final Observable<T> producer;

        public State(Observer<T> consumer2, Observable<T> producer2, int count2) {
            this.consumer = consumer2;
            this.producer = producer2;
            this.count = count2;
        }

        public State<T> next() {
            return new State<>(this.consumer, this.producer, this.count + 1);
        }

        public State<T> create(Observer<T> consumer2, Observable<T> producer2) {
            return new State<>(consumer2, producer2, 0);
        }

        public State<T> clear() {
            return empty();
        }

        public static <T> State<T> empty() {
            return EMPTY;
        }
    }

    public OperatorWindowWithTime(long timespan2, long timeshift2, TimeUnit unit2, int size2, Scheduler scheduler2) {
        this.timespan = timespan2;
        this.timeshift = timeshift2;
        this.unit = unit2;
        this.size = size2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        Worker worker = this.scheduler.createWorker();
        if (this.timespan == this.timeshift) {
            ExactSubscriber s = new ExactSubscriber<>(child, worker);
            s.add(worker);
            s.scheduleExact();
            return s;
        }
        InexactSubscriber s2 = new InexactSubscriber<>(child, worker);
        s2.add(worker);
        s2.startNewChunk();
        s2.scheduleChunk();
        return s2;
    }
}
