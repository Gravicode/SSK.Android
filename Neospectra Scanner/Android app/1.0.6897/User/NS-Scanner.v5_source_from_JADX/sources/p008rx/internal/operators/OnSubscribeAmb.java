package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OnSubscribeAmb */
public final class OnSubscribeAmb<T> implements OnSubscribe<T> {
    final Iterable<? extends Observable<? extends T>> sources;

    /* renamed from: rx.internal.operators.OnSubscribeAmb$AmbSubscriber */
    static final class AmbSubscriber<T> extends Subscriber<T> {
        private boolean chosen;
        private final Selection<T> selection;
        private final Subscriber<? super T> subscriber;

        AmbSubscriber(long requested, Subscriber<? super T> subscriber2, Selection<T> selection2) {
            this.subscriber = subscriber2;
            this.selection = selection2;
            request(requested);
        }

        /* access modifiers changed from: private */
        public void requestMore(long n) {
            request(n);
        }

        public void onNext(T t) {
            if (isSelected()) {
                this.subscriber.onNext(t);
            }
        }

        public void onCompleted() {
            if (isSelected()) {
                this.subscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (isSelected()) {
                this.subscriber.onError(e);
            }
        }

        private boolean isSelected() {
            if (this.chosen) {
                return true;
            }
            if (this.selection.get() == this) {
                this.chosen = true;
                return true;
            } else if (this.selection.compareAndSet(null, this)) {
                this.selection.unsubscribeOthers(this);
                this.chosen = true;
                return true;
            } else {
                this.selection.unsubscribeLosers();
                return false;
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeAmb$Selection */
    static final class Selection<T> extends AtomicReference<AmbSubscriber<T>> {
        final Collection<AmbSubscriber<T>> ambSubscribers = new ConcurrentLinkedQueue();

        Selection() {
        }

        public void unsubscribeLosers() {
            AmbSubscriber<T> winner = (AmbSubscriber) get();
            if (winner != null) {
                unsubscribeOthers(winner);
            }
        }

        public void unsubscribeOthers(AmbSubscriber<T> notThis) {
            for (AmbSubscriber<T> other : this.ambSubscribers) {
                if (other != notThis) {
                    other.unsubscribe();
                }
            }
            this.ambSubscribers.clear();
        }
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        sources2.add(o7);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        sources2.add(o7);
        sources2.add(o8);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        List<Observable<? extends T>> sources2 = new ArrayList<>();
        sources2.add(o1);
        sources2.add(o2);
        sources2.add(o3);
        sources2.add(o4);
        sources2.add(o5);
        sources2.add(o6);
        sources2.add(o7);
        sources2.add(o8);
        sources2.add(o9);
        return amb(sources2);
    }

    public static <T> OnSubscribe<T> amb(Iterable<? extends Observable<? extends T>> sources2) {
        return new OnSubscribeAmb(sources2);
    }

    private OnSubscribeAmb(Iterable<? extends Observable<? extends T>> sources2) {
        this.sources = sources2;
    }

    public void call(Subscriber<? super T> subscriber) {
        final Selection<T> selection = new Selection<>();
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                AmbSubscriber ambSubscriber = (AmbSubscriber) selection.get();
                AmbSubscriber ambSubscriber2 = ambSubscriber;
                if (ambSubscriber != null) {
                    ambSubscriber2.unsubscribe();
                }
                OnSubscribeAmb.unsubscribeAmbSubscribers(selection.ambSubscribers);
            }
        }));
        for (Observable<? extends T> source : this.sources) {
            if (subscriber.isUnsubscribed()) {
                break;
            }
            AmbSubscriber<T> ambSubscriber = new AmbSubscriber<>(0, subscriber, selection);
            selection.ambSubscribers.add(ambSubscriber);
            AmbSubscriber ambSubscriber2 = (AmbSubscriber) selection.get();
            AmbSubscriber ambSubscriber3 = ambSubscriber2;
            if (ambSubscriber2 != null) {
                selection.unsubscribeOthers(ambSubscriber3);
                return;
            }
            source.unsafeSubscribe(ambSubscriber);
        }
        if (subscriber.isUnsubscribed()) {
            unsubscribeAmbSubscribers(selection.ambSubscribers);
        }
        subscriber.setProducer(new Producer() {
            public void request(long n) {
                AmbSubscriber ambSubscriber = (AmbSubscriber) selection.get();
                AmbSubscriber ambSubscriber2 = ambSubscriber;
                if (ambSubscriber != null) {
                    ambSubscriber2.requestMore(n);
                } else {
                    for (AmbSubscriber<T> ambSubscriber3 : selection.ambSubscribers) {
                        if (!ambSubscriber3.isUnsubscribed()) {
                            if (selection.get() == ambSubscriber3) {
                                ambSubscriber3.requestMore(n);
                                return;
                            }
                            ambSubscriber3.requestMore(n);
                        }
                    }
                }
            }
        });
    }

    static <T> void unsubscribeAmbSubscribers(Collection<AmbSubscriber<T>> ambSubscribers) {
        if (!ambSubscribers.isEmpty()) {
            for (AmbSubscriber<T> other : ambSubscribers) {
                other.unsubscribe();
            }
            ambSubscribers.clear();
        }
    }
}
