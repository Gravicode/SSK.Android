package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.observers.SerializedSubscriber;

/* renamed from: rx.internal.operators.OperatorBufferWithTime */
public final class OperatorBufferWithTime<T> implements Operator<List<T>, T> {
    final int count;
    final Scheduler scheduler;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorBufferWithTime$ExactSubscriber */
    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk = new ArrayList();
        boolean done;
        final Worker inner;

        public ExactSubscriber(Subscriber<? super List<T>> child2, Worker inner2) {
            this.child = child2;
            this.inner = inner2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
            if (r0 == null) goto L_0x002b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0026, code lost:
            r3.child.onNext(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r4) {
            /*
                r3 = this;
                r0 = 0
                monitor-enter(r3)
                boolean r1 = r3.done     // Catch:{ all -> 0x002c }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r3)     // Catch:{ all -> 0x002c }
                return
            L_0x0008:
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x002c }
                r1.add(r4)     // Catch:{ all -> 0x002c }
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x002c }
                int r1 = r1.size()     // Catch:{ all -> 0x002c }
                rx.internal.operators.OperatorBufferWithTime r2 = p008rx.internal.operators.OperatorBufferWithTime.this     // Catch:{ all -> 0x002c }
                int r2 = r2.count     // Catch:{ all -> 0x002c }
                if (r1 != r2) goto L_0x0023
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x002c }
                r0 = r1
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x002c }
                r1.<init>()     // Catch:{ all -> 0x002c }
                r3.chunk = r1     // Catch:{ all -> 0x002c }
            L_0x0023:
                monitor-exit(r3)     // Catch:{ all -> 0x002c }
                if (r0 == 0) goto L_0x002b
                rx.Subscriber<? super java.util.List<T>> r1 = r3.child
                r1.onNext(r0)
            L_0x002b:
                return
            L_0x002c:
                r1 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002c }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    this.chunk = null;
                    this.child.onError(e);
                    unsubscribe();
                }
            }
        }

        public void onCompleted() {
            try {
                this.inner.unsubscribe();
                synchronized (this) {
                    try {
                        if (!this.done) {
                            this.done = true;
                            List<T> toEmit = this.chunk;
                            try {
                                this.chunk = null;
                                this.child.onNext(toEmit);
                                this.child.onCompleted();
                                unsubscribe();
                            } catch (Throwable th) {
                                List<T> list = toEmit;
                                th = th;
                                List<T> list2 = list;
                                throw th;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            } catch (Throwable t) {
                Exceptions.throwOrReport(t, (Observer<?>) this.child);
            }
        }

        /* access modifiers changed from: 0000 */
        public void scheduleExact() {
            this.inner.schedulePeriodically(new Action0() {
                public void call() {
                    ExactSubscriber.this.emit();
                }
            }, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            r2.child.onNext(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0019, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
            p008rx.exceptions.Exceptions.throwOrReport(r1, (p008rx.Observer<?>) r2);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit() {
            /*
                r2 = this;
                monitor-enter(r2)
                r0 = 0
                boolean r1 = r2.done     // Catch:{ all -> 0x001e }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r2)     // Catch:{ all -> 0x001e }
                return
            L_0x0008:
                java.util.List<T> r1 = r2.chunk     // Catch:{ all -> 0x001e }
                r0 = r1
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x001e }
                r1.<init>()     // Catch:{ all -> 0x001e }
                r2.chunk = r1     // Catch:{ all -> 0x001e }
                monitor-exit(r2)     // Catch:{ all -> 0x001e }
                rx.Subscriber<? super java.util.List<T>> r1 = r2.child     // Catch:{ Throwable -> 0x0019 }
                r1.onNext(r0)     // Catch:{ Throwable -> 0x0019 }
                goto L_0x001d
            L_0x0019:
                r1 = move-exception
                p008rx.exceptions.Exceptions.throwOrReport(r1, r2)
            L_0x001d:
                return
            L_0x001e:
                r1 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x001e }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.emit():void");
        }
    }

    /* renamed from: rx.internal.operators.OperatorBufferWithTime$InexactSubscriber */
    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks = new LinkedList();
        boolean done;
        final Worker inner;

        public InexactSubscriber(Subscriber<? super List<T>> child2, Worker inner2) {
            this.child = child2;
            this.inner = inner2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0037, code lost:
            if (r0 == null) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
            r1 = r0.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
            if (r1.hasNext() == false) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0043, code lost:
            r5.child.onNext((java.util.List) r1.next());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004f, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
                r5 = this;
                r0 = 0
                monitor-enter(r5)
                boolean r1 = r5.done     // Catch:{ all -> 0x0050 }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r5)     // Catch:{ all -> 0x0050 }
                return
            L_0x0008:
                java.util.List<java.util.List<T>> r1 = r5.chunks     // Catch:{ all -> 0x0050 }
                java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0050 }
            L_0x000e:
                boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0050 }
                if (r2 == 0) goto L_0x0036
                java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0050 }
                java.util.List r2 = (java.util.List) r2     // Catch:{ all -> 0x0050 }
                r2.add(r6)     // Catch:{ all -> 0x0050 }
                int r3 = r2.size()     // Catch:{ all -> 0x0050 }
                rx.internal.operators.OperatorBufferWithTime r4 = p008rx.internal.operators.OperatorBufferWithTime.this     // Catch:{ all -> 0x0050 }
                int r4 = r4.count     // Catch:{ all -> 0x0050 }
                if (r3 != r4) goto L_0x0035
                r1.remove()     // Catch:{ all -> 0x0050 }
                if (r0 != 0) goto L_0x0032
                java.util.LinkedList r3 = new java.util.LinkedList     // Catch:{ all -> 0x0050 }
                r3.<init>()     // Catch:{ all -> 0x0050 }
                r0 = r3
            L_0x0032:
                r0.add(r2)     // Catch:{ all -> 0x0050 }
            L_0x0035:
                goto L_0x000e
            L_0x0036:
                monitor-exit(r5)     // Catch:{ all -> 0x0050 }
                if (r0 == 0) goto L_0x004f
                java.util.Iterator r1 = r0.iterator()
            L_0x003d:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x004f
                java.lang.Object r2 = r1.next()
                java.util.List r2 = (java.util.List) r2
                rx.Subscriber<? super java.util.List<T>> r3 = r5.child
                r3.onNext(r2)
                goto L_0x003d
            L_0x004f:
                return
            L_0x0050:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0050 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (!this.done) {
                    this.done = true;
                    this.chunks.clear();
                    this.child.onError(e);
                    unsubscribe();
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
            r1 = r0.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r1.hasNext() == false) goto L_0x002f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            r4.child.onNext((java.util.List) r1.next());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002f, code lost:
            r4.child.onCompleted();
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0038, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r4 = this;
                monitor-enter(r4)     // Catch:{ Throwable -> 0x003c }
                r0 = 0
                boolean r1 = r4.done     // Catch:{ all -> 0x0039 }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r4)     // Catch:{ all -> 0x0039 }
                return
            L_0x0008:
                r1 = 1
                r4.done = r1     // Catch:{ all -> 0x0039 }
                java.util.LinkedList r1 = new java.util.LinkedList     // Catch:{ all -> 0x0039 }
                java.util.List<java.util.List<T>> r2 = r4.chunks     // Catch:{ all -> 0x0039 }
                r1.<init>(r2)     // Catch:{ all -> 0x0039 }
                r0 = r1
                java.util.List<java.util.List<T>> r1 = r4.chunks     // Catch:{ all -> 0x0039 }
                r1.clear()     // Catch:{ all -> 0x0039 }
                monitor-exit(r4)     // Catch:{ all -> 0x0039 }
                java.util.Iterator r1 = r0.iterator()     // Catch:{ Throwable -> 0x003c }
            L_0x001d:
                boolean r2 = r1.hasNext()     // Catch:{ Throwable -> 0x003c }
                if (r2 == 0) goto L_0x002f
                java.lang.Object r2 = r1.next()     // Catch:{ Throwable -> 0x003c }
                java.util.List r2 = (java.util.List) r2     // Catch:{ Throwable -> 0x003c }
                rx.Subscriber<? super java.util.List<T>> r3 = r4.child     // Catch:{ Throwable -> 0x003c }
                r3.onNext(r2)     // Catch:{ Throwable -> 0x003c }
                goto L_0x001d
            L_0x002f:
                rx.Subscriber<? super java.util.List<T>> r0 = r4.child
                r0.onCompleted()
                r4.unsubscribe()
                return
            L_0x0039:
                r1 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0039 }
                throw r1     // Catch:{ Throwable -> 0x003c }
            L_0x003c:
                r0 = move-exception
                rx.Subscriber<? super java.util.List<T>> r1 = r4.child
                p008rx.exceptions.Exceptions.throwOrReport(r0, r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: 0000 */
        public void scheduleChunk() {
            this.inner.schedulePeriodically(new Action0() {
                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.unit);
        }

        /* access modifiers changed from: 0000 */
        public void startNewChunk() {
            final List<T> chunk = new ArrayList<>();
            synchronized (this) {
                if (!this.done) {
                    this.chunks.add(chunk);
                    this.inner.schedule(new Action0() {
                        public void call() {
                            InexactSubscriber.this.emitChunk(chunk);
                        }
                    }, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
            if (r0 == false) goto L_0x002f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
            r3.child.onNext(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002b, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002c, code lost:
            p008rx.exceptions.Exceptions.throwOrReport(r1, (p008rx.Observer<?>) r3);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitChunk(java.util.List<T> r4) {
            /*
                r3 = this;
                r0 = 0
                monitor-enter(r3)
                boolean r1 = r3.done     // Catch:{ all -> 0x0030 }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r3)     // Catch:{ all -> 0x0030 }
                return
            L_0x0008:
                java.util.List<java.util.List<T>> r1 = r3.chunks     // Catch:{ all -> 0x0030 }
                java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0030 }
            L_0x000e:
                boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0030 }
                if (r2 == 0) goto L_0x0022
                java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0030 }
                java.util.List r2 = (java.util.List) r2     // Catch:{ all -> 0x0030 }
                if (r2 != r4) goto L_0x0021
                r1.remove()     // Catch:{ all -> 0x0030 }
                r0 = 1
                goto L_0x0022
            L_0x0021:
                goto L_0x000e
            L_0x0022:
                monitor-exit(r3)     // Catch:{ all -> 0x0030 }
                if (r0 == 0) goto L_0x002f
                rx.Subscriber<? super java.util.List<T>> r1 = r3.child     // Catch:{ Throwable -> 0x002b }
                r1.onNext(r4)     // Catch:{ Throwable -> 0x002b }
                goto L_0x002f
            L_0x002b:
                r1 = move-exception
                p008rx.exceptions.Exceptions.throwOrReport(r1, r3)
            L_0x002f:
                return
            L_0x0030:
                r1 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0030 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.emitChunk(java.util.List):void");
        }
    }

    public OperatorBufferWithTime(long timespan2, long timeshift2, TimeUnit unit2, int count2, Scheduler scheduler2) {
        this.timespan = timespan2;
        this.timeshift = timeshift2;
        this.unit = unit2;
        this.count = count2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        Worker inner = this.scheduler.createWorker();
        SerializedSubscriber<List<T>> serialized = new SerializedSubscriber<>(child);
        if (this.timespan == this.timeshift) {
            ExactSubscriber parent = new ExactSubscriber<>(serialized, inner);
            parent.add(inner);
            child.add(parent);
            parent.scheduleExact();
            return parent;
        }
        InexactSubscriber parent2 = new InexactSubscriber<>(serialized, inner);
        parent2.add(inner);
        child.add(parent2);
        parent2.startNewChunk();
        parent2.scheduleChunk();
        return parent2;
    }
}
