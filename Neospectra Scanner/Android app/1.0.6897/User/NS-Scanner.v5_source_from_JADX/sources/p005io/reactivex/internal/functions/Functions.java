package p005io.reactivex.internal.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscription;
import p005io.reactivex.Notification;
import p005io.reactivex.Scheduler;
import p005io.reactivex.exceptions.OnErrorNotImplementedException;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.BooleanSupplier;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Function3;
import p005io.reactivex.functions.Function4;
import p005io.reactivex.functions.Function5;
import p005io.reactivex.functions.Function6;
import p005io.reactivex.functions.Function7;
import p005io.reactivex.functions.Function8;
import p005io.reactivex.functions.Function9;
import p005io.reactivex.functions.LongConsumer;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.functions.Functions */
public final class Functions {
    static final Predicate<Object> ALWAYS_FALSE = new FalsePredicate();
    static final Predicate<Object> ALWAYS_TRUE = new TruePredicate();
    public static final Action EMPTY_ACTION = new EmptyAction();
    static final Consumer<Object> EMPTY_CONSUMER = new EmptyConsumer();
    public static final LongConsumer EMPTY_LONG_CONSUMER = new EmptyLongConsumer();
    public static final Runnable EMPTY_RUNNABLE = new EmptyRunnable();
    public static final Consumer<Throwable> ERROR_CONSUMER = new ErrorConsumer();
    static final Function<Object, Object> IDENTITY = new Identity();
    static final Comparator<Object> NATURAL_COMPARATOR = new NaturalObjectComparator();
    static final Callable<Object> NULL_SUPPLIER = new NullCallable();
    public static final Consumer<Throwable> ON_ERROR_MISSING = new OnErrorMissingConsumer();
    public static final Consumer<Subscription> REQUEST_MAX = new MaxRequestSubscription();

    /* renamed from: io.reactivex.internal.functions.Functions$ActionConsumer */
    static final class ActionConsumer<T> implements Consumer<T> {
        final Action action;

        ActionConsumer(Action action2) {
            this.action = action2;
        }

