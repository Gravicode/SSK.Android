package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import p008rx.Notification;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorMaterialize */
public final class OperatorMaterialize<T> implements Operator<Notification<T>, T> {

    /* renamed from: rx.internal.operators.OperatorMaterialize$Holder */
    static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE = new OperatorMaterialize<>();

        Holder() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorMaterialize$ParentSubscriber */
    static class ParentSubscriber<T> extends Subscriber<T> {
        private boolean busy;
        private final Subscriber<? super Notification<T>> child;
        private boolean missed;
        private final AtomicLong requested = new AtomicLong();
        private volatile Notification<T> terminalNotification;

        ParentSubscriber(Subscriber<? super Notification<T>> child2) {
            this.child = child2;
        }

        public void onStart() {
            request(0);
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long n) {
            BackpressureUtils.getAndAddRequest(this.requested, n);
            request(n);
            drain();
        }

        public void onCompleted() {
            this.terminalNotification = Notification.createOnCompleted();
            drain();
        }

        public void onError(Throwable e) {
            this.terminalNotification = Notification.createOnError(e);
            RxJavaHooks.onError(e);
            drain();
        }

        public void onNext(T t) {
            this.child.onNext(Notification.createOnNext(t));
            decrementRequested();
        }

        private void decrementRequested() {
            long r;
            AtomicLong localRequested = this.requested;
            do {
                r = localRequested.get();
                if (r == Long.MAX_VALUE) {
                    return;
                }
            } while (!localRequested.compareAndSet(r, r - 1));
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
            if (r6.child.isUnsubscribed() != false) goto L_0x0048;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
            r1 = r6.terminalNotification;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
            if (r1 == null) goto L_0x0039;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
            if (r0.get() <= 0) goto L_0x0039;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
            r6.terminalNotification = null;
            r6.child.onNext(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0031, code lost:
            if (r6.child.isUnsubscribed() != false) goto L_0x0038;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0033, code lost:
            r6.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
            if (r6.missed != false) goto L_0x0043;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003e, code lost:
            r6.busy = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0041, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0042, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0043, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0048, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r0 = r6.requested;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void drain() {
            /*
                r6 = this;
                monitor-enter(r6)
                boolean r0 = r6.busy     // Catch:{ all -> 0x0049 }
                if (r0 == 0) goto L_0x000a
                r0 = 1
                r6.missed = r0     // Catch:{ all -> 0x0049 }
                monitor-exit(r6)     // Catch:{ all -> 0x0049 }
                return
            L_0x000a:
                monitor-exit(r6)     // Catch:{ all -> 0x0049 }
                java.util.concurrent.atomic.AtomicLong r0 = r6.requested
            L_0x000d:
                rx.Subscriber<? super rx.Notification<T>> r1 = r6.child
                boolean r1 = r1.isUnsubscribed()
                if (r1 != 0) goto L_0x0048
                rx.Notification<T> r1 = r6.terminalNotification
                if (r1 == 0) goto L_0x0039
                long r2 = r0.get()
                r4 = 0
                int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r2 <= 0) goto L_0x0039
                r2 = 0
                r6.terminalNotification = r2
                rx.Subscriber<? super rx.Notification<T>> r2 = r6.child
                r2.onNext(r1)
                rx.Subscriber<? super rx.Notification<T>> r2 = r6.child
                boolean r2 = r2.isUnsubscribed()
                if (r2 != 0) goto L_0x0038
                rx.Subscriber<? super rx.Notification<T>> r2 = r6.child
                r2.onCompleted()
            L_0x0038:
                return
            L_0x0039:
                monitor-enter(r6)
                boolean r2 = r6.missed     // Catch:{ all -> 0x0045 }
                if (r2 != 0) goto L_0x0043
                r2 = 0
                r6.busy = r2     // Catch:{ all -> 0x0045 }
                monitor-exit(r6)     // Catch:{ all -> 0x0045 }
                return
            L_0x0043:
                monitor-exit(r6)     // Catch:{ all -> 0x0045 }
                goto L_0x000d
            L_0x0045:
                r2 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0045 }
                throw r2
            L_0x0048:
                return
            L_0x0049:
                r0 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0049 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorMaterialize.ParentSubscriber.drain():void");
        }
    }

    public static <T> OperatorMaterialize<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorMaterialize() {
    }

    public Subscriber<? super T> call(Subscriber<? super Notification<T>> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child);
        child.add(parent);
        child.setProducer(new Producer() {
            public void request(long n) {
                if (n > 0) {
                    parent.requestMore(n);
                }
            }
        });
        return parent;
    }
}
