package p008rx.internal.operators;

import java.util.HashMap;
import java.util.Map;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.observers.SerializedSubscriber;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OnSubscribeJoin */
public final class OnSubscribeJoin<TLeft, TRight, TLeftDuration, TRightDuration, R> implements OnSubscribe<R> {
    final Observable<TLeft> left;
    final Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector;
    final Func2<TLeft, TRight, R> resultSelector;
    final Observable<TRight> right;
    final Func1<TRight, Observable<TRightDuration>> rightDurationSelector;

    /* renamed from: rx.internal.operators.OnSubscribeJoin$ResultSink */
    final class ResultSink extends HashMap<Integer, TLeft> {
        private static final long serialVersionUID = 3491669543549085380L;
        final CompositeSubscription group = new CompositeSubscription();
        boolean leftDone;
        int leftId;
        boolean rightDone;
        int rightId;
        final Map<Integer, TRight> rightMap = new HashMap();
        final Subscriber<? super R> subscriber;

        /* renamed from: rx.internal.operators.OnSubscribeJoin$ResultSink$LeftSubscriber */
        final class LeftSubscriber extends Subscriber<TLeft> {

            /* renamed from: rx.internal.operators.OnSubscribeJoin$ResultSink$LeftSubscriber$LeftDurationSubscriber */
            final class LeftDurationSubscriber extends Subscriber<TLeftDuration> {

                /* renamed from: id */
                final int f905id;
                boolean once = true;

                public LeftDurationSubscriber(int id) {
                    this.f905id = id;
                }

                public void onNext(TLeftDuration tleftduration) {
                    onCompleted();
                }

                public void onError(Throwable e) {
                    LeftSubscriber.this.onError(e);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        LeftSubscriber.this.expire(this.f905id, this);
                    }
                }
            }

            LeftSubscriber() {
            }

