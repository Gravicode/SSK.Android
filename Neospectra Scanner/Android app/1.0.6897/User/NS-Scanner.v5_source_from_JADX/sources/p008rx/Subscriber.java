package p008rx;

import p008rx.internal.util.SubscriptionList;

/* renamed from: rx.Subscriber */
public abstract class Subscriber<T> implements Observer<T>, Subscription {
    private static final long NOT_SET = Long.MIN_VALUE;
    private Producer producer;
    private long requested;
    private final Subscriber<?> subscriber;
    private final SubscriptionList subscriptions;

    protected Subscriber() {
        this(null, false);
    }

    protected Subscriber(Subscriber<?> subscriber2) {
        this(subscriber2, true);
    }

    protected Subscriber(Subscriber<?> subscriber2, boolean shareSubscriptions) {
        this.requested = NOT_SET;
        this.subscriber = subscriber2;
        this.subscriptions = (!shareSubscriptions || subscriber2 == null) ? new SubscriptionList() : subscriber2.subscriptions;
    }

    public final void add(Subscription s) {
        this.subscriptions.add(s);
    }

    public final void unsubscribe() {
        this.subscriptions.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.subscriptions.isUnsubscribed();
    }

    public void onStart() {
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        if (n < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("number requested cannot be negative: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        synchronized (this) {
            if (this.producer != null) {
                Producer producerToRequestFrom = this.producer;
                producerToRequestFrom.request(n);
                return;
            }
            addToRequested(n);
        }
    }

    private void addToRequested(long n) {
        if (this.requested == NOT_SET) {
            this.requested = n;
            return;
        }
        long total = this.requested + n;
        if (total < 0) {
            this.requested = Long.MAX_VALUE;
        } else {
            this.requested = total;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0012, code lost:
        if (r0 == false) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
        r6.subscriber.setProducer(r6.producer);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001e, code lost:
        if (r1 != NOT_SET) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
        r6.producer.request(Long.MAX_VALUE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002b, code lost:
        r6.producer.request(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(p008rx.Producer r7) {
        /*
            r6 = this;
            r0 = 0
            monitor-enter(r6)
            long r1 = r6.requested     // Catch:{ all -> 0x0033 }
            r6.producer = r7     // Catch:{ all -> 0x0031 }
            rx.Subscriber<?> r3 = r6.subscriber     // Catch:{ all -> 0x0031 }
            r4 = -9223372036854775808
            if (r3 == 0) goto L_0x0011
            int r3 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r3 != 0) goto L_0x0011
            r0 = 1
        L_0x0011:
            monitor-exit(r6)     // Catch:{ all -> 0x0031 }
            if (r0 == 0) goto L_0x001c
            rx.Subscriber<?> r3 = r6.subscriber
            rx.Producer r4 = r6.producer
            r3.setProducer(r4)
            goto L_0x0030
        L_0x001c:
            int r3 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r3 != 0) goto L_0x002b
            rx.Producer r3 = r6.producer
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r3.request(r4)
            goto L_0x0030
        L_0x002b:
            rx.Producer r3 = r6.producer
            r3.request(r1)
        L_0x0030:
            return
        L_0x0031:
            r3 = move-exception
            goto L_0x0036
        L_0x0033:
            r3 = move-exception
            r1 = 0
        L_0x0036:
            monitor-exit(r6)     // Catch:{ all -> 0x0031 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.Subscriber.setProducer(rx.Producer):void");
    }
}
