package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.observers.SerializedSubscriber;
import p008rx.subjects.UnicastSubject;

/* renamed from: rx.internal.operators.OperatorWindowWithObservable */
public final class OperatorWindowWithObservable<T, U> implements Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    final Observable<U> other;

    /* renamed from: rx.internal.operators.OperatorWindowWithObservable$BoundarySubscriber */
    static final class BoundarySubscriber<T, U> extends Subscriber<U> {
        final SourceSubscriber<T> sub;

        public BoundarySubscriber(SourceSubscriber<T> sub2) {
            this.sub = sub2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(U u) {
            this.sub.replaceWindow();
        }

        public void onError(Throwable e) {
            this.sub.onError(e);
        }

        public void onCompleted() {
            this.sub.onCompleted();
        }
    }

    /* renamed from: rx.internal.operators.OperatorWindowWithObservable$SourceSubscriber */
    static final class SourceSubscriber<T> extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        Observer<T> consumer;
        boolean emitting;
        final Object guard = new Object();
        Observable<T> producer;
        List<Object> queue;

        public SourceSubscriber(Subscriber<? super Observable<T>> child2) {
            this.child = new SerializedSubscriber(child2);
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0022, code lost:
            r4 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            drain(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0029, code lost:
            if (r4 == false) goto L_0x002f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002b, code lost:
            r4 = false;
            emitValue(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x002f, code lost:
            r5 = r8.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0031, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            r2 = r8.queue;
            r8.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0037, code lost:
            if (r2 != null) goto L_0x004a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0039, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x003c, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x003d, code lost:
            if (1 != 0) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x003f, code lost:
            r1 = r8.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0041, code lost:
            monitor-enter(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0044, code lost:
            monitor-exit(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0049, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0051, code lost:
            if (r8.child.isUnsubscribed() == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0053, code lost:
            if (0 != 0) goto L_0x005f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0055, code lost:
            r1 = r8.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0057, code lost:
            monitor-enter(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x005a, code lost:
            monitor-exit(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x005f, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x0063, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x0064, code lost:
            if (0 == 0) goto L_0x0066;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x0068, code lost:
            monitor-enter(r8.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x0070, code lost:
            throw r1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r9) {
            /*
                r8 = this;
                java.lang.Object r0 = r8.guard
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r8.emitting     // Catch:{ all -> 0x0076 }
                if (r2 == 0) goto L_0x001a
                java.util.List<java.lang.Object> r2 = r8.queue     // Catch:{ all -> 0x0076 }
                if (r2 != 0) goto L_0x0013
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0076 }
                r2.<init>()     // Catch:{ all -> 0x0076 }
                r8.queue = r2     // Catch:{ all -> 0x0076 }
            L_0x0013:
                java.util.List<java.lang.Object> r2 = r8.queue     // Catch:{ all -> 0x0076 }
                r2.add(r9)     // Catch:{ all -> 0x0076 }
                monitor-exit(r0)     // Catch:{ all -> 0x0076 }
                return
            L_0x001a:
                java.util.List<java.lang.Object> r2 = r8.queue     // Catch:{ all -> 0x0076 }
                r8.queue = r1     // Catch:{ all -> 0x0071 }
                r3 = 1
                r8.emitting = r3     // Catch:{ all -> 0x0071 }
                monitor-exit(r0)     // Catch:{ all -> 0x0071 }
                r0 = 1
                r3 = 0
                r4 = r0
                r0 = 0
            L_0x0026:
                r8.drain(r2)     // Catch:{ all -> 0x0063 }
                if (r4 == 0) goto L_0x002f
                r4 = 0
                r8.emitValue(r9)     // Catch:{ all -> 0x0063 }
            L_0x002f:
                java.lang.Object r5 = r8.guard     // Catch:{ all -> 0x0063 }
                monitor-enter(r5)     // Catch:{ all -> 0x0063 }
                java.util.List<java.lang.Object> r6 = r8.queue     // Catch:{ all -> 0x0060 }
                r2 = r6
                r8.queue = r1     // Catch:{ all -> 0x0060 }
                if (r2 != 0) goto L_0x004a
                r8.emitting = r3     // Catch:{ all -> 0x0060 }
                r0 = 1
                monitor-exit(r5)     // Catch:{ all -> 0x0060 }
                if (r0 != 0) goto L_0x0049
                java.lang.Object r1 = r8.guard
                monitor-enter(r1)
                r8.emitting = r3     // Catch:{ all -> 0x0046 }
                monitor-exit(r1)     // Catch:{ all -> 0x0046 }
                goto L_0x0049
            L_0x0046:
                r3 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0046 }
                throw r3
            L_0x0049:
                return
            L_0x004a:
                monitor-exit(r5)     // Catch:{ all -> 0x0060 }
                rx.Subscriber<? super rx.Observable<T>> r5 = r8.child     // Catch:{ all -> 0x0063 }
                boolean r5 = r5.isUnsubscribed()     // Catch:{ all -> 0x0063 }
                if (r5 == 0) goto L_0x0026
                if (r0 != 0) goto L_0x005f
                java.lang.Object r1 = r8.guard
                monitor-enter(r1)
                r8.emitting = r3     // Catch:{ all -> 0x005c }
                monitor-exit(r1)     // Catch:{ all -> 0x005c }
                goto L_0x005f
            L_0x005c:
                r3 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x005c }
                throw r3
            L_0x005f:
                return
            L_0x0060:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0060 }
                throw r1     // Catch:{ all -> 0x0063 }
            L_0x0063:
                r1 = move-exception
                if (r0 != 0) goto L_0x0070
                java.lang.Object r5 = r8.guard
                monitor-enter(r5)
                r8.emitting = r3     // Catch:{ all -> 0x006d }
                monitor-exit(r5)     // Catch:{ all -> 0x006d }
                goto L_0x0070
            L_0x006d:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x006d }
                throw r1
            L_0x0070:
                throw r1
            L_0x0071:
                r1 = move-exception
                r7 = r2
                r2 = r1
                r1 = r7
                goto L_0x0077
            L_0x0076:
                r2 = move-exception
            L_0x0077:
                monitor-exit(r0)     // Catch:{ all -> 0x0076 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithObservable.SourceSubscriber.onNext(java.lang.Object):void");
        }

        /* access modifiers changed from: 0000 */
        public void drain(List<Object> queue2) {
            if (queue2 != null) {
                Iterator i$ = queue2.iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    Object o = i$.next();
                    if (o == OperatorWindowWithObservable.NEXT_SUBJECT) {
                        replaceSubject();
                    } else if (NotificationLite.isError(o)) {
                        error(NotificationLite.getError(o));
                        break;
                    } else if (NotificationLite.isCompleted(o)) {
                        complete();
                        break;
                    } else {
                        emitValue(o);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void replaceSubject() {
            Observer<T> s = this.consumer;
            if (s != null) {
                s.onCompleted();
            }
            createNewWindow();
            this.child.onNext(this.producer);
        }

        /* access modifiers changed from: 0000 */
        public void createNewWindow() {
            UnicastSubject<T> bus = UnicastSubject.create();
            this.consumer = bus;
            this.producer = bus;
        }

        /* access modifiers changed from: 0000 */
        public void emitValue(T t) {
            Observer<T> s = this.consumer;
            if (s != null) {
                s.onNext(t);
            }
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
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            r4 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            drain(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002b, code lost:
            if (r4 == false) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
            r4 = false;
            replaceSubject();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0031, code lost:
            r5 = r8.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0033, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            r2 = r8.queue;
            r8.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0039, code lost:
            if (r2 != null) goto L_0x004c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x003b, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x003e, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x003f, code lost:
            if (1 != 0) goto L_0x004b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0041, code lost:
            r1 = r8.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0043, code lost:
            monitor-enter(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0046, code lost:
            monitor-exit(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x004b, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0053, code lost:
            if (r8.child.isUnsubscribed() == false) goto L_0x0028;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0055, code lost:
            if (0 != 0) goto L_0x0061;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0057, code lost:
            r1 = r8.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0059, code lost:
            monitor-enter(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x005c, code lost:
            monitor-exit(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0061, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x0065, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x0066, code lost:
            if (0 == 0) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x006a, code lost:
            monitor-enter(r8.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x0072, code lost:
            throw r1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replaceWindow() {
            /*
                r8 = this;
                java.lang.Object r0 = r8.guard
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r8.emitting     // Catch:{ all -> 0x0078 }
                if (r2 == 0) goto L_0x001c
                java.util.List<java.lang.Object> r2 = r8.queue     // Catch:{ all -> 0x0078 }
                if (r2 != 0) goto L_0x0013
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0078 }
                r2.<init>()     // Catch:{ all -> 0x0078 }
                r8.queue = r2     // Catch:{ all -> 0x0078 }
            L_0x0013:
                java.util.List<java.lang.Object> r2 = r8.queue     // Catch:{ all -> 0x0078 }
                java.lang.Object r3 = p008rx.internal.operators.OperatorWindowWithObservable.NEXT_SUBJECT     // Catch:{ all -> 0x0078 }
                r2.add(r3)     // Catch:{ all -> 0x0078 }
                monitor-exit(r0)     // Catch:{ all -> 0x0078 }
                return
            L_0x001c:
                java.util.List<java.lang.Object> r2 = r8.queue     // Catch:{ all -> 0x0078 }
                r8.queue = r1     // Catch:{ all -> 0x0073 }
                r3 = 1
                r8.emitting = r3     // Catch:{ all -> 0x0073 }
                monitor-exit(r0)     // Catch:{ all -> 0x0073 }
                r0 = 1
                r3 = 0
                r4 = r0
                r0 = 0
            L_0x0028:
                r8.drain(r2)     // Catch:{ all -> 0x0065 }
                if (r4 == 0) goto L_0x0031
                r4 = 0
                r8.replaceSubject()     // Catch:{ all -> 0x0065 }
            L_0x0031:
                java.lang.Object r5 = r8.guard     // Catch:{ all -> 0x0065 }
                monitor-enter(r5)     // Catch:{ all -> 0x0065 }
                java.util.List<java.lang.Object> r6 = r8.queue     // Catch:{ all -> 0x0062 }
                r2 = r6
                r8.queue = r1     // Catch:{ all -> 0x0062 }
                if (r2 != 0) goto L_0x004c
                r8.emitting = r3     // Catch:{ all -> 0x0062 }
                r0 = 1
                monitor-exit(r5)     // Catch:{ all -> 0x0062 }
                if (r0 != 0) goto L_0x004b
                java.lang.Object r1 = r8.guard
                monitor-enter(r1)
                r8.emitting = r3     // Catch:{ all -> 0x0048 }
                monitor-exit(r1)     // Catch:{ all -> 0x0048 }
                goto L_0x004b
            L_0x0048:
                r3 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0048 }
                throw r3
            L_0x004b:
                return
            L_0x004c:
                monitor-exit(r5)     // Catch:{ all -> 0x0062 }
                rx.Subscriber<? super rx.Observable<T>> r5 = r8.child     // Catch:{ all -> 0x0065 }
                boolean r5 = r5.isUnsubscribed()     // Catch:{ all -> 0x0065 }
                if (r5 == 0) goto L_0x0028
                if (r0 != 0) goto L_0x0061
                java.lang.Object r1 = r8.guard
                monitor-enter(r1)
                r8.emitting = r3     // Catch:{ all -> 0x005e }
                monitor-exit(r1)     // Catch:{ all -> 0x005e }
                goto L_0x0061
            L_0x005e:
                r3 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x005e }
                throw r3
            L_0x0061:
                return
            L_0x0062:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0062 }
                throw r1     // Catch:{ all -> 0x0065 }
            L_0x0065:
                r1 = move-exception
                if (r0 != 0) goto L_0x0072
                java.lang.Object r5 = r8.guard
                monitor-enter(r5)
                r8.emitting = r3     // Catch:{ all -> 0x006f }
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                goto L_0x0072
            L_0x006f:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                throw r1
            L_0x0072:
                throw r1
            L_0x0073:
                r1 = move-exception
                r7 = r2
                r2 = r1
                r1 = r7
                goto L_0x0079
            L_0x0078:
                r2 = move-exception
            L_0x0079:
                monitor-exit(r0)     // Catch:{ all -> 0x0078 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithObservable.SourceSubscriber.replaceWindow():void");
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            Observer<T> s = this.consumer;
            this.consumer = null;
            this.producer = null;
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable e) {
            Observer<T> s = this.consumer;
            this.consumer = null;
            this.producer = null;
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }
    }

    public OperatorWindowWithObservable(Observable<U> other2) {
        this.other = other2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        SourceSubscriber<T> sub = new SourceSubscriber<>(child);
        BoundarySubscriber<T, U> bs = new BoundarySubscriber<>(sub);
        child.add(sub);
        child.add(bs);
        sub.replaceWindow();
        this.other.unsafeSubscribe(bs);
        return sub;
    }
}
