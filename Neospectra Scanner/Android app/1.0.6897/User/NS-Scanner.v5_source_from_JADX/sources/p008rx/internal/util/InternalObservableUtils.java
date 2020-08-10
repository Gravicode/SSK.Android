package p008rx.internal.util;

import java.util.List;
import java.util.concurrent.TimeUnit;
import p008rx.Notification;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.exceptions.OnErrorNotImplementedException;
import p008rx.functions.Action1;
import p008rx.functions.Action2;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.internal.operators.OperatorAny;
import p008rx.observables.ConnectableObservable;

/* renamed from: rx.internal.util.InternalObservableUtils */
public enum InternalObservableUtils {
    ;
    
    public static final PlusOneFunc2 COUNTER = null;
    static final NotificationErrorExtractor ERROR_EXTRACTOR = null;
    public static final Action1<Throwable> ERROR_NOT_IMPLEMENTED = null;
    public static final Operator<Boolean, Object> IS_EMPTY = null;
    public static final PlusOneLongFunc2 LONG_COUNTER = null;
    public static final ObjectEqualsFunc2 OBJECT_EQUALS = null;
    static final ReturnsVoidFunc1 RETURNS_VOID = null;
    public static final ToArrayFunc1 TO_ARRAY = null;

    /* renamed from: rx.internal.util.InternalObservableUtils$CollectorCaller */
    static final class CollectorCaller<T, R> implements Func2<R, T, R> {
        final Action2<R, ? super T> collector;

        public CollectorCaller(Action2<R, ? super T> collector2) {
            this.collector = collector2;
        }

