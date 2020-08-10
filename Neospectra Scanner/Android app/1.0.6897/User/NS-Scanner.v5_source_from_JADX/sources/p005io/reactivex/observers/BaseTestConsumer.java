package p005io.reactivex.observers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Notification;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.internal.util.VolatileSizeArrayList;
import p005io.reactivex.observers.BaseTestConsumer;

/* renamed from: io.reactivex.observers.BaseTestConsumer */
public abstract class BaseTestConsumer<T, U extends BaseTestConsumer<T, U>> implements Disposable {
    protected boolean checkSubscriptionOnce;
    protected long completions;
    protected final CountDownLatch done = new CountDownLatch(1);
    protected final List<Throwable> errors = new VolatileSizeArrayList();
    protected int establishedFusionMode;
    protected int initialFusionMode;
    protected Thread lastThread;
    protected CharSequence tag;
    protected boolean timeout;
    protected final List<T> values = new VolatileSizeArrayList();

    /* renamed from: io.reactivex.observers.BaseTestConsumer$TestWaitStrategy */
    public enum TestWaitStrategy implements Runnable {
        SPIN {
            public void run() {
            }
        },
        YIELD {
            public void run() {
                Thread.yield();
            }
        },
        SLEEP_1MS {
            public void run() {
                sleep(1);
            }
        },
        SLEEP_10MS {
            public void run() {
                sleep(10);
            }
        },
        SLEEP_100MS {
            public void run() {
                sleep(100);
            }
        },
        SLEEP_1000MS {
            public void run() {
                sleep(1000);
            }
        };

        public abstract void run();