            /* access modifiers changed from: protected */
            public void expire(int id, Subscription resource) {
                boolean complete = false;
                synchronized (ResultSink.this) {
                    if (ResultSink.this.leftMap().remove(Integer.valueOf(id)) != null && ResultSink.this.leftMap().isEmpty() && ResultSink.this.leftDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(resource);
            }

            /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
                r0 = (p008rx.Observable) r10.this$1.this$0.leftDurationSelector.call(r11);
                r3 = new p008rx.internal.operators.OnSubscribeJoin.ResultSink.LeftSubscriber.LeftDurationSubscriber<>(r10, r2);
                r10.this$1.group.add(r3);
                r0.unsafeSubscribe(r3);
                r4 = new java.util.ArrayList<>();
                r5 = r10.this$1;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
                monitor-enter(r5);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
                r6 = r10.this$1.rightMap.entrySet().iterator();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x0054, code lost:
                if (r6.hasNext() == false) goto L_0x0070;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:18:0x0056, code lost:
                r7 = (java.util.Map.Entry) r6.next();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:0x0066, code lost:
                if (((java.lang.Integer) r7.getKey()).intValue() >= r1) goto L_0x006f;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x0068, code lost:
                r4.add(r7.getValue());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:0x0070, code lost:
                monitor-exit(r5);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
                r5 = r4.iterator();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:0x0079, code lost:
                if (r5.hasNext() == false) goto L_0x0099;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:27:0x007b, code lost:
                r10.this$1.subscriber.onNext(r10.this$1.this$0.resultSelector.call(r11, r5.next()));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:33:0x0095, code lost:
                r3 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x0096, code lost:
                p008rx.exceptions.Exceptions.throwOrReport(r3, (p008rx.Observer<?>) r10);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onNext(TLeft r11) {
                /*
                    r10 = this;
                    rx.internal.operators.OnSubscribeJoin$ResultSink r0 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this
                    monitor-enter(r0)
                    r1 = 0
                    rx.internal.operators.OnSubscribeJoin$ResultSink r2 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x00a2 }
                    int r3 = r2.leftId     // Catch:{ all -> 0x00a2 }
                    int r4 = r3 + 1
                    r2.leftId = r4     // Catch:{ all -> 0x00a2 }
                    r2 = r3
                    rx.internal.operators.OnSubscribeJoin$ResultSink r3 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x009f }
                    java.util.HashMap r3 = r3.leftMap()     // Catch:{ all -> 0x009f }
                    java.lang.Integer r4 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x009f }
                    r3.put(r4, r11)     // Catch:{ all -> 0x009f }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r3 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x009f }
                    int r3 = r3.rightId     // Catch:{ all -> 0x009f }
                    r1 = r3
                    monitor-exit(r0)     // Catch:{ all -> 0x009a }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r3 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin r3 = p008rx.internal.operators.OnSubscribeJoin.this     // Catch:{ Throwable -> 0x0095 }
                    rx.functions.Func1<TLeft, rx.Observable<TLeftDuration>> r3 = r3.leftDurationSelector     // Catch:{ Throwable -> 0x0095 }
                    java.lang.Object r3 = r3.call(r11)     // Catch:{ Throwable -> 0x0095 }
                    rx.Observable r3 = (p008rx.Observable) r3     // Catch:{ Throwable -> 0x0095 }
                    r0 = r3
                    rx.internal.operators.OnSubscribeJoin$ResultSink$LeftSubscriber$LeftDurationSubscriber r3 = new rx.internal.operators.OnSubscribeJoin$ResultSink$LeftSubscriber$LeftDurationSubscriber     // Catch:{ Throwable -> 0x0095 }
                    r3.<init>(r2)     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r4 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x0095 }
                    rx.subscriptions.CompositeSubscription r4 = r4.group     // Catch:{ Throwable -> 0x0095 }
                    r4.add(r3)     // Catch:{ Throwable -> 0x0095 }
                    r0.unsafeSubscribe(r3)     // Catch:{ Throwable -> 0x0095 }
                    java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0095 }
                    r4.<init>()     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r5 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x0095 }
                    monitor-enter(r5)     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r6 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x0092 }
                    java.util.Map<java.lang.Integer, TRight> r6 = r6.rightMap     // Catch:{ all -> 0x0092 }
                    java.util.Set r6 = r6.entrySet()     // Catch:{ all -> 0x0092 }
                    java.util.Iterator r6 = r6.iterator()     // Catch:{ all -> 0x0092 }
                L_0x0050:
                    boolean r7 = r6.hasNext()     // Catch:{ all -> 0x0092 }
                    if (r7 == 0) goto L_0x0070
                    java.lang.Object r7 = r6.next()     // Catch:{ all -> 0x0092 }
                    java.util.Map$Entry r7 = (java.util.Map.Entry) r7     // Catch:{ all -> 0x0092 }
                    java.lang.Object r8 = r7.getKey()     // Catch:{ all -> 0x0092 }
                    java.lang.Integer r8 = (java.lang.Integer) r8     // Catch:{ all -> 0x0092 }
                    int r8 = r8.intValue()     // Catch:{ all -> 0x0092 }
                    if (r8 >= r1) goto L_0x006f
                    java.lang.Object r8 = r7.getValue()     // Catch:{ all -> 0x0092 }
                    r4.add(r8)     // Catch:{ all -> 0x0092 }
                L_0x006f:
                    goto L_0x0050
                L_0x0070:
                    monitor-exit(r5)     // Catch:{ all -> 0x0092 }
                    java.util.Iterator r5 = r4.iterator()     // Catch:{ Throwable -> 0x0095 }
                L_0x0075:
                    boolean r6 = r5.hasNext()     // Catch:{ Throwable -> 0x0095 }
                    if (r6 == 0) goto L_0x0091
                    java.lang.Object r6 = r5.next()     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r7 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin r7 = p008rx.internal.operators.OnSubscribeJoin.this     // Catch:{ Throwable -> 0x0095 }
                    rx.functions.Func2<TLeft, TRight, R> r7 = r7.resultSelector     // Catch:{ Throwable -> 0x0095 }
                    java.lang.Object r7 = r7.call(r11, r6)     // Catch:{ Throwable -> 0x0095 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r8 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x0095 }
                    rx.Subscriber<? super R> r8 = r8.subscriber     // Catch:{ Throwable -> 0x0095 }
                    r8.onNext(r7)     // Catch:{ Throwable -> 0x0095 }
                    goto L_0x0075
                L_0x0091:
                    goto L_0x0099
                L_0x0092:
                    r6 = move-exception
                    monitor-exit(r5)     // Catch:{ all -> 0x0092 }
                    throw r6     // Catch:{ Throwable -> 0x0095 }
                L_0x0095:
                    r3 = move-exception
                    p008rx.exceptions.Exceptions.throwOrReport(r3, r10)
                L_0x0099:
                    return
                L_0x009a:
                    r3 = move-exception
                    r9 = r2
                    r2 = r1
                    r1 = r9
                    goto L_0x00a4
                L_0x009f:
                    r3 = move-exception
                    r1 = r2
                    goto L_0x00a3
                L_0x00a2:
                    r3 = move-exception
                L_0x00a3:
                    r2 = 0
                L_0x00a4:
                    monitor-exit(r0)     // Catch:{ all -> 0x00a6 }
                    throw r3
                L_0x00a6:
                    r3 = move-exception
                    goto L_0x00a4
                */
                throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OnSubscribeJoin.ResultSink.LeftSubscriber.onNext(java.lang.Object):void");
            }

            public void onError(Throwable e) {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean complete = false;
                synchronized (ResultSink.this) {
                    ResultSink.this.leftDone = true;
                    if (ResultSink.this.rightDone || ResultSink.this.leftMap().isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }
        }

        /* renamed from: rx.internal.operators.OnSubscribeJoin$ResultSink$RightSubscriber */
        final class RightSubscriber extends Subscriber<TRight> {

            /* renamed from: rx.internal.operators.OnSubscribeJoin$ResultSink$RightSubscriber$RightDurationSubscriber */
            final class RightDurationSubscriber extends Subscriber<TRightDuration> {

                /* renamed from: id */
                final int f906id;
                boolean once = true;

                public RightDurationSubscriber(int id) {
                    this.f906id = id;
                }

                public void onNext(TRightDuration trightduration) {
                    onCompleted();
                }

                public void onError(Throwable e) {
                    RightSubscriber.this.onError(e);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        RightSubscriber.this.expire(this.f906id, this);
                    }
                }
            }

            RightSubscriber() {
            }

            /* access modifiers changed from: 0000 */
            public void expire(int id, Subscription resource) {
                boolean complete = false;
                synchronized (ResultSink.this) {
                    if (ResultSink.this.rightMap.remove(Integer.valueOf(id)) != null && ResultSink.this.rightMap.isEmpty() && ResultSink.this.rightDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(resource);
            }

            /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
                r11.this$1.group.add(new p008rx.subscriptions.SerialSubscription());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
                r3 = (p008rx.Observable) r11.this$1.this$0.rightDurationSelector.call(r12);
                r4 = new p008rx.internal.operators.OnSubscribeJoin.ResultSink.RightSubscriber.RightDurationSubscriber<>(r11, r2);
                r11.this$1.group.add(r4);
                r3.unsafeSubscribe(r4);
                r5 = new java.util.ArrayList<>();
                r6 = r11.this$1;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:14:0x004e, code lost:
                monitor-enter(r6);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
                r7 = r11.this$1.leftMap().entrySet().iterator();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:18:0x0061, code lost:
                if (r7.hasNext() == false) goto L_0x007d;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:0x0063, code lost:
                r8 = (java.util.Map.Entry) r7.next();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x0073, code lost:
                if (((java.lang.Integer) r8.getKey()).intValue() >= r1) goto L_0x007c;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:21:0x0075, code lost:
                r5.add(r8.getValue());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x007d, code lost:
                monitor-exit(r6);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
                r6 = r5.iterator();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:27:0x0086, code lost:
                if (r6.hasNext() == false) goto L_0x00a6;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:28:0x0088, code lost:
                r11.this$1.subscriber.onNext(r11.this$1.this$0.resultSelector.call(r6.next(), r12));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a2, code lost:
                r4 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a3, code lost:
                p008rx.exceptions.Exceptions.throwOrReport(r4, (p008rx.Observer<?>) r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onNext(TRight r12) {
                /*
                    r11 = this;
                    rx.internal.operators.OnSubscribeJoin$ResultSink r0 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this
                    monitor-enter(r0)
                    r1 = 0
                    rx.internal.operators.OnSubscribeJoin$ResultSink r2 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x00af }
                    int r3 = r2.rightId     // Catch:{ all -> 0x00af }
                    int r4 = r3 + 1
                    r2.rightId = r4     // Catch:{ all -> 0x00af }
                    r2 = r3
                    rx.internal.operators.OnSubscribeJoin$ResultSink r3 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x00ac }
                    java.util.Map<java.lang.Integer, TRight> r3 = r3.rightMap     // Catch:{ all -> 0x00ac }
                    java.lang.Integer r4 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x00ac }
                    r3.put(r4, r12)     // Catch:{ all -> 0x00ac }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r3 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x00ac }
                    int r3 = r3.leftId     // Catch:{ all -> 0x00ac }
                    r1 = r3
                    monitor-exit(r0)     // Catch:{ all -> 0x00a7 }
                    rx.subscriptions.SerialSubscription r0 = new rx.subscriptions.SerialSubscription
                    r0.<init>()
                    rx.internal.operators.OnSubscribeJoin$ResultSink r3 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this
                    rx.subscriptions.CompositeSubscription r3 = r3.group
                    r3.add(r0)
                    r3 = 0
                    rx.internal.operators.OnSubscribeJoin$ResultSink r4 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin r4 = p008rx.internal.operators.OnSubscribeJoin.this     // Catch:{ Throwable -> 0x00a2 }
                    rx.functions.Func1<TRight, rx.Observable<TRightDuration>> r4 = r4.rightDurationSelector     // Catch:{ Throwable -> 0x00a2 }
                    java.lang.Object r4 = r4.call(r12)     // Catch:{ Throwable -> 0x00a2 }
                    rx.Observable r4 = (p008rx.Observable) r4     // Catch:{ Throwable -> 0x00a2 }
                    r3 = r4
                    rx.internal.operators.OnSubscribeJoin$ResultSink$RightSubscriber$RightDurationSubscriber r4 = new rx.internal.operators.OnSubscribeJoin$ResultSink$RightSubscriber$RightDurationSubscriber     // Catch:{ Throwable -> 0x00a2 }
                    r4.<init>(r2)     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r5 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x00a2 }
                    rx.subscriptions.CompositeSubscription r5 = r5.group     // Catch:{ Throwable -> 0x00a2 }
                    r5.add(r4)     // Catch:{ Throwable -> 0x00a2 }
                    r3.unsafeSubscribe(r4)     // Catch:{ Throwable -> 0x00a2 }
                    java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ Throwable -> 0x00a2 }
                    r5.<init>()     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r6 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x00a2 }
                    monitor-enter(r6)     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r7 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ all -> 0x009f }
                    java.util.HashMap r7 = r7.leftMap()     // Catch:{ all -> 0x009f }
                    java.util.Set r7 = r7.entrySet()     // Catch:{ all -> 0x009f }
                    java.util.Iterator r7 = r7.iterator()     // Catch:{ all -> 0x009f }
                L_0x005d:
                    boolean r8 = r7.hasNext()     // Catch:{ all -> 0x009f }
                    if (r8 == 0) goto L_0x007d
                    java.lang.Object r8 = r7.next()     // Catch:{ all -> 0x009f }
                    java.util.Map$Entry r8 = (java.util.Map.Entry) r8     // Catch:{ all -> 0x009f }
                    java.lang.Object r9 = r8.getKey()     // Catch:{ all -> 0x009f }
                    java.lang.Integer r9 = (java.lang.Integer) r9     // Catch:{ all -> 0x009f }
                    int r9 = r9.intValue()     // Catch:{ all -> 0x009f }
                    if (r9 >= r1) goto L_0x007c
                    java.lang.Object r9 = r8.getValue()     // Catch:{ all -> 0x009f }
                    r5.add(r9)     // Catch:{ all -> 0x009f }
                L_0x007c:
                    goto L_0x005d
                L_0x007d:
                    monitor-exit(r6)     // Catch:{ all -> 0x009f }
                    java.util.Iterator r6 = r5.iterator()     // Catch:{ Throwable -> 0x00a2 }
                L_0x0082:
                    boolean r7 = r6.hasNext()     // Catch:{ Throwable -> 0x00a2 }
                    if (r7 == 0) goto L_0x009e
                    java.lang.Object r7 = r6.next()     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r8 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin r8 = p008rx.internal.operators.OnSubscribeJoin.this     // Catch:{ Throwable -> 0x00a2 }
                    rx.functions.Func2<TLeft, TRight, R> r8 = r8.resultSelector     // Catch:{ Throwable -> 0x00a2 }
                    java.lang.Object r8 = r8.call(r7, r12)     // Catch:{ Throwable -> 0x00a2 }
                    rx.internal.operators.OnSubscribeJoin$ResultSink r9 = p008rx.internal.operators.OnSubscribeJoin.ResultSink.this     // Catch:{ Throwable -> 0x00a2 }
                    rx.Subscriber<? super R> r9 = r9.subscriber     // Catch:{ Throwable -> 0x00a2 }
                    r9.onNext(r8)     // Catch:{ Throwable -> 0x00a2 }
                    goto L_0x0082
                L_0x009e:
                    goto L_0x00a6
                L_0x009f:
                    r7 = move-exception
                    monitor-exit(r6)     // Catch:{ all -> 0x009f }
                    throw r7     // Catch:{ Throwable -> 0x00a2 }
                L_0x00a2:
                    r4 = move-exception
                    p008rx.exceptions.Exceptions.throwOrReport(r4, r11)
                L_0x00a6:
                    return
                L_0x00a7:
                    r3 = move-exception
                    r10 = r2
                    r2 = r1
                    r1 = r10
                    goto L_0x00b1
                L_0x00ac:
                    r3 = move-exception
                    r1 = r2
                    goto L_0x00b0
                L_0x00af:
                    r3 = move-exception
                L_0x00b0:
                    r2 = 0
                L_0x00b1:
                    monitor-exit(r0)     // Catch:{ all -> 0x00b3 }
                    throw r3
                L_0x00b3:
                    r3 = move-exception
                    goto L_0x00b1
                */
                throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OnSubscribeJoin.ResultSink.RightSubscriber.onNext(java.lang.Object):void");
            }

            public void onError(Throwable e) {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean complete = false;
                synchronized (ResultSink.this) {
                    ResultSink.this.rightDone = true;
                    if (ResultSink.this.leftDone || ResultSink.this.rightMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }
        }

        public ResultSink(Subscriber<? super R> subscriber2) {
            this.subscriber = subscriber2;
        }

        /* access modifiers changed from: 0000 */
        public HashMap<Integer, TLeft> leftMap() {
            return this;
        }

        public void run() {
            this.subscriber.add(this.group);
            Subscriber<TLeft> s1 = new LeftSubscriber<>();
            Subscriber<TRight> s2 = new RightSubscriber<>();
            this.group.add(s1);
            this.group.add(s2);
            OnSubscribeJoin.this.left.unsafeSubscribe(s1);
            OnSubscribeJoin.this.right.unsafeSubscribe(s2);
        }
    }

    public OnSubscribeJoin(Observable<TLeft> left2, Observable<TRight> right2, Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector2, Func1<TRight, Observable<TRightDuration>> rightDurationSelector2, Func2<TLeft, TRight, R> resultSelector2) {
        this.left = left2;
        this.right = right2;
        this.leftDurationSelector = leftDurationSelector2;
        this.rightDurationSelector = rightDurationSelector2;
        this.resultSelector = resultSelector2;
    }

    public void call(Subscriber<? super R> t1) {
        new ResultSink<>(new SerializedSubscriber(t1)).run();
    }
}
