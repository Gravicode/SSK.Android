package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.observers.SerializedSubscriber;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OperatorDebounceWithTime */
public final class OperatorDebounceWithTime<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorDebounceWithTime$DebounceState */
    static final class DebounceState<T> {
        boolean emitting;
        boolean hasValue;
        int index;
        boolean terminate;
        T value;

        DebounceState() {
        }

        public synchronized int next(T value2) {
            int i;
            this.value = value2;
            this.hasValue = true;
            i = this.index + 1;
            this.index = i;
            return i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            r6.onNext(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x001e, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0021, code lost:
            if (r4.terminate != false) goto L_0x0027;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0023, code lost:
            r4.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0025, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0026, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0027, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0028, code lost:
            r6.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x002b, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x002f, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0030, code lost:
            p008rx.exceptions.Exceptions.throwOrReport(r0, (p008rx.Observer<?>) r7, (java.lang.Object) r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0033, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit(int r5, p008rx.Subscriber<T> r6, p008rx.Subscriber<?> r7) {
            /*
                r4 = this;
                monitor-enter(r4)
                r0 = 0
                boolean r1 = r4.emitting     // Catch:{ all -> 0x003b }
                if (r1 != 0) goto L_0x0039
                boolean r1 = r4.hasValue     // Catch:{ all -> 0x003b }
                if (r1 == 0) goto L_0x0039
                int r1 = r4.index     // Catch:{ all -> 0x003b }
                if (r5 == r1) goto L_0x000f
                goto L_0x0039
            L_0x000f:
                T r1 = r4.value     // Catch:{ all -> 0x003b }
                r4.value = r0     // Catch:{ all -> 0x0034 }
                r0 = 0
                r4.hasValue = r0     // Catch:{ all -> 0x0034 }
                r2 = 1
                r4.emitting = r2     // Catch:{ all -> 0x0034 }
                monitor-exit(r4)     // Catch:{ all -> 0x0034 }
                r6.onNext(r1)     // Catch:{ Throwable -> 0x002f }
                monitor-enter(r4)
                boolean r2 = r4.terminate     // Catch:{ all -> 0x002c }
                if (r2 != 0) goto L_0x0027
                r4.emitting = r0     // Catch:{ all -> 0x002c }
                monitor-exit(r4)     // Catch:{ all -> 0x002c }
                return
            L_0x0027:
                monitor-exit(r4)     // Catch:{ all -> 0x002c }
                r6.onCompleted()
                return
            L_0x002c:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x002c }
                throw r0
            L_0x002f:
                r0 = move-exception
                p008rx.exceptions.Exceptions.throwOrReport(r0, r7, r1)
                return
            L_0x0034:
                r0 = move-exception
                r3 = r1
                r1 = r0
                r0 = r3
                goto L_0x003c
            L_0x0039:
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                return
            L_0x003b:
                r1 = move-exception
            L_0x003c:
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorDebounceWithTime.DebounceState.emit(int, rx.Subscriber, rx.Subscriber):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0017, code lost:
            if (r4 == false) goto L_0x0022;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
            r7.onNext(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x001d, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x001e, code lost:
            p008rx.exceptions.Exceptions.throwOrReport(r0, (p008rx.Observer<?>) r8, (java.lang.Object) r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0021, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0022, code lost:
            r7.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0025, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitAndComplete(p008rx.Subscriber<T> r7, p008rx.Subscriber<?> r8) {
            /*
                r6 = this;
                monitor-enter(r6)
                r0 = 0
                r1 = 0
                boolean r2 = r6.emitting     // Catch:{ all -> 0x0029 }
                r3 = 1
                if (r2 == 0) goto L_0x000c
                r6.terminate = r3     // Catch:{ all -> 0x0029 }
                monitor-exit(r6)     // Catch:{ all -> 0x0029 }
                return
            L_0x000c:
                T r2 = r6.value     // Catch:{ all -> 0x0029 }
                boolean r4 = r6.hasValue     // Catch:{ all -> 0x002f }
                r6.value = r0     // Catch:{ all -> 0x0026 }
                r6.hasValue = r1     // Catch:{ all -> 0x0026 }
                r6.emitting = r3     // Catch:{ all -> 0x0026 }
                monitor-exit(r6)     // Catch:{ all -> 0x0026 }
                if (r4 == 0) goto L_0x0022
                r7.onNext(r2)     // Catch:{ Throwable -> 0x001d }
                goto L_0x0022
            L_0x001d:
                r0 = move-exception
                p008rx.exceptions.Exceptions.throwOrReport(r0, r8, r2)
                return
            L_0x0022:
                r7.onCompleted()
                return
            L_0x0026:
                r0 = move-exception
                r1 = r4
                goto L_0x002d
            L_0x0029:
                r2 = move-exception
                r5 = r2
                r2 = r0
                r0 = r5
            L_0x002d:
                monitor-exit(r6)     // Catch:{ all -> 0x002f }
                throw r0
            L_0x002f:
                r0 = move-exception
                goto L_0x002d
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorDebounceWithTime.DebounceState.emitAndComplete(rx.Subscriber, rx.Subscriber):void");
        }

        public synchronized void clear() {
            this.index++;
            this.value = null;
            this.hasValue = false;
        }
    }

    public OperatorDebounceWithTime(long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(child);
        SerialSubscription serial = new SerialSubscription();
        serializedSubscriber.add(worker);
        serializedSubscriber.add(serial);
        final SerialSubscription serialSubscription = serial;
        final Worker worker2 = worker;
        final SerializedSubscriber serializedSubscriber2 = serializedSubscriber;
        C15911 r1 = new Subscriber<T>(child) {
            final Subscriber<?> self = this;
            final DebounceState<T> state = new DebounceState<>();

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                final int index = this.state.next(t);
                serialSubscription.set(worker2.schedule(new Action0() {
                    public void call() {
                        C15911.this.state.emit(index, serializedSubscriber2, C15911.this.self);
                    }
                }, OperatorDebounceWithTime.this.timeout, OperatorDebounceWithTime.this.unit));
            }

            public void onError(Throwable e) {
                serializedSubscriber2.onError(e);
                unsubscribe();
                this.state.clear();
            }

            public void onCompleted() {
                this.state.emitAndComplete(serializedSubscriber2, this);
            }
        };
        return r1;
    }
}
