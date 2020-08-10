package p008rx.internal.operators;

import java.util.LinkedList;
import java.util.List;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.functions.Func1;
import p008rx.observers.SerializedObserver;
import p008rx.observers.SerializedSubscriber;
import p008rx.subjects.UnicastSubject;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OperatorWindowWithStartEndObservable */
public final class OperatorWindowWithStartEndObservable<T, U, V> implements Operator<Observable<T>, T> {
    final Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector;
    final Observable<? extends U> windowOpenings;

    /* renamed from: rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject */
    static final class SerializedSubject<T> {
        final Observer<T> consumer;
        final Observable<T> producer;

        public SerializedSubject(Observer<T> consumer2, Observable<T> producer2) {
            this.consumer = new SerializedObserver(consumer2);
            this.producer = producer2;
        }
    }

    /* renamed from: rx.internal.operators.OperatorWindowWithStartEndObservable$SourceSubscriber */
    final class SourceSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<SerializedSubject<T>> chunks = new LinkedList();
        final CompositeSubscription composite;
        boolean done;
        final Object guard = new Object();

        public SourceSubscriber(Subscriber<? super Observable<T>> child2, CompositeSubscription composite2) {
            this.child = new SerializedSubscriber(child2);
            this.composite = composite2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
            r0 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
            if (r0.hasNext() == false) goto L_0x0029;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
            ((p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0.next()).consumer.onNext(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0029, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r5) {
            /*
                r4 = this;
                java.lang.Object r0 = r4.guard
                monitor-enter(r0)
                r1 = 0
                boolean r2 = r4.done     // Catch:{ all -> 0x002a }
                if (r2 == 0) goto L_0x000a
                monitor-exit(r0)     // Catch:{ all -> 0x002a }
                return
            L_0x000a:
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x002a }
                java.util.List<rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r4.chunks     // Catch:{ all -> 0x002a }
                r2.<init>(r3)     // Catch:{ all -> 0x002a }
                r1 = r2
                monitor-exit(r0)     // Catch:{ all -> 0x002a }
                java.util.Iterator r0 = r1.iterator()
            L_0x0017:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L_0x0029
                java.lang.Object r2 = r0.next()
                rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r2 = (p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r2
                rx.Observer<T> r3 = r2.consumer
                r3.onNext(r5)
                goto L_0x0017
            L_0x0029:
                return
            L_0x002a:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x002a }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onNext(java.lang.Object):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            r0 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0028, code lost:
            if (r0.hasNext() == false) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
            ((p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0.next()).consumer.onError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
            r4.child.onError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x003b, code lost:
            r4.composite.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r5) {
            /*
                r4 = this;
                java.lang.Object r0 = r4.guard     // Catch:{ all -> 0x0045 }
                monitor-enter(r0)     // Catch:{ all -> 0x0045 }
                r1 = 0
                boolean r2 = r4.done     // Catch:{ all -> 0x0042 }
                if (r2 == 0) goto L_0x000f
                monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                rx.subscriptions.CompositeSubscription r0 = r4.composite
                r0.unsubscribe()
                return
            L_0x000f:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0042 }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0042 }
                java.util.List<rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r4.chunks     // Catch:{ all -> 0x0042 }
                r2.<init>(r3)     // Catch:{ all -> 0x0042 }
                r1 = r2
                java.util.List<rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r2 = r4.chunks     // Catch:{ all -> 0x0042 }
                r2.clear()     // Catch:{ all -> 0x0042 }
                monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                java.util.Iterator r0 = r1.iterator()     // Catch:{ all -> 0x0045 }
            L_0x0024:
                boolean r2 = r0.hasNext()     // Catch:{ all -> 0x0045 }
                if (r2 == 0) goto L_0x0036
                java.lang.Object r2 = r0.next()     // Catch:{ all -> 0x0045 }
                rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r2 = (p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r2     // Catch:{ all -> 0x0045 }
                rx.Observer<T> r3 = r2.consumer     // Catch:{ all -> 0x0045 }
                r3.onError(r5)     // Catch:{ all -> 0x0045 }
                goto L_0x0024
            L_0x0036:
                rx.Subscriber<? super rx.Observable<T>> r0 = r4.child     // Catch:{ all -> 0x0045 }
                r0.onError(r5)     // Catch:{ all -> 0x0045 }
                rx.subscriptions.CompositeSubscription r0 = r4.composite
                r0.unsubscribe()
                return
            L_0x0042:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                throw r2     // Catch:{ all -> 0x0045 }
            L_0x0045:
                r0 = move-exception
                rx.subscriptions.CompositeSubscription r1 = r4.composite
                r1.unsubscribe()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            r0 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0028, code lost:
            if (r0.hasNext() == false) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
            ((p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0.next()).consumer.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
            r4.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x003b, code lost:
            r4.composite.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r4 = this;
                java.lang.Object r0 = r4.guard     // Catch:{ all -> 0x0045 }
                monitor-enter(r0)     // Catch:{ all -> 0x0045 }
                r1 = 0
                boolean r2 = r4.done     // Catch:{ all -> 0x0042 }
                if (r2 == 0) goto L_0x000f
                monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                rx.subscriptions.CompositeSubscription r0 = r4.composite
                r0.unsubscribe()
                return
            L_0x000f:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0042 }
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0042 }
                java.util.List<rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r3 = r4.chunks     // Catch:{ all -> 0x0042 }
                r2.<init>(r3)     // Catch:{ all -> 0x0042 }
                r1 = r2
                java.util.List<rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r2 = r4.chunks     // Catch:{ all -> 0x0042 }
                r2.clear()     // Catch:{ all -> 0x0042 }
                monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                java.util.Iterator r0 = r1.iterator()     // Catch:{ all -> 0x0045 }
            L_0x0024:
                boolean r2 = r0.hasNext()     // Catch:{ all -> 0x0045 }
                if (r2 == 0) goto L_0x0036
                java.lang.Object r2 = r0.next()     // Catch:{ all -> 0x0045 }
                rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r2 = (p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r2     // Catch:{ all -> 0x0045 }
                rx.Observer<T> r3 = r2.consumer     // Catch:{ all -> 0x0045 }
                r3.onCompleted()     // Catch:{ all -> 0x0045 }
                goto L_0x0024
            L_0x0036:
                rx.Subscriber<? super rx.Observable<T>> r0 = r4.child     // Catch:{ all -> 0x0045 }
                r0.onCompleted()     // Catch:{ all -> 0x0045 }
                rx.subscriptions.CompositeSubscription r0 = r4.composite
                r0.unsubscribe()
                return
            L_0x0042:
                r2 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                throw r2     // Catch:{ all -> 0x0045 }
            L_0x0045:
                r0 = move-exception
                rx.subscriptions.CompositeSubscription r1 = r4.composite
                r1.unsubscribe()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: 0000 */
        public void beginWindow(U token) {
            final SerializedSubject<T> s = createSerializedSubject();
            synchronized (this.guard) {
                if (!this.done) {
                    this.chunks.add(s);
                    this.child.onNext(s.producer);
                    try {
                        Observable<? extends V> end = (Observable) OperatorWindowWithStartEndObservable.this.windowClosingSelector.call(token);
                        Subscriber<V> v = new Subscriber<V>() {
                            boolean once = true;

                            public void onNext(V v) {
                                onCompleted();
                            }

                            public void onError(Throwable e) {
                                SourceSubscriber.this.onError(e);
                            }

                            public void onCompleted() {
                                if (this.once) {
                                    this.once = false;
                                    SourceSubscriber.this.endWindow(s);
                                    SourceSubscriber.this.composite.remove(this);
                                }
                            }
                        };
                        this.composite.add(v);
                        end.unsafeSubscribe(v);
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
        public void endWindow(p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject<T> r5) {
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
                java.util.List<rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject<T>> r2 = r4.chunks     // Catch:{ all -> 0x002d }
                java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x002d }
            L_0x0010:
                boolean r3 = r2.hasNext()     // Catch:{ all -> 0x002d }
                if (r3 == 0) goto L_0x0024
                java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x002d }
                rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject r3 = (p008rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r3     // Catch:{ all -> 0x002d }
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
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.endWindow(rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject):void");
        }

        /* access modifiers changed from: 0000 */
        public SerializedSubject<T> createSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new SerializedSubject<>(bus, bus);
        }
    }

    public OperatorWindowWithStartEndObservable(Observable<? extends U> windowOpenings2, Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector2) {
        this.windowOpenings = windowOpenings2;
        this.windowClosingSelector = windowClosingSelector2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        CompositeSubscription composite = new CompositeSubscription();
        child.add(composite);
        final SourceSubscriber sub = new SourceSubscriber<>(child, composite);
        Subscriber<U> open = new Subscriber<U>() {
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(U t) {
                sub.beginWindow(t);
            }

            public void onError(Throwable e) {
                sub.onError(e);
            }

            public void onCompleted() {
                sub.onCompleted();
            }
        };
        composite.add(sub);
        composite.add(open);
        this.windowOpenings.unsafeSubscribe(open);
        return sub;
    }
}