        static void sleep(int millis) {
            try {
                Thread.sleep((long) millis);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public abstract U assertNotSubscribed();

    public abstract U assertSubscribed();

    public final Thread lastThread() {
        return this.lastThread;
    }

    public final List<T> values() {
        return this.values;
    }

    public final List<Throwable> errors() {
        return this.errors;
    }

    public final long completions() {
        return this.completions;
    }

    public final boolean isTerminated() {
        return this.done.getCount() == 0;
    }

    public final int valueCount() {
        return this.values.size();
    }

    public final int errorCount() {
        return this.errors.size();
    }

    /* access modifiers changed from: protected */
    public final AssertionError fail(String message) {
        StringBuilder b = new StringBuilder(message.length() + 64);
        b.append(message);
        b.append(" (");
        b.append("latch = ");
        b.append(this.done.getCount());
        b.append(", ");
        b.append("values = ");
        b.append(this.values.size());
        b.append(", ");
        b.append("errors = ");
        b.append(this.errors.size());
        b.append(", ");
        b.append("completions = ");
        b.append(this.completions);
        if (this.timeout) {
            b.append(", timeout!");
        }
        if (isDisposed()) {
            b.append(", disposed!");
        }
        CharSequence tag2 = this.tag;
        if (tag2 != null) {
            b.append(", tag = ");
            b.append(tag2);
        }
        b.append(')');
        AssertionError ae = new AssertionError(b.toString());
        if (!this.errors.isEmpty()) {
            if (this.errors.size() == 1) {
                ae.initCause((Throwable) this.errors.get(0));
            } else {
                ae.initCause(new CompositeException((Iterable<? extends Throwable>) this.errors));
            }
        }
        return ae;
    }

    public final U await() throws InterruptedException {
        if (this.done.getCount() == 0) {
            return this;
        }
        this.done.await();
        return this;
    }

    public final boolean await(long time, TimeUnit unit) throws InterruptedException {
        boolean z = true;
        boolean d = this.done.getCount() == 0 || this.done.await(time, unit);
        if (d) {
            z = false;
        }
        this.timeout = z;
        return d;
    }

    public final U assertComplete() {
        long c = this.completions;
        if (c == 0) {
            throw fail("Not completed");
        } else if (c <= 1) {
            return this;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple completions: ");
            sb.append(c);
            throw fail(sb.toString());
        }
    }

    public final U assertNotComplete() {
        long c = this.completions;
        if (c == 1) {
            throw fail("Completed!");
        } else if (c <= 1) {
            return this;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple completions: ");
            sb.append(c);
            throw fail(sb.toString());
        }
    }

    public final U assertNoErrors() {
        if (this.errors.size() == 0) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Error(s) present: ");
        sb.append(this.errors);
        throw fail(sb.toString());
    }

    public final U assertError(Throwable error) {
        return assertError(Functions.equalsWith(error));
    }

    public final U assertError(Class<? extends Throwable> errorClass) {
        return assertError(Functions.isInstanceOf(errorClass));
    }

    public final U assertError(Predicate<Throwable> errorPredicate) {
        int s = this.errors.size();
        if (s == 0) {
            throw fail("No errors");
        }
        boolean found = false;
        Iterator it = this.errors.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            try {
                if (errorPredicate.test((Throwable) it.next())) {
                    found = true;
                    break;
                }
            } catch (Exception ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        if (!found) {
            throw fail("Error not present");
        } else if (s == 1) {
            return this;
        } else {
            throw fail("Error present but other errors as well");
        }
    }

    public final U assertValue(T value) {
        if (this.values.size() != 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected: ");
            sb.append(valueAndClass(value));
            sb.append(", Actual: ");
            sb.append(this.values);
            throw fail(sb.toString());
        }
        T v = this.values.get(0);
        if (ObjectHelper.equals(value, v)) {
            return this;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected: ");
        sb2.append(valueAndClass(value));
        sb2.append(", Actual: ");
        sb2.append(valueAndClass(v));
        throw fail(sb2.toString());
    }

    public final U assertNever(T value) {
        int s = this.values.size();
        for (int i = 0; i < s; i++) {
            if (ObjectHelper.equals(this.values.get(i), value)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Value at position ");
                sb.append(i);
                sb.append(" is equal to ");
                sb.append(valueAndClass(value));
                sb.append("; Expected them to be different");
                throw fail(sb.toString());
            }
        }
        return this;
    }

    public final U assertValue(Predicate<T> valuePredicate) {
        assertValueAt(0, valuePredicate);
        if (this.values.size() <= 1) {
            return this;
        }
        throw fail("Value present but other values as well");
    }

    public final U assertNever(Predicate<? super T> valuePredicate) {
        int s = this.values.size();
        int i = 0;
        while (i < s) {
            try {
                if (valuePredicate.test(this.values.get(i))) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Value at position ");
                    sb.append(i);
                    sb.append(" matches predicate ");
                    sb.append(valuePredicate.toString());
                    sb.append(", which was not expected.");
                    throw fail(sb.toString());
                }
                i++;
            } catch (Exception ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        return this;
    }

    @Experimental
    public final U assertValueAt(int index, T value) {
        int s = this.values.size();
        if (s == 0) {
            throw fail("No values");
        } else if (index >= s) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid index: ");
            sb.append(index);
            throw fail(sb.toString());
        } else {
            T v = this.values.get(index);
            if (ObjectHelper.equals(value, v)) {
                return this;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected: ");
            sb2.append(valueAndClass(value));
            sb2.append(", Actual: ");
            sb2.append(valueAndClass(v));
            throw fail(sb2.toString());
        }
    }

    public final U assertValueAt(int index, Predicate<T> valuePredicate) {
        if (this.values.size() == 0) {
            throw fail("No values");
        } else if (index >= this.values.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid index: ");
            sb.append(index);
            throw fail(sb.toString());
        } else {
            boolean found = false;
            try {
                if (valuePredicate.test(this.values.get(index))) {
                    found = true;
                }
                if (found) {
                    return this;
                }
                throw fail("Value not present");
            } catch (Exception ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
    }

    public static String valueAndClass(Object o) {
        if (o == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(o);
        sb.append(" (class: ");
        sb.append(o.getClass().getSimpleName());
        sb.append(")");
        return sb.toString();
    }

    public final U assertValueCount(int count) {
        int s = this.values.size();
        if (s == count) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Value counts differ; Expected: ");
        sb.append(count);
        sb.append(", Actual: ");
        sb.append(s);
        throw fail(sb.toString());
    }

    public final U assertNoValues() {
        return assertValueCount(0);
    }

    public final U assertValues(T... values2) {
        int s = this.values.size();
        if (s != values2.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Value count differs; Expected: ");
            sb.append(values2.length);
            sb.append(" ");
            sb.append(Arrays.toString(values2));
            sb.append(", Actual: ");
            sb.append(s);
            sb.append(" ");
            sb.append(this.values);
            throw fail(sb.toString());
        }
        for (int i = 0; i < s; i++) {
            T v = this.values.get(i);
            T u = values2[i];
            if (!ObjectHelper.equals(u, v)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Values at position ");
                sb2.append(i);
                sb2.append(" differ; Expected: ");
                sb2.append(valueAndClass(u));
                sb2.append(", Actual: ");
                sb2.append(valueAndClass(v));
                throw fail(sb2.toString());
            }
        }
        return this;
    }

    @Experimental
    public final U assertValuesOnly(T... values2) {
        return assertSubscribed().assertValues(values2).assertNoErrors().assertNotComplete();
    }

    public final U assertValueSet(Collection<? extends T> expected) {
        if (expected.isEmpty()) {
            assertNoValues();
            return this;
        }
        for (T v : this.values) {
            if (!expected.contains(v)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Value not in the expected collection: ");
                sb.append(valueAndClass(v));
                throw fail(sb.toString());
            }
        }
        return this;
    }

    @Experimental
    public final U assertValueSetOnly(Collection<? extends T> expected) {
        return assertSubscribed().assertValueSet(expected).assertNoErrors().assertNotComplete();
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final U assertValueSequence(java.lang.Iterable<? extends T> r10) {
        /*
            r9 = this;
            r0 = 0
            java.util.List<T> r1 = r9.values
            java.util.Iterator r1 = r1.iterator()
            java.util.Iterator r2 = r10.iterator()
        L_0x000b:
            boolean r3 = r2.hasNext()
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x0057
            if (r3 != 0) goto L_0x0018
            goto L_0x0057
        L_0x0018:
            java.lang.Object r5 = r2.next()
            java.lang.Object r6 = r1.next()
            boolean r7 = p005io.reactivex.internal.functions.ObjectHelper.equals(r5, r6)
            if (r7 != 0) goto L_0x0054
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Values at position "
            r7.append(r8)
            r7.append(r0)
            java.lang.String r8 = " differ; Expected: "
            r7.append(r8)
            java.lang.String r8 = valueAndClass(r5)
            r7.append(r8)
            java.lang.String r8 = ", Actual: "
            r7.append(r8)
            java.lang.String r8 = valueAndClass(r6)
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            java.lang.AssertionError r7 = r9.fail(r7)
            throw r7
        L_0x0054:
            int r0 = r0 + 1
            goto L_0x000b
        L_0x0057:
            if (r4 == 0) goto L_0x0074
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "More values received than expected ("
            r5.append(r6)
            r5.append(r0)
            java.lang.String r6 = ")"
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            java.lang.AssertionError r5 = r9.fail(r5)
            throw r5
        L_0x0074:
            if (r3 == 0) goto L_0x0091
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Fewer values received than expected ("
            r5.append(r6)
            r5.append(r0)
            java.lang.String r6 = ")"
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            java.lang.AssertionError r5 = r9.fail(r5)
            throw r5
        L_0x0091:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.observers.BaseTestConsumer.assertValueSequence(java.lang.Iterable):io.reactivex.observers.BaseTestConsumer");
    }

    @Experimental
    public final U assertValueSequenceOnly(Iterable<? extends T> sequence) {
        return assertSubscribed().assertValueSequence(sequence).assertNoErrors().assertNotComplete();
    }

    public final U assertTerminated() {
        if (this.done.getCount() != 0) {
            throw fail("Subscriber still running!");
        }
        long c = this.completions;
        if (c > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Terminated with multiple completions: ");
            sb.append(c);
            throw fail(sb.toString());
        }
        int s = this.errors.size();
        if (s > 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Terminated with multiple errors: ");
            sb2.append(s);
            throw fail(sb2.toString());
        } else if (c == 0 || s == 0) {
            return this;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Terminated with multiple completions and errors: ");
            sb3.append(c);
            throw fail(sb3.toString());
        }
    }

    public final U assertNotTerminated() {
        if (this.done.getCount() != 0) {
            return this;
        }
        throw fail("Subscriber terminated!");
    }

    public final boolean awaitTerminalEvent() {
        try {
            await();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public final boolean awaitTerminalEvent(long duration, TimeUnit unit) {
        try {
            return await(duration, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public final U assertErrorMessage(String message) {
        int s = this.errors.size();
        if (s == 0) {
            throw fail("No errors");
        } else if (s == 1) {
            String errorMessage = ((Throwable) this.errors.get(0)).getMessage();
            if (ObjectHelper.equals(message, errorMessage)) {
                return this;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Error message differs; Expected: ");
            sb.append(message);
            sb.append(", Actual: ");
            sb.append(errorMessage);
            throw fail(sb.toString());
        } else {
            throw fail("Multiple errors");
        }
    }

    public final List<List<Object>> getEvents() {
        List<List<Object>> result = new ArrayList<>();
        result.add(values());
        result.add(errors());
        List<Object> completeList = new ArrayList<>();
        for (long i = 0; i < this.completions; i++) {
            completeList.add(Notification.createOnComplete());
        }
        result.add(completeList);
        return result;
    }

    public final U assertResult(T... values2) {
        return assertSubscribed().assertValues(values2).assertNoErrors().assertComplete();
    }

    public final U assertFailure(Class<? extends Throwable> error, T... values2) {
        return assertSubscribed().assertValues(values2).assertError(error).assertNotComplete();
    }

    public final U assertFailure(Predicate<Throwable> errorPredicate, T... values2) {
        return assertSubscribed().assertValues(values2).assertError(errorPredicate).assertNotComplete();
    }

    public final U assertFailureAndMessage(Class<? extends Throwable> error, String message, T... values2) {
        return assertSubscribed().assertValues(values2).assertError(error).assertErrorMessage(message).assertNotComplete();
    }

    public final U awaitDone(long time, TimeUnit unit) {
        try {
            if (!this.done.await(time, unit)) {
                this.timeout = true;
                dispose();
            }
            return this;
        } catch (InterruptedException ex) {
            dispose();
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    public final U assertEmpty() {
        return assertSubscribed().assertNoValues().assertNoErrors().assertNotComplete();
    }

    public final U withTag(CharSequence tag2) {
        this.tag = tag2;
        return this;
    }

    public final U awaitCount(int atLeast) {
        return awaitCount(atLeast, TestWaitStrategy.SLEEP_10MS, 5000);
    }

    public final U awaitCount(int atLeast, Runnable waitStrategy) {
        return awaitCount(atLeast, waitStrategy, 5000);
    }

    public final U awaitCount(int atLeast, Runnable waitStrategy, long timeoutMillis) {
        long start = System.currentTimeMillis();
        while (true) {
            if (timeoutMillis <= 0 || System.currentTimeMillis() - start < timeoutMillis) {
                if (this.done.getCount() == 0 || this.values.size() >= atLeast) {
                    break;
                }
                waitStrategy.run();
            } else {
                this.timeout = true;
                break;
            }
        }
        return this;
    }

    public final boolean isTimeout() {
        return this.timeout;
    }

    public final U clearTimeout() {
        this.timeout = false;
        return this;
    }

    public final U assertTimeout() {
        if (this.timeout) {
            return this;
        }
        throw fail("No timeout?!");
    }

    public final U assertNoTimeout() {
        if (!this.timeout) {
            return this;
        }
        throw fail("Timeout?!");
    }
}
