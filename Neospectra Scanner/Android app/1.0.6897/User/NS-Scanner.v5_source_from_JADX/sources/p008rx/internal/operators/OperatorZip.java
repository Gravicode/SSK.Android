package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.functions.Func2;
import p008rx.functions.Func3;
import p008rx.functions.Func4;
import p008rx.functions.Func5;
import p008rx.functions.Func6;
import p008rx.functions.Func7;
import p008rx.functions.Func8;
import p008rx.functions.Func9;
import p008rx.functions.FuncN;
import p008rx.functions.Functions;
import p008rx.internal.util.RxRingBuffer;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OperatorZip */
public final class OperatorZip<R> implements Operator<R, Observable<?>[]> {
    final FuncN<? extends R> zipFunction;

    /* renamed from: rx.internal.operators.OperatorZip$Zip */
    static final class Zip<R> extends AtomicLong {
        static final int THRESHOLD = ((int) (((double) RxRingBuffer.SIZE) * 0.7d));
        private static final long serialVersionUID = 5995274816189928317L;
        final Observer<? super R> child;
        private final CompositeSubscription childSubscription = new CompositeSubscription();
        int emitted;
        private AtomicLong requested;
        private volatile Object[] subscribers;
        private final FuncN<? extends R> zipFunction;

        /* renamed from: rx.internal.operators.OperatorZip$Zip$InnerSubscriber */
        final class InnerSubscriber extends Subscriber {
            final RxRingBuffer items = RxRingBuffer.getSpmcInstance();

            InnerSubscriber() {
            }

            public void onStart() {
                request((long) RxRingBuffer.SIZE);
            }

            public void requestMore(long n) {
                request(n);
            }

            public void onCompleted() {
                this.items.onCompleted();
                Zip.this.tick();
            }

            public void onError(Throwable e) {
                Zip.this.child.onError(e);
            }

            public void onNext(Object t) {
                try {
                    this.items.onNext(t);
                } catch (MissingBackpressureException e) {
                    onError(e);
                }
                Zip.this.tick();
            }
        }

        public Zip(Subscriber<? super R> child2, FuncN<? extends R> zipFunction2) {
            this.child = child2;
            this.zipFunction = zipFunction2;
            child2.add(this.childSubscription);
        }

        public void start(Observable[] os, AtomicLong requested2) {
            Object[] subscribers2 = new Object[os.length];
            for (int i = 0; i < os.length; i++) {
                InnerSubscriber io = new InnerSubscriber<>();
                subscribers2[i] = io;
                this.childSubscription.add(io);
            }
            this.requested = requested2;
            this.subscribers = subscribers2;
            for (int i2 = 0; i2 < os.length; i2++) {
                os[i2].unsafeSubscribe((InnerSubscriber) subscribers2[i2]);
            }
        }

        /* access modifiers changed from: 0000 */
        public void tick() {
            long j;
            Object[] subscribers2 = this.subscribers;
            if (subscribers2 != null) {
                long j2 = 0;
                if (getAndIncrement() == 0) {
                    int length = subscribers2.length;
                    Observer<? super R> child2 = this.child;
                    AtomicLong requested2 = this.requested;
                    while (true) {
                        Object[] vs = new Object[length];
                        boolean allHaveValues = true;
                        for (int i = 0; i < length; i++) {
                            RxRingBuffer buffer = ((InnerSubscriber) subscribers2[i]).items;
                            Object n = buffer.peek();
                            if (n == null) {
                                allHaveValues = false;
                            } else if (buffer.isCompleted(n)) {
                                child2.onCompleted();
                                this.childSubscription.unsubscribe();
                                return;
                            } else {
                                vs[i] = buffer.getValue(n);
                            }
                        }
                        if (requested2.get() <= j2 || !allHaveValues) {
                            j = 0;
                            if (decrementAndGet() <= 0) {
                                break;
                            }
                        } else {
                            try {
                                child2.onNext(this.zipFunction.call(vs));
                                requested2.decrementAndGet();
                                this.emitted++;
                                Object[] arr$ = subscribers2;
                                int len$ = arr$.length;
                                for (int i$ = 0; i$ < len$; i$++) {
                                    RxRingBuffer buffer2 = ((InnerSubscriber) arr$[i$]).items;
                                    buffer2.poll();
                                    if (buffer2.isCompleted(buffer2.peek())) {
                                        child2.onCompleted();
                                        this.childSubscription.unsubscribe();
                                        return;
                                    }
                                }
                                if (this.emitted > THRESHOLD) {
                                    Object[] arr$2 = subscribers2;
                                    int len$2 = arr$2.length;
                                    for (int i$2 = 0; i$2 < len$2; i$2++) {
                                        ((InnerSubscriber) arr$2[i$2]).requestMore((long) this.emitted);
                                    }
                                    this.emitted = 0;
                                }
                                j = 0;
                            } catch (Throwable th) {
                                Exceptions.throwOrReport(th, child2, (Object) vs);
                                return;
                            }
                        }
                        j2 = j;
                    }
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorZip$ZipProducer */
    static final class ZipProducer<R> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1216676403723546796L;
        final Zip<R> zipper;

        public ZipProducer(Zip<R> zipper2) {
            this.zipper = zipper2;
        }

        public void request(long n) {
            BackpressureUtils.getAndAddRequest(this, n);
            this.zipper.tick();
        }
    }

    /* renamed from: rx.internal.operators.OperatorZip$ZipSubscriber */
    final class ZipSubscriber extends Subscriber<Observable[]> {
        final Subscriber<? super R> child;
        final ZipProducer<R> producer;
        boolean started;
        final Zip<R> zipper;

        public ZipSubscriber(Subscriber<? super R> child2, Zip<R> zipper2, ZipProducer<R> producer2) {
            this.child = child2;
            this.zipper = zipper2;
            this.producer = producer2;
        }

        public void onCompleted() {
            if (!this.started) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(Observable[] observables) {
            if (observables == null || observables.length == 0) {
                this.child.onCompleted();
                return;
            }
            this.started = true;
            this.zipper.start(observables, this.producer);
        }
    }

    public OperatorZip(FuncN<? extends R> f) {
        this.zipFunction = f;
    }

    public OperatorZip(Func2 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func3 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func4 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func5 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func6 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func7 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func8 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func9 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public Subscriber<? super Observable[]> call(Subscriber<? super R> child) {
        Zip<R> zipper = new Zip<>(child, this.zipFunction);
        ZipProducer<R> producer = new ZipProducer<>(zipper);
        ZipSubscriber subscriber = new ZipSubscriber<>(child, zipper, producer);
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }
}
