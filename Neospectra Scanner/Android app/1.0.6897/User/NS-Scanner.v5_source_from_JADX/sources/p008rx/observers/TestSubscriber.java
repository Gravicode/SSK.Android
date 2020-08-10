package p008rx.observers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import p008rx.Notification;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.annotations.Experimental;
import p008rx.exceptions.CompositeException;

/* renamed from: rx.observers.TestSubscriber */
public class TestSubscriber<T> extends Subscriber<T> {
    private static final Observer<Object> INERT = new Observer<Object>() {
        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    };
    private int completions;
    private final Observer<T> delegate;
    private final List<Throwable> errors;
    private volatile Thread lastSeenThread;
    private final CountDownLatch latch;
    private volatile int valueCount;
    private final List<T> values;

    public TestSubscriber(long initialRequest) {
        this(INERT, initialRequest);
    }

    public TestSubscriber(Observer<T> delegate2, long initialRequest) {
        this.latch = new CountDownLatch(1);
        if (delegate2 == null) {
            throw new NullPointerException();
        }
        this.delegate = delegate2;
        if (initialRequest >= 0) {
            request(initialRequest);
        }
        this.values = new ArrayList();
        this.errors = new ArrayList();
    }

    public TestSubscriber(Subscriber<T> delegate2) {
        this(delegate2, -1);
    }

    public TestSubscriber(Observer<T> delegate2) {
        this(delegate2, -1);
    }

    public TestSubscriber() {
        this(-1);
    }

    public static <T> TestSubscriber<T> create() {
        return new TestSubscriber<>();
    }

    public static <T> TestSubscriber<T> create(long initialRequest) {
        return new TestSubscriber<>(initialRequest);
    }

    public static <T> TestSubscriber<T> create(Observer<T> delegate2, long initialRequest) {
        return new TestSubscriber<>(delegate2, initialRequest);
    }

    public static <T> TestSubscriber<T> create(Subscriber<T> delegate2) {
        return new TestSubscriber<>(delegate2);
    }

    public static <T> TestSubscriber<T> create(Observer<T> delegate2) {
        return new TestSubscriber<>(delegate2);
    }

    public void onCompleted() {
        try {
            this.completions++;
            this.lastSeenThread = Thread.currentThread();
            this.delegate.onCompleted();
        } finally {
            this.latch.countDown();
        }
    }

    @Deprecated
    public List<Notification<T>> getOnCompletedEvents() {
        int c = this.completions;
        List<Notification<T>> result = new ArrayList<>(c != 0 ? c : 1);
        for (int i = 0; i < c; i++) {
            result.add(Notification.createOnCompleted());
        }
        return result;
    }

    @Experimental
    public final int getCompletions() {
        return this.completions;
    }

