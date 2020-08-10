package p008rx.subscriptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.subscriptions.CompositeSubscription */
public final class CompositeSubscription implements Subscription {
    private Set<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public CompositeSubscription() {
    }

    public CompositeSubscription(Subscription... subscriptions2) {
        this.subscriptions = new HashSet(Arrays.asList(subscriptions2));
    }

    public boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(Subscription s) {
        if (!s.isUnsubscribed()) {
            if (!this.unsubscribed) {
                synchronized (this) {
                    if (!this.unsubscribed) {
                        if (this.subscriptions == null) {
                            this.subscriptions = new HashSet(4);
                        }
                        this.subscriptions.add(s);
                        return;
                    }
                }
            }
            s.unsubscribe();
        }
    }

    public void addAll(Subscription... subscriptions2) {
        int i$ = 0;
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed) {
                    if (this.subscriptions == null) {
                        this.subscriptions = new HashSet(subscriptions2.length);
                    }
                    Subscription[] arr$ = subscriptions2;
                    int len$ = arr$.length;
                    while (i$ < len$) {
                        Subscription s = arr$[i$];
                        if (!s.isUnsubscribed()) {
                            this.subscriptions.add(s);
                        }
                        i$++;
                    }
                    return;
                }
            }
        }
        Subscription[] arr$2 = subscriptions2;
        int len$2 = arr$2.length;
        while (i$ < len$2) {
            arr$2[i$].unsubscribe();
            i$++;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        if (r0 == false) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
        r3.unsubscribe();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(p008rx.Subscription r3) {
        /*
            r2 = this;
            boolean r0 = r2.unsubscribed
            if (r0 != 0) goto L_0x0022
            monitor-enter(r2)
            r0 = 0
            boolean r1 = r2.unsubscribed     // Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x001d
            java.util.Set<rx.Subscription> r1 = r2.subscriptions     // Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x000f
            goto L_0x001d
        L_0x000f:
            java.util.Set<rx.Subscription> r1 = r2.subscriptions     // Catch:{ all -> 0x001f }
            boolean r1 = r1.remove(r3)     // Catch:{ all -> 0x001f }
            r0 = r1
            monitor-exit(r2)     // Catch:{ all -> 0x001f }
            if (r0 == 0) goto L_0x0022
            r3.unsubscribe()
            goto L_0x0022
        L_0x001d:
            monitor-exit(r2)     // Catch:{ all -> 0x001f }
            return
        L_0x001f:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001f }
            throw r1
        L_0x0022:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.subscriptions.CompositeSubscription.remove(rx.Subscription):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0014, code lost:
        unsubscribeFromAll(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clear() {
        /*
            r3 = this;
            boolean r0 = r3.unsubscribed
            if (r0 != 0) goto L_0x0022
            monitor-enter(r3)
            r0 = 0
            boolean r1 = r3.unsubscribed     // Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x001d
            java.util.Set<rx.Subscription> r1 = r3.subscriptions     // Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x000f
            goto L_0x001d
        L_0x000f:
            java.util.Set<rx.Subscription> r1 = r3.subscriptions     // Catch:{ all -> 0x001f }
            r3.subscriptions = r0     // Catch:{ all -> 0x0018 }
            monitor-exit(r3)     // Catch:{ all -> 0x0018 }
            unsubscribeFromAll(r1)
            goto L_0x0022
        L_0x0018:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
            goto L_0x0020
        L_0x001d:
            monitor-exit(r3)     // Catch:{ all -> 0x001f }
            return
        L_0x001f:
            r1 = move-exception
        L_0x0020:
            monitor-exit(r3)     // Catch:{ all -> 0x001f }
            throw r1
        L_0x0022:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.subscriptions.CompositeSubscription.clear():void");
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
            java.util.Set<rx.Subscription> r1 = r3.subscriptions     // Catch:{ all -> 0x001d }
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
        throw new UnsupportedOperationException("Method not decompiled: p008rx.subscriptions.CompositeSubscription.unsubscribe():void");
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
