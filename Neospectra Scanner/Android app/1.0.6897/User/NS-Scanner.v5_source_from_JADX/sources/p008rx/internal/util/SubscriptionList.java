package p008rx.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.internal.util.SubscriptionList */
public final class SubscriptionList implements Subscription {
    private List<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public SubscriptionList() {
    }

    public SubscriptionList(Subscription... subscriptions2) {
        this.subscriptions = new LinkedList(Arrays.asList(subscriptions2));
    }

    public SubscriptionList(Subscription s) {
        this.subscriptions = new LinkedList();
        this.subscriptions.add(s);
    }

    public boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(Subscription s) {
        if (!s.isUnsubscribed()) {
            if (!this.unsubscribed) {
                synchronized (this) {
                    if (!this.unsubscribed) {
                        List<Subscription> subs = this.subscriptions;
                        if (subs == null) {
                            subs = new LinkedList<>();
                            this.subscriptions = subs;
                        }
                        subs.add(s);
                        return;
                    }
                }
            }
            s.unsubscribe();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        if (r0 == false) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        r4.unsubscribe();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(p008rx.Subscription r4) {
        /*
            r3 = this;
            boolean r0 = r3.unsubscribed
            if (r0 != 0) goto L_0x0020
            monitor-enter(r3)
            r0 = 0
            java.util.List<rx.Subscription> r1 = r3.subscriptions     // Catch:{ all -> 0x001d }
            boolean r2 = r3.unsubscribed     // Catch:{ all -> 0x001d }
            if (r2 != 0) goto L_0x001b
            if (r1 != 0) goto L_0x000f
            goto L_0x001b
        L_0x000f:
            boolean r2 = r1.remove(r4)     // Catch:{ all -> 0x001d }
            r0 = r2
            monitor-exit(r3)     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x0020
            r4.unsubscribe()
            goto L_0x0020
        L_0x001b:
            monitor-exit(r3)     // Catch:{ all -> 0x001d }
            return
        L_0x001d:
            r1 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x001d }
            throw r1
        L_0x0020:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.util.SubscriptionList.remove(rx.Subscription):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0014, code lost:
        unsubscribeFromAll(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unsubscribe() {
        /*
            r3 = this;
            boolean r0 = r3.unsubscribed
            if (r0 != 0) goto L_0x0020
            monitor-enter(r3)
            r0 = 0
            boolean r1 = r3.unsubscribed     // Catch:{ all -> 0x001d }
            if (r1 == 0) goto L_0x000c
            monitor-exit(r3)     // Catch:{ all -> 0x001d }
            return
        L_0x000c:
            r1 = 1
            r3.unsubscribed = r1     // Catch:{ all -> 0x001d }
            java.util.List<rx.Subscription> r1 = r3.subscriptions     // Catch:{ all -> 0x001d }
            r3.subscriptions = r0     // Catch:{ all -> 0x0018 }
            monitor-exit(r3)     // Catch:{ all -> 0x0018 }
            unsubscribeFromAll(r1)
            goto L_0x0020
        L_0x0018:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
            goto L_0x001e
        L_0x001d:
            r1 = move-exception
        L_0x001e:
            monitor-exit(r3)     // Catch:{ all -> 0x001d }
            throw r1
        L_0x0020:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.util.SubscriptionList.unsubscribe():void");
    }

    private static void unsubscribeFromAll(Collection<Subscription> subscriptions2) {
        if (subscriptions2 != null) {
            List<Throwable> es = null;
            for (Subscription s : subscriptions2) {
                try {
                    s.unsubscribe();
                } catch (Throwable e) {
                    if (es == null) {
                        es = new ArrayList<>();
                    }
                    es.add(e);
                }
            }
            Exceptions.throwIfAny(es);
        }
    }

    public void clear() {
        Throwable th;
        if (!this.unsubscribed) {
            synchronized (this) {
                try {
                    List<Subscription> list = this.subscriptions;
                    try {
                        this.subscriptions = null;
                        unsubscribeFromAll(list);
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
    }

    public boolean hasSubscriptions() {
        boolean z = false;
        if (this.unsubscribed) {
            return false;
        }
        synchronized (this) {
            if (!this.unsubscribed && this.subscriptions != null && !this.subscriptions.isEmpty()) {
                z = true;
            }
        }
        return z;
    }
}