    public void onError(Throwable e) {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.errors.add(e);
            this.delegate.onError(e);
        } finally {
            this.latch.countDown();
        }
    }

    public List<Throwable> getOnErrorEvents() {
        return this.errors;
    }

    public void onNext(T t) {
        this.lastSeenThread = Thread.currentThread();
        this.values.add(t);
        this.valueCount = this.values.size();
        this.delegate.onNext(t);
    }

    public final int getValueCount() {
        return this.valueCount;
    }

    public void requestMore(long n) {
        request(n);
    }

    public List<T> getOnNextEvents() {
        return this.values;
    }

    public void assertReceivedOnNext(List<T> items) {
        if (this.values.size() != items.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Number of items does not match. Provided: ");
            sb.append(items.size());
            sb.append("  Actual: ");
            sb.append(this.values.size());
            sb.append(".\n");
            sb.append("Provided values: ");
            sb.append(items);
            sb.append("\n");
            sb.append("Actual values: ");
            sb.append(this.values);
            sb.append("\n");
            assertionError(sb.toString());
        }
        for (int i = 0; i < items.size(); i++) {
            assertItem(items.get(i), i);
        }
    }

    private void assertItem(T expected, int i) {
        T actual = this.values.get(i);
        if (expected == null) {
            if (actual != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Value at index: ");
                sb.append(i);
                sb.append(" expected to be [null] but was: [");
                sb.append(actual);
                sb.append("]\n");
                assertionError(sb.toString());
            }
        } else if (!expected.equals(actual)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Value at index: ");
            sb2.append(i);
            sb2.append(" expected to be [");
            sb2.append(expected);
            sb2.append("] (");
            sb2.append(expected.getClass().getSimpleName());
            sb2.append(") but was: [");
            sb2.append(actual);
            sb2.append("] (");
            sb2.append(actual != null ? actual.getClass().getSimpleName() : "null");
            sb2.append(")\n");
            assertionError(sb2.toString());
        }
    }

    @Experimental
    public final boolean awaitValueCount(int expected, long timeout, TimeUnit unit) {
        while (timeout != 0 && this.valueCount < expected) {
            try {
                unit.sleep(1);
                timeout--;
            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted", e);
            }
        }
        return this.valueCount >= expected;
    }

    public void assertTerminalEvent() {
        if (this.errors.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Too many onError events: ");
            sb.append(this.errors.size());
            assertionError(sb.toString());
        }
        if (this.completions > 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Too many onCompleted events: ");
            sb2.append(this.completions);
            assertionError(sb2.toString());
        }
        if (this.completions == 1 && this.errors.size() == 1) {
            assertionError("Received both an onError and onCompleted. Should be one or the other.");
        }
        if (this.completions == 0 && this.errors.isEmpty()) {
            assertionError("No terminal events received.");
        }
    }

    public void assertUnsubscribed() {
        if (!isUnsubscribed()) {
            assertionError("Not unsubscribed.");
        }
    }

    public void assertNoErrors() {
        if (!getOnErrorEvents().isEmpty()) {
            assertionError("Unexpected onError events");
        }
    }

    public void awaitTerminalEvent() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted", e);
        }
    }

    public void awaitTerminalEvent(long timeout, TimeUnit unit) {
        try {
            this.latch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted", e);
        }
    }

    public void awaitTerminalEventAndUnsubscribeOnTimeout(long timeout, TimeUnit unit) {
        try {
            if (!this.latch.await(timeout, unit)) {
                unsubscribe();
            }
        } catch (InterruptedException e) {
            unsubscribe();
        }
    }

    public Thread getLastSeenThread() {
        return this.lastSeenThread;
    }

    public void assertCompleted() {
        int s = this.completions;
        if (s == 0) {
            assertionError("Not completed!");
        } else if (s > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Completed multiple times: ");
            sb.append(s);
            assertionError(sb.toString());
        }
    }

    public void assertNotCompleted() {
        int s = this.completions;
        if (s == 1) {
            assertionError("Completed!");
        } else if (s > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Completed multiple times: ");
            sb.append(s);
            assertionError(sb.toString());
        }
    }

    public void assertError(Class<? extends Throwable> clazz) {
        List<Throwable> err = this.errors;
        if (err.isEmpty()) {
            assertionError("No errors");
        } else if (err.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple errors: ");
            sb.append(err.size());
            AssertionError ae = new AssertionError(sb.toString());
            ae.initCause(new CompositeException((Collection<? extends Throwable>) err));
            throw ae;
        } else if (!clazz.isInstance(err.get(0))) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Exceptions differ; expected: ");
            sb2.append(clazz);
            sb2.append(", actual: ");
            sb2.append(err.get(0));
            AssertionError ae2 = new AssertionError(sb2.toString());
            ae2.initCause((Throwable) err.get(0));
            throw ae2;
        }
    }

    public void assertError(Throwable throwable) {
        List<Throwable> err = this.errors;
        if (err.isEmpty()) {
            assertionError("No errors");
        } else if (err.size() > 1) {
            assertionError("Multiple errors");
        } else if (!throwable.equals(err.get(0))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Exceptions differ; expected: ");
            sb.append(throwable);
            sb.append(", actual: ");
            sb.append(err.get(0));
            assertionError(sb.toString());
        }
    }

    public void assertNoTerminalEvent() {
        List<Throwable> err = this.errors;
        int s = this.completions;
        if (err.isEmpty() && s <= 0) {
            return;
        }
        if (err.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Found ");
            sb.append(err.size());
            sb.append(" errors and ");
            sb.append(s);
            sb.append(" completion events instead of none");
            assertionError(sb.toString());
        } else if (err.size() == 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Found ");
            sb2.append(err.size());
            sb2.append(" errors and ");
            sb2.append(s);
            sb2.append(" completion events instead of none");
            assertionError(sb2.toString());
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Found ");
            sb3.append(err.size());
            sb3.append(" errors and ");
            sb3.append(s);
            sb3.append(" completion events instead of none");
            assertionError(sb3.toString());
        }
    }

    public void assertNoValues() {
        int s = this.values.size();
        if (s != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("No onNext events expected yet some received: ");
            sb.append(s);
            assertionError(sb.toString());
        }
    }

    public void assertValueCount(int count) {
        int s = this.values.size();
        if (s != count) {
            StringBuilder sb = new StringBuilder();
            sb.append("Number of onNext events differ; expected: ");
            sb.append(count);
            sb.append(", actual: ");
            sb.append(s);
            assertionError(sb.toString());
        }
    }

    public void assertValues(T... values2) {
        assertReceivedOnNext(Arrays.asList(values2));
    }

    public void assertValue(T value) {
        assertReceivedOnNext(Collections.singletonList(value));
    }

    /* access modifiers changed from: 0000 */
    public final void assertionError(String message) {
        StringBuilder b = new StringBuilder(message.length() + 32);
        b.append(message);
        b.append(" (");
        int c = this.completions;
        b.append(c);
        b.append(" completion");
        if (c != 1) {
            b.append('s');
        }
        b.append(')');
        if (!this.errors.isEmpty()) {
            int size = this.errors.size();
            b.append(" (+");
            b.append(size);
            b.append(" error");
            if (size != 1) {
                b.append('s');
            }
            b.append(')');
        }
        AssertionError ae = new AssertionError(b.toString());
        if (!this.errors.isEmpty()) {
            if (this.errors.size() == 1) {
                ae.initCause((Throwable) this.errors.get(0));
            } else {
                ae.initCause(new CompositeException((Collection<? extends Throwable>) this.errors));
            }
        }
        throw ae;
    }

    @Experimental
    public final void assertValuesAndClear(T expectedFirstValue, T... expectedRestValues) {
        assertValueCount(expectedRestValues.length + 1);
        assertItem(expectedFirstValue, 0);
        for (int i = 0; i < expectedRestValues.length; i++) {
            assertItem(expectedRestValues[i], i + 1);
        }
        this.values.clear();
    }
}
