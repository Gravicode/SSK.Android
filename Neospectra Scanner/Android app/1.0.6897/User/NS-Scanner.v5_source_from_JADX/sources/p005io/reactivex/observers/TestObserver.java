package p005io.reactivex.observers;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.QueueDisposable;
import p005io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.observers.TestObserver */
public class TestObserver<T> extends BaseTestConsumer<T, TestObserver<T>> implements Observer<T>, Disposable, MaybeObserver<T>, SingleObserver<T>, CompletableObserver {
    private final Observer<? super T> actual;

    /* renamed from: qs */
    private QueueDisposable<T> f469qs;
    private final AtomicReference<Disposable> subscription;

    /* renamed from: io.reactivex.observers.TestObserver$EmptyObserver */
    enum EmptyObserver implements Observer<Object> {
        INSTANCE;

        public void onSubscribe(Disposable d) {
        }

        public void onNext(Object t) {
        }

        public void onError(Throwable t) {
        }

        public void onComplete() {
        }
    }

    public static <T> TestObserver<T> create() {
        return new TestObserver<>();
    }

    public static <T> TestObserver<T> create(Observer<? super T> delegate) {
        return new TestObserver<>(delegate);
    }

    public TestObserver() {
        this(EmptyObserver.INSTANCE);
    }

    public TestObserver(Observer<? super T> actual2) {
        this.subscription = new AtomicReference<>();
        this.actual = actual2;
    }

    public void onSubscribe(Disposable s) {
        this.lastThread = Thread.currentThread();
        if (s == null) {
            this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
        } else if (!this.subscription.compareAndSet(null, s)) {
            s.dispose();
            if (this.subscription.get() != DisposableHelper.DISPOSED) {
                List list = this.errors;
                StringBuilder sb = new StringBuilder();
                sb.append("onSubscribe received multiple subscriptions: ");
                sb.append(s);
                list.add(new IllegalStateException(sb.toString()));
            }
        } else {
            if (this.initialFusionMode != 0 && (s instanceof QueueDisposable)) {
                this.f469qs = (QueueDisposable) s;
                int m = this.f469qs.requestFusion(this.initialFusionMode);
                this.establishedFusionMode = m;
                if (m == 1) {
                    this.checkSubscriptionOnce = true;
                    this.lastThread = Thread.currentThread();
                    while (true) {
                        try {
                            Object poll = this.f469qs.poll();
                            Object obj = poll;
                            if (poll == null) {
                                break;
                            }
                            this.values.add(obj);
                        } catch (Throwable ex) {
                            this.errors.add(ex);
                        }
                    }
                    this.completions++;
                    this.subscription.lazySet(DisposableHelper.DISPOSED);
                    return;
                }
            }
            this.actual.onSubscribe(s);
        }
    }

    public void onNext(T t) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        this.lastThread = Thread.currentThread();
        if (this.establishedFusionMode == 2) {
            while (true) {
                try {
                    Object poll = this.f469qs.poll();
                    Object obj = poll;
                    if (poll == null) {
                        break;
                    }
                    this.values.add(obj);
                } catch (Throwable ex) {
                    this.errors.add(ex);
                    this.f469qs.dispose();
                }
            }
            return;
        }
        this.values.add(t);
        if (t == null) {
            this.errors.add(new NullPointerException("onNext received a null value"));
        }
        this.actual.onNext(t);
    }

    public void onError(Throwable t) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            if (t == null) {
                this.errors.add(new NullPointerException("onError received a null Throwable"));
            } else {
                this.errors.add(t);
            }
            this.actual.onError(t);
        } finally {
            this.done.countDown();
        }
    }

    public void onComplete() {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.subscription.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.completions++;
            this.actual.onComplete();
        } finally {
            this.done.countDown();
        }
    }

    public final boolean isCancelled() {
        return isDisposed();
    }

    public final void cancel() {
        dispose();
    }

    public final void dispose() {
        DisposableHelper.dispose(this.subscription);
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) this.subscription.get());
    }

    public final boolean hasSubscription() {
        return this.subscription.get() != null;
    }

    public final TestObserver<T> assertSubscribed() {
        if (this.subscription.get() != null) {
            return this;
        }
        throw fail("Not subscribed!");
    }

    public final TestObserver<T> assertNotSubscribed() {
        if (this.subscription.get() != null) {
            throw fail("Subscribed!");
        } else if (this.errors.isEmpty()) {
            return this;
        } else {
            throw fail("Not subscribed but errors found");
        }
    }

    public final TestObserver<T> assertOf(Consumer<? super TestObserver<T>> check) {
        try {
            check.accept(this);
            return this;
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> setInitialFusionMode(int mode) {
        this.initialFusionMode = mode;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> assertFusionMode(int mode) {
        int m = this.establishedFusionMode;
        if (m == mode) {
            return this;
        }
        if (this.f469qs != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fusion mode different. Expected: ");
            sb.append(fusionModeToString(mode));
            sb.append(", actual: ");
            sb.append(fusionModeToString(m));
            throw new AssertionError(sb.toString());
        }
        throw fail("Upstream is not fuseable");
    }

    static String fusionModeToString(int mode) {
        switch (mode) {
            case 0:
                return "NONE";
            case 1:
                return "SYNC";
            case 2:
                return "ASYNC";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown(");
                sb.append(mode);
                sb.append(")");
                return sb.toString();
        }
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> assertFuseable() {
        if (this.f469qs != null) {
            return this;
        }
        throw new AssertionError("Upstream is not fuseable.");
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> assertNotFuseable() {
        if (this.f469qs == null) {
            return this;
        }
        throw new AssertionError("Upstream is fuseable.");
    }

    public void onSuccess(T value) {
        onNext(value);
        onComplete();
    }
}