        public void accept(T t) throws Exception {
            this.action.run();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array2Func */
    static final class Array2Func<T1, T2, R> implements Function<Object[], R> {

        /* renamed from: f */
        final BiFunction<? super T1, ? super T2, ? extends R> f69f;

        Array2Func(BiFunction<? super T1, ? super T2, ? extends R> f) {
            this.f69f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 2) {
                return this.f69f.apply(a[0], a[1]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 2 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array3Func */
    static final class Array3Func<T1, T2, T3, R> implements Function<Object[], R> {

        /* renamed from: f */
        final Function3<T1, T2, T3, R> f70f;

        Array3Func(Function3<T1, T2, T3, R> f) {
            this.f70f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 3) {
                return this.f70f.apply(a[0], a[1], a[2]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 3 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array4Func */
    static final class Array4Func<T1, T2, T3, T4, R> implements Function<Object[], R> {

        /* renamed from: f */
        final Function4<T1, T2, T3, T4, R> f71f;

        Array4Func(Function4<T1, T2, T3, T4, R> f) {
            this.f71f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 4) {
                return this.f71f.apply(a[0], a[1], a[2], a[3]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 4 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array5Func */
    static final class Array5Func<T1, T2, T3, T4, T5, R> implements Function<Object[], R> {

        /* renamed from: f */
        private final Function5<T1, T2, T3, T4, T5, R> f72f;

        Array5Func(Function5<T1, T2, T3, T4, T5, R> f) {
            this.f72f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 5) {
                return this.f72f.apply(a[0], a[1], a[2], a[3], a[4]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 5 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array6Func */
    static final class Array6Func<T1, T2, T3, T4, T5, T6, R> implements Function<Object[], R> {

        /* renamed from: f */
        final Function6<T1, T2, T3, T4, T5, T6, R> f73f;

        Array6Func(Function6<T1, T2, T3, T4, T5, T6, R> f) {
            this.f73f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 6) {
                return this.f73f.apply(a[0], a[1], a[2], a[3], a[4], a[5]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 6 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array7Func */
    static final class Array7Func<T1, T2, T3, T4, T5, T6, T7, R> implements Function<Object[], R> {

        /* renamed from: f */
        final Function7<T1, T2, T3, T4, T5, T6, T7, R> f74f;

        Array7Func(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
            this.f74f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 7) {
                return this.f74f.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 7 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array8Func */
    static final class Array8Func<T1, T2, T3, T4, T5, T6, T7, T8, R> implements Function<Object[], R> {

        /* renamed from: f */
        final Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f75f;

        Array8Func(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
            this.f75f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 8) {
                return this.f75f.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 8 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Array9Func */
    static final class Array9Func<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> implements Function<Object[], R> {

        /* renamed from: f */
        final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f76f;

        Array9Func(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
            this.f76f = f;
        }

        public R apply(Object[] a) throws Exception {
            if (a.length == 9) {
                return this.f76f.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Array of size 9 expected but got ");
            sb.append(a.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ArrayListCapacityCallable */
    static final class ArrayListCapacityCallable<T> implements Callable<List<T>> {
        final int capacity;

        ArrayListCapacityCallable(int capacity2) {
            this.capacity = capacity2;
        }

        public List<T> call() throws Exception {
            return new ArrayList(this.capacity);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$BooleanSupplierPredicateReverse */
    static final class BooleanSupplierPredicateReverse<T> implements Predicate<T> {
        final BooleanSupplier supplier;

        BooleanSupplierPredicateReverse(BooleanSupplier supplier2) {
            this.supplier = supplier2;
        }

        public boolean test(T t) throws Exception {
            return !this.supplier.getAsBoolean();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$CastToClass */
    static final class CastToClass<T, U> implements Function<T, U> {
        final Class<U> clazz;

        CastToClass(Class<U> clazz2) {
            this.clazz = clazz2;
        }

        public U apply(T t) throws Exception {
            return this.clazz.cast(t);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ClassFilter */
    static final class ClassFilter<T, U> implements Predicate<T> {
        final Class<U> clazz;

        ClassFilter(Class<U> clazz2) {
            this.clazz = clazz2;
        }

        public boolean test(T t) throws Exception {
            return this.clazz.isInstance(t);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$EmptyAction */
    static final class EmptyAction implements Action {
        EmptyAction() {
        }

        public void run() {
        }

        public String toString() {
            return "EmptyAction";
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$EmptyConsumer */
    static final class EmptyConsumer implements Consumer<Object> {
        EmptyConsumer() {
        }

        public void accept(Object v) {
        }

        public String toString() {
            return "EmptyConsumer";
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$EmptyLongConsumer */
    static final class EmptyLongConsumer implements LongConsumer {
        EmptyLongConsumer() {
        }

        public void accept(long v) {
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$EmptyRunnable */
    static final class EmptyRunnable implements Runnable {
        EmptyRunnable() {
        }

        public void run() {
        }

        public String toString() {
            return "EmptyRunnable";
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$EqualsPredicate */
    static final class EqualsPredicate<T> implements Predicate<T> {
        final T value;

        EqualsPredicate(T value2) {
            this.value = value2;
        }

        public boolean test(T t) throws Exception {
            return ObjectHelper.equals(t, this.value);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ErrorConsumer */
    static final class ErrorConsumer implements Consumer<Throwable> {
        ErrorConsumer() {
        }

        public void accept(Throwable error) {
            RxJavaPlugins.onError(error);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$FalsePredicate */
    static final class FalsePredicate implements Predicate<Object> {
        FalsePredicate() {
        }

        public boolean test(Object o) {
            return false;
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$FutureAction */
    static final class FutureAction implements Action {
        final Future<?> future;

        FutureAction(Future<?> future2) {
            this.future = future2;
        }

        public void run() throws Exception {
            this.future.get();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$HashSetCallable */
    enum HashSetCallable implements Callable<Set<Object>> {
        INSTANCE;

        public Set<Object> call() throws Exception {
            return new HashSet();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$Identity */
    static final class Identity implements Function<Object, Object> {
        Identity() {
        }

        public Object apply(Object v) {
            return v;
        }

        public String toString() {
            return "IdentityFunction";
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$JustValue */
    static final class JustValue<T, U> implements Callable<U>, Function<T, U> {
        final U value;

        JustValue(U value2) {
            this.value = value2;
        }

        public U call() throws Exception {
            return this.value;
        }

        public U apply(T t) throws Exception {
            return this.value;
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ListSorter */
    static final class ListSorter<T> implements Function<List<T>, List<T>> {
        final Comparator<? super T> comparator;

        ListSorter(Comparator<? super T> comparator2) {
            this.comparator = comparator2;
        }

        public List<T> apply(List<T> v) {
            Collections.sort(v, this.comparator);
            return v;
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$MaxRequestSubscription */
    static final class MaxRequestSubscription implements Consumer<Subscription> {
        MaxRequestSubscription() {
        }

        public void accept(Subscription t) throws Exception {
            t.request(Long.MAX_VALUE);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NaturalComparator */
    enum NaturalComparator implements Comparator<Object> {
        INSTANCE;

        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NaturalObjectComparator */
    static final class NaturalObjectComparator implements Comparator<Object> {
        NaturalObjectComparator() {
        }

        public int compare(Object a, Object b) {
            return ((Comparable) a).compareTo(b);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NotificationOnComplete */
    static final class NotificationOnComplete<T> implements Action {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnComplete(Consumer<? super Notification<T>> onNotification2) {
            this.onNotification = onNotification2;
        }

        public void run() throws Exception {
            this.onNotification.accept(Notification.createOnComplete());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NotificationOnError */
    static final class NotificationOnError<T> implements Consumer<Throwable> {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnError(Consumer<? super Notification<T>> onNotification2) {
            this.onNotification = onNotification2;
        }

        public void accept(Throwable v) throws Exception {
            this.onNotification.accept(Notification.createOnError(v));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NotificationOnNext */
    static final class NotificationOnNext<T> implements Consumer<T> {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnNext(Consumer<? super Notification<T>> onNotification2) {
            this.onNotification = onNotification2;
        }

        public void accept(T v) throws Exception {
            this.onNotification.accept(Notification.createOnNext(v));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NullCallable */
    static final class NullCallable implements Callable<Object> {
        NullCallable() {
        }

        public Object call() {
            return null;
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$OnErrorMissingConsumer */
    static final class OnErrorMissingConsumer implements Consumer<Throwable> {
        OnErrorMissingConsumer() {
        }

        public void accept(Throwable error) {
            RxJavaPlugins.onError(new OnErrorNotImplementedException(error));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$TimestampFunction */
    static final class TimestampFunction<T> implements Function<T, Timed<T>> {
        final Scheduler scheduler;
        final TimeUnit unit;

        TimestampFunction(TimeUnit unit2, Scheduler scheduler2) {
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public Timed<T> apply(T t) throws Exception {
            return new Timed<>(t, this.scheduler.now(this.unit), this.unit);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ToMapKeySelector */
    static final class ToMapKeySelector<K, T> implements BiConsumer<Map<K, T>, T> {
        private final Function<? super T, ? extends K> keySelector;

        ToMapKeySelector(Function<? super T, ? extends K> keySelector2) {
            this.keySelector = keySelector2;
        }

        public void accept(Map<K, T> m, T t) throws Exception {
            m.put(this.keySelector.apply(t), t);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ToMapKeyValueSelector */
    static final class ToMapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, V>, T> {
        private final Function<? super T, ? extends K> keySelector;
        private final Function<? super T, ? extends V> valueSelector;

        ToMapKeyValueSelector(Function<? super T, ? extends V> valueSelector2, Function<? super T, ? extends K> keySelector2) {
            this.valueSelector = valueSelector2;
            this.keySelector = keySelector2;
        }

        public void accept(Map<K, V> m, T t) throws Exception {
            m.put(this.keySelector.apply(t), this.valueSelector.apply(t));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ToMultimapKeyValueSelector */
    static final class ToMultimapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, Collection<V>>, T> {
        private final Function<? super K, ? extends Collection<? super V>> collectionFactory;
        private final Function<? super T, ? extends K> keySelector;
        private final Function<? super T, ? extends V> valueSelector;

        ToMultimapKeyValueSelector(Function<? super K, ? extends Collection<? super V>> collectionFactory2, Function<? super T, ? extends V> valueSelector2, Function<? super T, ? extends K> keySelector2) {
            this.collectionFactory = collectionFactory2;
            this.valueSelector = valueSelector2;
            this.keySelector = keySelector2;
        }

        public void accept(Map<K, Collection<V>> m, T t) throws Exception {
            K key = this.keySelector.apply(t);
            Collection<V> coll = (Collection) m.get(key);
            if (coll == null) {
                coll = (Collection) this.collectionFactory.apply(key);
                m.put(key, coll);
            }
            coll.add(this.valueSelector.apply(t));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$TruePredicate */
    static final class TruePredicate implements Predicate<Object> {
        TruePredicate() {
        }

        public boolean test(Object o) {
            return true;
        }
    }

    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static <T1, T2, R> Function<Object[], R> toFunction(BiFunction<? super T1, ? super T2, ? extends R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array2Func(f);
    }

    public static <T1, T2, T3, R> Function<Object[], R> toFunction(Function3<T1, T2, T3, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array3Func(f);
    }

    public static <T1, T2, T3, T4, R> Function<Object[], R> toFunction(Function4<T1, T2, T3, T4, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array4Func(f);
    }

    public static <T1, T2, T3, T4, T5, R> Function<Object[], R> toFunction(Function5<T1, T2, T3, T4, T5, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array5Func(f);
    }

    public static <T1, T2, T3, T4, T5, T6, R> Function<Object[], R> toFunction(Function6<T1, T2, T3, T4, T5, T6, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array6Func(f);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Function<Object[], R> toFunction(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array7Func(f);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function<Object[], R> toFunction(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array8Func(f);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function<Object[], R> toFunction(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array9Func(f);
    }

    public static <T> Function<T, T> identity() {
        return IDENTITY;
    }

    public static <T> Consumer<T> emptyConsumer() {
        return EMPTY_CONSUMER;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return ALWAYS_FALSE;
    }

    public static <T> Callable<T> nullSupplier() {
        return NULL_SUPPLIER;
    }

    public static <T> Comparator<T> naturalOrder() {
        return NATURAL_COMPARATOR;
    }

    public static Action futureAction(Future<?> future) {
        return new FutureAction(future);
    }

    public static <T> Callable<T> justCallable(T value) {
        return new JustValue(value);
    }

    public static <T, U> Function<T, U> justFunction(U value) {
        return new JustValue(value);
    }

    public static <T, U> Function<T, U> castFunction(Class<U> target) {
        return new CastToClass(target);
    }

    public static <T> Callable<List<T>> createArrayList(int capacity) {
        return new ArrayListCapacityCallable(capacity);
    }

    public static <T> Predicate<T> equalsWith(T value) {
        return new EqualsPredicate(value);
    }

    public static <T> Callable<Set<T>> createHashSet() {
        return HashSetCallable.INSTANCE;
    }

    public static <T> Consumer<T> notificationOnNext(Consumer<? super Notification<T>> onNotification) {
        return new NotificationOnNext(onNotification);
    }

    public static <T> Consumer<Throwable> notificationOnError(Consumer<? super Notification<T>> onNotification) {
        return new NotificationOnError(onNotification);
    }

    public static <T> Action notificationOnComplete(Consumer<? super Notification<T>> onNotification) {
        return new NotificationOnComplete(onNotification);
    }

    public static <T> Consumer<T> actionConsumer(Action action) {
        return new ActionConsumer(action);
    }

    public static <T, U> Predicate<T> isInstanceOf(Class<U> clazz) {
        return new ClassFilter(clazz);
    }

    public static <T> Predicate<T> predicateReverseFor(BooleanSupplier supplier) {
        return new BooleanSupplierPredicateReverse(supplier);
    }

    public static <T> Function<T, Timed<T>> timestampWith(TimeUnit unit, Scheduler scheduler) {
        return new TimestampFunction(unit, scheduler);
    }

    public static <T, K> BiConsumer<Map<K, T>, T> toMapKeySelector(Function<? super T, ? extends K> keySelector) {
        return new ToMapKeySelector(keySelector);
    }

    public static <T, K, V> BiConsumer<Map<K, V>, T> toMapKeyValueSelector(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return new ToMapKeyValueSelector(valueSelector, keySelector);
    }

    public static <T, K, V> BiConsumer<Map<K, Collection<V>>, T> toMultimapKeyValueSelector(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
        return new ToMultimapKeyValueSelector(collectionFactory, valueSelector, keySelector);
    }

    public static <T> Comparator<T> naturalComparator() {
        return NaturalComparator.INSTANCE;
    }

    public static <T> Function<List<T>, List<T>> listSorter(Comparator<? super T> comparator) {
        return new ListSorter(comparator);
    }
}
