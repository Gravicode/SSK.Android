package p008rx.observers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import p008rx.Notification;
import p008rx.Observer;
import p008rx.exceptions.CompositeException;

@Deprecated
/* renamed from: rx.observers.TestObserver */
public class TestObserver<T> implements Observer<T> {
    private static final Observer<Object> INERT = new Observer<Object>() {
        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    };
    private final Observer<T> delegate;
    private final List<Notification<T>> onCompletedEvents;
    private final List<Throwable> onErrorEvents;
    private final List<T> onNextEvents;

    public TestObserver(Observer<T> delegate2) {
        this.onNextEvents = new ArrayList();
        this.onErrorEvents = new ArrayList();
        this.onCompletedEvents = new ArrayList();
        this.delegate = delegate2;
    }

    public TestObserver() {
        this.onNextEvents = new ArrayList();
        this.onErrorEvents = new ArrayList();
        this.onCompletedEvents = new ArrayList();
        this.delegate = INERT;
    }

    public void onCompleted() {
        this.onCompletedEvents.add(Notification.createOnCompleted());
        this.delegate.onCompleted();
    }

    public List<Notification<T>> getOnCompletedEvents() {
        return Collections.unmodifiableList(this.onCompletedEvents);
    }

    public void onError(Throwable e) {
        this.onErrorEvents.add(e);
        this.delegate.onError(e);
    }

    public List<Throwable> getOnErrorEvents() {
        return Collections.unmodifiableList(this.onErrorEvents);
    }

    public void onNext(T t) {
        this.onNextEvents.add(t);
        this.delegate.onNext(t);
    }

    public List<T> getOnNextEvents() {
        return Collections.unmodifiableList(this.onNextEvents);
    }

    public List<Object> getEvents() {
        ArrayList<Object> events = new ArrayList<>();
        events.add(this.onNextEvents);
        events.add(this.onErrorEvents);
        events.add(this.onCompletedEvents);
        return Collections.unmodifiableList(events);
    }

    public void assertReceivedOnNext(List<T> items) {
        if (this.onNextEvents.size() != items.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Number of items does not match. Provided: ");
            sb.append(items.size());
            sb.append("  Actual: ");
            sb.append(this.onNextEvents.size());
            sb.append(".\n");
            sb.append("Provided values: ");
            sb.append(items);
            sb.append("\n");
            sb.append("Actual values: ");
            sb.append(this.onNextEvents);
            sb.append("\n");
            assertionError(sb.toString());
        }
        for (int i = 0; i < items.size(); i++) {
            T expected = items.get(i);
            T actual = this.onNextEvents.get(i);
            if (expected == null) {
                if (actual != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Value at index: ");
                    sb2.append(i);
                    sb2.append(" expected to be [null] but was: [");
                    sb2.append(actual);
                    sb2.append("]\n");
                    assertionError(sb2.toString());
                }
            } else if (!expected.equals(actual)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Value at index: ");
                sb3.append(i);
                sb3.append(" expected to be [");
                sb3.append(expected);
                sb3.append("] (");
                sb3.append(expected.getClass().getSimpleName());
                sb3.append(") but was: [");
                sb3.append(actual);
                sb3.append("] (");
                sb3.append(actual != null ? actual.getClass().getSimpleName() : "null");
                sb3.append(")\n");
                assertionError(sb3.toString());
            }
        }
    }

    public void assertTerminalEvent() {
        if (this.onErrorEvents.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Too many onError events: ");
            sb.append(this.onErrorEvents.size());
            assertionError(sb.toString());
        }
        if (this.onCompletedEvents.size() > 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Too many onCompleted events: ");
            sb2.append(this.onCompletedEvents.size());
            assertionError(sb2.toString());
        }
        if (this.onCompletedEvents.size() == 1 && this.onErrorEvents.size() == 1) {
            assertionError("Received both an onError and onCompleted. Should be one or the other.");
        }
        if (this.onCompletedEvents.isEmpty() && this.onErrorEvents.isEmpty()) {
            assertionError("No terminal events received.");
        }
    }

    /* access modifiers changed from: 0000 */
    public final void assertionError(String message) {
        StringBuilder b = new StringBuilder(message.length() + 32);
        b.append(message);
        b.append(" (");
        int c = this.onCompletedEvents.size();
        b.append(c);
        b.append(" completion");
        if (c != 1) {
            b.append('s');
        }
        b.append(')');
        if (!this.onErrorEvents.isEmpty()) {
            int size = this.onErrorEvents.size();
            b.append(" (+");
            b.append(size);
            b.append(" error");
            if (size != 1) {
                b.append('s');
            }
            b.append(')');
        }
        AssertionError ae = new AssertionError(b.toString());
        if (!this.onErrorEvents.isEmpty()) {
            if (this.onErrorEvents.size() == 1) {
                ae.initCause((Throwable) this.onErrorEvents.get(0));
            } else {
                ae.initCause(new CompositeException((Collection<? extends Throwable>) this.onErrorEvents));
            }
        }
        throw ae;
    }
}
