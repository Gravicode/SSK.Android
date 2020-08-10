package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Func0;
import p008rx.observables.ConnectableObservable;
import p008rx.subjects.Subject;

/* renamed from: rx.internal.operators.OperatorMulticast */
public final class OperatorMulticast<T, R> extends ConnectableObservable<R> {
    final AtomicReference<Subject<? super T, ? extends R>> connectedSubject;
    final Object guard;
    Subscription guardedSubscription;
    final Observable<? extends T> source;
    final Func0<? extends Subject<? super T, ? extends R>> subjectFactory;
    Subscriber<T> subscription;
    final List<Subscriber<? super R>> waitingForConnect;

    public OperatorMulticast(Observable<? extends T> source2, Func0<? extends Subject<? super T, ? extends R>> subjectFactory2) {
        this(new Object(), new AtomicReference(), new ArrayList(), source2, subjectFactory2);
    }

    private OperatorMulticast(final Object guard2, final AtomicReference<Subject<? super T, ? extends R>> connectedSubject2, final List<Subscriber<? super R>> waitingForConnect2, Observable<? extends T> source2, Func0<? extends Subject<? super T, ? extends R>> subjectFactory2) {
        super(new OnSubscribe<R>() {
            public void call(Subscriber<? super R> subscriber) {
                synchronized (guard2) {
                    if (connectedSubject2.get() == null) {
                        waitingForConnect2.add(subscriber);
                    } else {
                        ((Subject) connectedSubject2.get()).unsafeSubscribe(subscriber);
                    }
                }
            }
        });
        this.guard = guard2;
        this.connectedSubject = connectedSubject2;
        this.waitingForConnect = waitingForConnect2;
        this.source = source2;
        this.subjectFactory = subjectFactory2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x005b, code lost:
        r7.call(r6.guardedSubscription);
        r1 = r6.guard;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0062, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r0 = r6.subscription;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0066, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0067, code lost:
        if (r0 == null) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0069, code lost:
        r6.source.subscribe(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p008rx.functions.Action1<? super p008rx.Subscription> r7) {
        /*
            r6 = this;
            java.lang.Object r0 = r6.guard
            monitor-enter(r0)
            rx.Subscriber<T> r1 = r6.subscription     // Catch:{ all -> 0x0072 }
            if (r1 == 0) goto L_0x000e
            rx.Subscription r1 = r6.guardedSubscription     // Catch:{ all -> 0x0072 }
            r7.call(r1)     // Catch:{ all -> 0x0072 }
            monitor-exit(r0)     // Catch:{ all -> 0x0072 }
            return
        L_0x000e:
            rx.functions.Func0<? extends rx.subjects.Subject<? super T, ? extends R>> r1 = r6.subjectFactory     // Catch:{ all -> 0x0072 }
            java.lang.Object r1 = r1.call()     // Catch:{ all -> 0x0072 }
            rx.subjects.Subject r1 = (p008rx.subjects.Subject) r1     // Catch:{ all -> 0x0072 }
            rx.Subscriber r2 = p008rx.observers.Subscribers.from(r1)     // Catch:{ all -> 0x0072 }
            r6.subscription = r2     // Catch:{ all -> 0x0072 }
            java.util.concurrent.atomic.AtomicReference r2 = new java.util.concurrent.atomic.AtomicReference     // Catch:{ all -> 0x0072 }
            r2.<init>()     // Catch:{ all -> 0x0072 }
            rx.internal.operators.OperatorMulticast$2 r3 = new rx.internal.operators.OperatorMulticast$2     // Catch:{ all -> 0x0072 }
            r3.<init>(r2)     // Catch:{ all -> 0x0072 }
            rx.Subscription r3 = p008rx.subscriptions.Subscriptions.create(r3)     // Catch:{ all -> 0x0072 }
            r2.set(r3)     // Catch:{ all -> 0x0072 }
            java.lang.Object r3 = r2.get()     // Catch:{ all -> 0x0072 }
            rx.Subscription r3 = (p008rx.Subscription) r3     // Catch:{ all -> 0x0072 }
            r6.guardedSubscription = r3     // Catch:{ all -> 0x0072 }
            java.util.List<rx.Subscriber<? super R>> r3 = r6.waitingForConnect     // Catch:{ all -> 0x0072 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0072 }
        L_0x003b:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0072 }
            if (r4 == 0) goto L_0x0050
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0072 }
            rx.Subscriber r4 = (p008rx.Subscriber) r4     // Catch:{ all -> 0x0072 }
            rx.internal.operators.OperatorMulticast$3 r5 = new rx.internal.operators.OperatorMulticast$3     // Catch:{ all -> 0x0072 }
            r5.<init>(r4, r4)     // Catch:{ all -> 0x0072 }
            r1.unsafeSubscribe(r5)     // Catch:{ all -> 0x0072 }
            goto L_0x003b
        L_0x0050:
            java.util.List<rx.Subscriber<? super R>> r3 = r6.waitingForConnect     // Catch:{ all -> 0x0072 }
            r3.clear()     // Catch:{ all -> 0x0072 }
            java.util.concurrent.atomic.AtomicReference<rx.subjects.Subject<? super T, ? extends R>> r3 = r6.connectedSubject     // Catch:{ all -> 0x0072 }
            r3.set(r1)     // Catch:{ all -> 0x0072 }
            monitor-exit(r0)     // Catch:{ all -> 0x0072 }
            rx.Subscription r1 = r6.guardedSubscription
            r7.call(r1)
            java.lang.Object r1 = r6.guard
            monitor-enter(r1)
            rx.Subscriber<T> r2 = r6.subscription     // Catch:{ all -> 0x006f }
            r0 = r2
            monitor-exit(r1)     // Catch:{ all -> 0x006f }
            if (r0 == 0) goto L_0x006e
            rx.Observable<? extends T> r1 = r6.source
            r1.subscribe(r0)
        L_0x006e:
            return
        L_0x006f:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x006f }
            throw r2
        L_0x0072:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0072 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorMulticast.connect(rx.functions.Action1):void");
    }
}
