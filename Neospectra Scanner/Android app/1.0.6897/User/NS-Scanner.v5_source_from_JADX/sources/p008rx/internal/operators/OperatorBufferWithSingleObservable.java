package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.observers.SerializedSubscriber;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OperatorBufferWithSingleObservable */
public final class OperatorBufferWithSingleObservable<T, TClosing> implements Operator<List<T>, T> {
    final Func0<? extends Observable<? extends TClosing>> bufferClosingSelector;
    final int initialCapacity;

    /* renamed from: rx.internal.operators.OperatorBufferWithSingleObservable$BufferingSubscriber */
    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk;
        boolean done;

        public BufferingSubscriber(Subscriber<? super List<T>> child2) {
            this.child = child2;
            this.chunk = new ArrayList(OperatorBufferWithSingleObservable.this.initialCapacity);
        }

        public void onNext(T t) {
            synchronized (this) {
                if (!this.done) {
                    this.chunk.add(t);
                }
            }
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
        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            r3.child.onNext(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            monitor-enter(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            if (r3.done != false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0027, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0028, code lost:
            r3.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x002c, code lost:
            p008rx.exceptions.Exceptions.throwOrReport(r1, (p008rx.Observer<?>) r3.child);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0031, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit() {
            /*
                r3 = this;
                monitor-enter(r3)
                r0 = 0
                boolean r1 = r3.done     // Catch:{ all -> 0x0035 }
                if (r1 == 0) goto L_0x0008
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                return
            L_0x0008:
                java.util.List<T> r1 = r3.chunk     // Catch:{ all -> 0x0035 }
                r0 = r1
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x0035 }
                rx.internal.operators.OperatorBufferWithSingleObservable r2 = p008rx.internal.operators.OperatorBufferWithSingleObservable.this     // Catch:{ all -> 0x0035 }
                int r2 = r2.initialCapacity     // Catch:{ all -> 0x0035 }
                r1.<init>(r2)     // Catch:{ all -> 0x0035 }
                r3.chunk = r1     // Catch:{ all -> 0x0035 }
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                rx.Subscriber<? super java.util.List<T>> r1 = r3.child     // Catch:{ Throwable -> 0x001d }
                r1.onNext(r0)     // Catch:{ Throwable -> 0x001d }
                goto L_0x0031
            L_0x001d:
                r1 = move-exception
                r3.unsubscribe()
                monitor-enter(r3)
                boolean r2 = r3.done     // Catch:{ all -> 0x0032 }
                if (r2 == 0) goto L_0x0028
                monitor-exit(r3)     // Catch:{ all -> 0x0032 }
                return
            L_0x0028:
                r2 = 1
                r3.done = r2     // Catch:{ all -> 0x0032 }
                monitor-exit(r3)     // Catch:{ all -> 0x0032 }
                rx.Subscriber<? super java.util.List<T>> r2 = r3.child
                p008rx.exceptions.Exceptions.throwOrReport(r1, r2)
            L_0x0031:
                return
            L_0x0032:
                r2 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0032 }
                throw r2
            L_0x0035:
                r1 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x0035 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorBufferWithSingleObservable.BufferingSubscriber.emit():void");
        }
    }

    public OperatorBufferWithSingleObservable(Func0<? extends Observable<? extends TClosing>> bufferClosingSelector2, int initialCapacity2) {
        this.bufferClosingSelector = bufferClosingSelector2;
        this.initialCapacity = initialCapacity2;
    }

    public OperatorBufferWithSingleObservable(final Observable<? extends TClosing> bufferClosing, int initialCapacity2) {
        this.bufferClosingSelector = new Func0<Observable<? extends TClosing>>() {
            public Observable<? extends TClosing> call() {
                return bufferClosing;
            }
        };
        this.initialCapacity = initialCapacity2;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        try {
            Observable<? extends TClosing> closing = (Observable) this.bufferClosingSelector.call();
            final BufferingSubscriber s = new BufferingSubscriber<>(new SerializedSubscriber(child));
            Subscriber<TClosing> closingSubscriber = new Subscriber<TClosing>() {
                public void onNext(TClosing tclosing) {
                    s.emit();
                }

                public void onError(Throwable e) {
                    s.onError(e);
                }

                public void onCompleted() {
                    s.onCompleted();
                }
            };
            child.add(closingSubscriber);
            child.add(s);
            closing.unsafeSubscribe(closingSubscriber);
            return s;
        } catch (Throwable t) {
            Exceptions.throwOrReport(t, (Observer<?>) child);
            return Subscribers.empty();
        }
    }
}
