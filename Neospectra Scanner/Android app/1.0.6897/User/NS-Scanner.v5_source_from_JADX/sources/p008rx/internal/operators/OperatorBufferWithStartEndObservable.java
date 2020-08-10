package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.observers.SerializedSubscriber;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OperatorBufferWithStartEndObservable */
public final class OperatorBufferWithStartEndObservable<T, TOpening, TClosing> implements Operator<List<T>, T> {
    final Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosing;
    final Observable<? extends TOpening> bufferOpening;

    /* renamed from: rx.internal.operators.OperatorBufferWithStartEndObservable$BufferingSubscriber */
    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks = new LinkedList();
        final CompositeSubscription closingSubscriptions = new CompositeSubscription();
        boolean done;

        public BufferingSubscriber(Subscriber<? super List<T>> child2) {
            this.child = child2;
            add(this.closingSubscriptions);
        }

        public void onNext(T t) {
            synchronized (this) {
                for (List<T> chunk : this.chunks) {
                    chunk.add(t);
                }
            }
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
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.onCompleted():void");
        }

        /* access modifiers changed from: 0000 */
        public void startBuffer(TOpening v) {
            final List<T> chunk = new ArrayList<>();
            synchronized (this) {
                if (!this.done) {
                    this.chunks.add(chunk);
                    try {
                        Observable<? extends TClosing> cobs = (Observable) OperatorBufferWithStartEndObservable.this.bufferClosing.call(v);
                        Subscriber<TClosing> closeSubscriber = new Subscriber<TClosing>() {
                            public void onNext(TClosing tclosing) {
                                BufferingSubscriber.this.closingSubscriptions.remove(this);
                                BufferingSubscriber.this.endBuffer(chunk);
                            }

                            public void onError(Throwable e) {
                                BufferingSubscriber.this.onError(e);
                            }

                            public void onCompleted() {
                                BufferingSubscriber.this.closingSubscriptions.remove(this);
                                BufferingSubscriber.this.endBuffer(chunk);
                            }
                        };
                        this.closingSubscriptions.add(closeSubscriber);
                        cobs.unsafeSubscribe(closeSubscriber);
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, (Observer<?>) this);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
            if (r0 == false) goto L_0x002a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
            r3.child.onNext(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002a, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void endBuffer(java.util.List<T> r4) {
            /*
                r3 = this;
                r0 = 0
                monitor-enter(r3)
                boolean r1 = r3.done     // Catch:{ all -> 0x002b }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                return
            L_0x0008:
                java.util.List<java.util.List<T>> r1 = r3.chunks     // Catch:{ all -> 0x002b }
                java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x002b }
            L_0x000e:
                boolean r2 = r1.hasNext()     // Catch:{ all -> 0x002b }
                if (r2 == 0) goto L_0x0022
                java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x002b }
                java.util.List r2 = (java.util.List) r2     // Catch:{ all -> 0x002b }
                if (r2 != r4) goto L_0x0021
                r0 = 1
                r1.remove()     // Catch:{ all -> 0x002b }
                goto L_0x0022
            L_0x0021:
                goto L_0x000e
            L_0x0022:
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                if (r0 == 0) goto L_0x002a
                rx.Subscriber<? super java.util.List<T>> r1 = r3.child
                r1.onNext(r4)
            L_0x002a:
                return
            L_0x002b:
                r1 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002b }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.endBuffer(java.util.List):void");
        }
    }

    public OperatorBufferWithStartEndObservable(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        this.bufferOpening = bufferOpenings;
        this.bufferClosing = bufferClosingSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        final BufferingSubscriber s = new BufferingSubscriber<>(new SerializedSubscriber(child));
        Subscriber<TOpening> openSubscriber = new Subscriber<TOpening>() {
            public void onNext(TOpening t) {
                s.startBuffer(t);
            }

            public void onError(Throwable e) {
                s.onError(e);
            }

            public void onCompleted() {
                s.onCompleted();
            }
        };
        child.add(openSubscriber);
        child.add(s);
        this.bufferOpening.unsafeSubscribe(openSubscriber);
        return s;
    }
}