        public R call(R state, T value) {
            this.collector.call(state, value);
            return state;
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$EqualsWithFunc1 */
    static final class EqualsWithFunc1 implements Func1<Object, Boolean> {
        final Object other;

        public EqualsWithFunc1(Object other2) {
            this.other = other2;
        }

        public Boolean call(Object t) {
            return Boolean.valueOf(t == this.other || (t != null && t.equals(this.other)));
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ErrorNotImplementedAction */
    static final class ErrorNotImplementedAction implements Action1<Throwable> {
        ErrorNotImplementedAction() {
        }

        public void call(Throwable t) {
            throw new OnErrorNotImplementedException(t);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$IsInstanceOfFunc1 */
    static final class IsInstanceOfFunc1 implements Func1<Object, Boolean> {
        final Class<?> clazz;

        public IsInstanceOfFunc1(Class<?> other) {
            this.clazz = other;
        }

        public Boolean call(Object t) {
            return Boolean.valueOf(this.clazz.isInstance(t));
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$NotificationErrorExtractor */
    static final class NotificationErrorExtractor implements Func1<Notification<?>, Throwable> {
        NotificationErrorExtractor() {
        }

        public Throwable call(Notification<?> t) {
            return t.getThrowable();
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ObjectEqualsFunc2 */
    static final class ObjectEqualsFunc2 implements Func2<Object, Object, Boolean> {
        ObjectEqualsFunc2() {
        }

        public Boolean call(Object first, Object second) {
            return Boolean.valueOf(first == second || (first != null && first.equals(second)));
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$PlusOneFunc2 */
    static final class PlusOneFunc2 implements Func2<Integer, Object, Integer> {
        PlusOneFunc2() {
        }

        public Integer call(Integer count, Object o) {
            return Integer.valueOf(count.intValue() + 1);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$PlusOneLongFunc2 */
    static final class PlusOneLongFunc2 implements Func2<Long, Object, Long> {
        PlusOneLongFunc2() {
        }

        public Long call(Long count, Object o) {
            return Long.valueOf(count.longValue() + 1);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$RepeatNotificationDematerializer */
    static final class RepeatNotificationDematerializer implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        final Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler;

        public RepeatNotificationDematerializer(Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler2) {
            this.notificationHandler = notificationHandler2;
        }

        public Observable<?> call(Observable<? extends Notification<?>> notifications) {
            return (Observable) this.notificationHandler.call(notifications.map(InternalObservableUtils.RETURNS_VOID));
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ReplaySupplierBuffer */
    static final class ReplaySupplierBuffer<T> implements Func0<ConnectableObservable<T>> {
        private final int bufferSize;
        private final Observable<T> source;

        ReplaySupplierBuffer(Observable<T> source2, int bufferSize2) {
            this.source = source2;
            this.bufferSize = bufferSize2;
        }

        public ConnectableObservable<T> call() {
            return this.source.replay(this.bufferSize);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ReplaySupplierBufferTime */
    static final class ReplaySupplierBufferTime<T> implements Func0<ConnectableObservable<T>> {
        private final Scheduler scheduler;
        private final Observable<T> source;
        private final long time;
        private final TimeUnit unit;

        ReplaySupplierBufferTime(Observable<T> source2, long time2, TimeUnit unit2, Scheduler scheduler2) {
            this.unit = unit2;
            this.source = source2;
            this.time = time2;
            this.scheduler = scheduler2;
        }

        public ConnectableObservable<T> call() {
            return this.source.replay(this.time, this.unit, this.scheduler);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ReplaySupplierNoParams */
    static final class ReplaySupplierNoParams<T> implements Func0<ConnectableObservable<T>> {
        private final Observable<T> source;

        ReplaySupplierNoParams(Observable<T> source2) {
            this.source = source2;
        }

        public ConnectableObservable<T> call() {
            return this.source.replay();
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ReplaySupplierTime */
    static final class ReplaySupplierTime<T> implements Func0<ConnectableObservable<T>> {
        private final int bufferSize;
        private final Scheduler scheduler;
        private final Observable<T> source;
        private final long time;
        private final TimeUnit unit;

        ReplaySupplierTime(Observable<T> source2, int bufferSize2, long time2, TimeUnit unit2, Scheduler scheduler2) {
            this.time = time2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.bufferSize = bufferSize2;
            this.source = source2;
        }

        public ConnectableObservable<T> call() {
            return this.source.replay(this.bufferSize, this.time, this.unit, this.scheduler);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$RetryNotificationDematerializer */
    static final class RetryNotificationDematerializer implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        final Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler;

        public RetryNotificationDematerializer(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler2) {
            this.notificationHandler = notificationHandler2;
        }

        public Observable<?> call(Observable<? extends Notification<?>> notifications) {
            return (Observable) this.notificationHandler.call(notifications.map(InternalObservableUtils.ERROR_EXTRACTOR));
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ReturnsVoidFunc1 */
    static final class ReturnsVoidFunc1 implements Func1<Object, Void> {
        ReturnsVoidFunc1() {
        }

        public Void call(Object t) {
            return null;
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$SelectorAndObserveOn */
    static final class SelectorAndObserveOn<T, R> implements Func1<Observable<T>, Observable<R>> {
        final Scheduler scheduler;
        final Func1<? super Observable<T>, ? extends Observable<R>> selector;

        public SelectorAndObserveOn(Func1<? super Observable<T>, ? extends Observable<R>> selector2, Scheduler scheduler2) {
            this.selector = selector2;
            this.scheduler = scheduler2;
        }

        public Observable<R> call(Observable<T> t) {
            return ((Observable) this.selector.call(t)).observeOn(this.scheduler);
        }
    }

    /* renamed from: rx.internal.util.InternalObservableUtils$ToArrayFunc1 */
    static final class ToArrayFunc1 implements Func1<List<? extends Observable<?>>, Observable<?>[]> {
        ToArrayFunc1() {
        }

        public Observable<?>[] call(List<? extends Observable<?>> o) {
            return (Observable[]) o.toArray(new Observable[o.size()]);
        }
    }

    static {
        LONG_COUNTER = new PlusOneLongFunc2();
        OBJECT_EQUALS = new ObjectEqualsFunc2();
        TO_ARRAY = new ToArrayFunc1();
        RETURNS_VOID = new ReturnsVoidFunc1();
        COUNTER = new PlusOneFunc2();
        ERROR_EXTRACTOR = new NotificationErrorExtractor();
        ERROR_NOT_IMPLEMENTED = new ErrorNotImplementedAction();
        IS_EMPTY = new OperatorAny(UtilityFunctions.alwaysTrue(), true);
    }

    public static Func1<Object, Boolean> equalsWith(Object other) {
        return new EqualsWithFunc1(other);
    }

    public static Func1<Object, Boolean> isInstanceOf(Class<?> clazz) {
        return new IsInstanceOfFunc1(clazz);
    }

    public static Func1<Observable<? extends Notification<?>>, Observable<?>> createRepeatDematerializer(Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler) {
        return new RepeatNotificationDematerializer(notificationHandler);
    }

    public static <T, R> Func1<Observable<T>, Observable<R>> createReplaySelectorAndObserveOn(Func1<? super Observable<T>, ? extends Observable<R>> selector, Scheduler scheduler) {
        return new SelectorAndObserveOn(selector, scheduler);
    }

    public static Func1<Observable<? extends Notification<?>>, Observable<?>> createRetryDematerializer(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler) {
        return new RetryNotificationDematerializer(notificationHandler);
    }

    public static <T> Func0<ConnectableObservable<T>> createReplaySupplier(Observable<T> source) {
        return new ReplaySupplierNoParams(source);
    }

    public static <T> Func0<ConnectableObservable<T>> createReplaySupplier(Observable<T> source, int bufferSize) {
        return new ReplaySupplierBuffer(source, bufferSize);
    }

    public static <T> Func0<ConnectableObservable<T>> createReplaySupplier(Observable<T> source, long time, TimeUnit unit, Scheduler scheduler) {
        ReplaySupplierBufferTime replaySupplierBufferTime = new ReplaySupplierBufferTime(source, time, unit, scheduler);
        return replaySupplierBufferTime;
    }

    public static <T> Func0<ConnectableObservable<T>> createReplaySupplier(Observable<T> source, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ReplaySupplierTime replaySupplierTime = new ReplaySupplierTime(source, bufferSize, time, unit, scheduler);
        return replaySupplierTime;
    }

    public static <T, R> Func2<R, T, R> createCollectorCaller(Action2<R, ? super T> collector) {
        return new CollectorCaller(collector);
    }
}
