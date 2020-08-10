package p008rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.subjects.Subject;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.BufferUntilSubscriber */
public final class BufferUntilSubscriber<T> extends Subject<T, T> {
    static final Observer EMPTY_OBSERVER = new Observer() {
        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Object t) {
        }
    };
    private boolean forward;
    final State<T> state;

    /* renamed from: rx.internal.operators.BufferUntilSubscriber$OnSubscribeAction */
    static final class OnSubscribeAction<T> implements OnSubscribe<T> {
        final State<T> state;

        public OnSubscribeAction(State<T> state2) {
            this.state = state2;
        }

        public void call(Subscriber<? super T> s) {
            if (this.state.casObserverRef(null, s)) {
                s.add(Subscriptions.create(new Action0() {
                    public void call() {
                        OnSubscribeAction.this.state.set(BufferUntilSubscriber.EMPTY_OBSERVER);
                    }
                }));
                boolean win = false;
                synchronized (this.state.guard) {
                    if (!this.state.emitting) {
                        this.state.emitting = true;
                        win = true;
                    }
                }
                if (win) {
                    while (true) {
                        Object poll = this.state.buffer.poll();
                        Object o = poll;
                        if (poll != null) {
                            NotificationLite.accept((Observer) this.state.get(), o);
                        } else {
                            synchronized (this.state.guard) {
                                if (this.state.buffer.isEmpty()) {
                                    this.state.emitting = false;
                                    return;
                                }
                            }
                        }
                    }
                }
            } else {
                s.onError(new IllegalStateException("Only one subscriber allowed!"));
            }
        }
    }

    /* renamed from: rx.internal.operators.BufferUntilSubscriber$State */
    static final class State<T> extends AtomicReference<Observer<? super T>> {
        private static final long serialVersionUID = 8026705089538090368L;
        final ConcurrentLinkedQueue<Object> buffer = new ConcurrentLinkedQueue<>();
        boolean emitting;
        final Object guard = new Object();

        State() {
        }

        /* access modifiers changed from: 0000 */
        public boolean casObserverRef(Observer<? super T> expected, Observer<? super T> next) {
            return compareAndSet(expected, next);
        }
    }

    public static <T> BufferUntilSubscriber<T> create() {
        return new BufferUntilSubscriber<>(new State<>());
    }

    private BufferUntilSubscriber(State<T> state2) {
        super(new OnSubscribeAction(state2));
        this.state = state2;
    }

    private void emit(Object v) {
        synchronized (this.state.guard) {
            this.state.buffer.add(v);
            if (this.state.get() != null && !this.state.emitting) {
                this.forward = true;
                this.state.emitting = true;
            }
        }
        if (this.forward) {
            while (true) {
                Object poll = this.state.buffer.poll();
                Object o = poll;
                if (poll != null) {
                    NotificationLite.accept((Observer) this.state.get(), o);
                } else {
                    return;
                }
            }
        }
    }

    public void onCompleted() {
        if (this.forward) {
            ((Observer) this.state.get()).onCompleted();
        } else {
            emit(NotificationLite.completed());
        }
    }

    public void onError(Throwable e) {
        if (this.forward) {
            ((Observer) this.state.get()).onError(e);
        } else {
            emit(NotificationLite.error(e));
        }
    }

    public void onNext(T t) {
        if (this.forward) {
            ((Observer) this.state.get()).onNext(t);
        } else {
            emit(NotificationLite.next(t));
        }
    }

    public boolean hasObservers() {
        boolean z;
        synchronized (this.state.guard) {
            z = this.state.get() != null;
        }
        return z;
    }
}
