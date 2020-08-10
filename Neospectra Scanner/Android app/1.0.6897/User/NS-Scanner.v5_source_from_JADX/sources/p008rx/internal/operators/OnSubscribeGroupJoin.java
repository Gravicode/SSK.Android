package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.observers.SerializedObserver;
import p008rx.observers.SerializedSubscriber;
import p008rx.subjects.PublishSubject;
import p008rx.subjects.Subject;
import p008rx.subscriptions.CompositeSubscription;
import p008rx.subscriptions.RefCountSubscription;

/* renamed from: rx.internal.operators.OnSubscribeGroupJoin */
public final class OnSubscribeGroupJoin<T1, T2, D1, D2, R> implements OnSubscribe<R> {
    final Observable<T1> left;
    final Func1<? super T1, ? extends Observable<D1>> leftDuration;
    final Func2<? super T1, ? super Observable<T2>, ? extends R> resultSelector;
    final Observable<T2> right;
    final Func1<? super T2, ? extends Observable<D2>> rightDuration;

    /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$ResultManager */
    final class ResultManager extends HashMap<Integer, Observer<T2>> implements Subscription {
        private static final long serialVersionUID = -3035156013812425335L;
        final RefCountSubscription cancel;
        final CompositeSubscription group;
        boolean leftDone;
        int leftIds;
        boolean rightDone;
        int rightIds;
        final Map<Integer, T2> rightMap = new HashMap();
        final Subscriber<? super R> subscriber;

        /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$ResultManager$LeftDurationObserver */
        final class LeftDurationObserver extends Subscriber<D1> {

            /* renamed from: id */
            final int f903id;
            boolean once = true;

            public LeftDurationObserver(int id) {
                this.f903id = id;
            }

            public void onCompleted() {
                Observer observer;
                if (this.once) {
                    this.once = false;
                    synchronized (ResultManager.this) {
                        observer = (Observer) ResultManager.this.leftMap().remove(Integer.valueOf(this.f903id));
                    }
                    if (observer != null) {
                        observer.onCompleted();
                    }
                    ResultManager.this.group.remove(this);
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorMain(e);
            }

            public void onNext(D1 d1) {
                onCompleted();
            }
        }

        /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$ResultManager$LeftObserver */
        final class LeftObserver extends Subscriber<T1> {
            LeftObserver() {
            }

            public void onNext(T1 args) {
                int id;
                List<T2> rightMapValues;
                try {
                    Subject<T2, T2> subj = PublishSubject.create();
                    Observer<T2> subjSerial = new SerializedObserver<>(subj);
                    synchronized (ResultManager.this) {
                        ResultManager resultManager = ResultManager.this;
                        int i = resultManager.leftIds;
                        resultManager.leftIds = i + 1;
                        id = i;
                        ResultManager.this.leftMap().put(Integer.valueOf(id), subjSerial);
                    }
                    Observable<T2> window = Observable.create((OnSubscribe<T>) new WindowObservableFunc<T>(subj, ResultManager.this.cancel));
                    Observable<D1> duration = (Observable) OnSubscribeGroupJoin.this.leftDuration.call(args);
                    Subscriber<D1> d1 = new LeftDurationObserver<>(id);
                    ResultManager.this.group.add(d1);
                    duration.unsafeSubscribe(d1);
                    R result = OnSubscribeGroupJoin.this.resultSelector.call(args, window);
                    synchronized (ResultManager.this) {
                        rightMapValues = new ArrayList<>(ResultManager.this.rightMap.values());
                    }
                    ResultManager.this.subscriber.onNext(result);
                    for (T2 t2 : rightMapValues) {
                        subjSerial.onNext(t2);
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, (Observer<?>) this);
                }
            }

            public void onCompleted() {
                List<Observer<T2>> list = null;
                synchronized (ResultManager.this) {
                    ResultManager.this.leftDone = true;
                    if (ResultManager.this.rightDone) {
                        list = new ArrayList<>(ResultManager.this.leftMap().values());
                        ResultManager.this.leftMap().clear();
                        ResultManager.this.rightMap.clear();
                    }
                }
                ResultManager.this.complete(list);
            }

            public void onError(Throwable e) {
                ResultManager.this.errorAll(e);
            }
        }

        /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$ResultManager$RightDurationObserver */
        final class RightDurationObserver extends Subscriber<D2> {

            /* renamed from: id */
            final int f904id;
            boolean once = true;

            public RightDurationObserver(int id) {
                this.f904id = id;
            }

            public void onCompleted() {
                if (this.once) {
                    this.once = false;
                    synchronized (ResultManager.this) {
                        ResultManager.this.rightMap.remove(Integer.valueOf(this.f904id));
                    }
                    ResultManager.this.group.remove(this);
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorMain(e);
            }

            public void onNext(D2 d2) {
                onCompleted();
            }
        }

        /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$ResultManager$RightObserver */
        final class RightObserver extends Subscriber<T2> {
            RightObserver() {
            }

            public void onNext(T2 args) {
                int id;
                List<Observer<T2>> list;
                try {
                    synchronized (ResultManager.this) {
                        ResultManager resultManager = ResultManager.this;
                        int i = resultManager.rightIds;
                        resultManager.rightIds = i + 1;
                        id = i;
                        ResultManager.this.rightMap.put(Integer.valueOf(id), args);
                    }
                    Observable<D2> duration = (Observable) OnSubscribeGroupJoin.this.rightDuration.call(args);
                    Subscriber<D2> d2 = new RightDurationObserver<>(id);
                    ResultManager.this.group.add(d2);
                    duration.unsafeSubscribe(d2);
                    synchronized (ResultManager.this) {
                        list = new ArrayList<>(ResultManager.this.leftMap().values());
                    }
                    for (Observer<T2> o : list) {
                        o.onNext(args);
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, (Observer<?>) this);
                }
            }

            public void onCompleted() {
                List<Observer<T2>> list = null;
                synchronized (ResultManager.this) {
                    ResultManager.this.rightDone = true;
                    if (ResultManager.this.leftDone) {
                        list = new ArrayList<>(ResultManager.this.leftMap().values());
                        ResultManager.this.leftMap().clear();
                        ResultManager.this.rightMap.clear();
                    }
                }
                ResultManager.this.complete(list);
            }

            public void onError(Throwable e) {
                ResultManager.this.errorAll(e);
            }
        }

        public ResultManager(Subscriber<? super R> subscriber2) {
            this.subscriber = subscriber2;
            this.group = new CompositeSubscription();
            this.cancel = new RefCountSubscription(this.group);
        }

        public void init() {
            Subscriber<T1> s1 = new LeftObserver<>();
            Subscriber<T2> s2 = new RightObserver<>();
            this.group.add(s1);
            this.group.add(s2);
            OnSubscribeGroupJoin.this.left.unsafeSubscribe(s1);
            OnSubscribeGroupJoin.this.right.unsafeSubscribe(s2);
        }

        public void unsubscribe() {
            this.cancel.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.cancel.isUnsubscribed();
        }

        /* access modifiers changed from: 0000 */
        public Map<Integer, Observer<T2>> leftMap() {
            return this;
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Throwable e) {
            List<Observer<T2>> list;
            synchronized (this) {
                list = new ArrayList<>(leftMap().values());
                leftMap().clear();
                this.rightMap.clear();
            }
            for (Observer<T2> o : list) {
                o.onError(e);
            }
            this.subscriber.onError(e);
            this.cancel.unsubscribe();
        }

        /* access modifiers changed from: 0000 */
        public void errorMain(Throwable e) {
            synchronized (this) {
                leftMap().clear();
                this.rightMap.clear();
            }
            this.subscriber.onError(e);
            this.cancel.unsubscribe();
        }

        /* access modifiers changed from: 0000 */
        public void complete(List<Observer<T2>> list) {
            if (list != null) {
                for (Observer<T2> o : list) {
                    o.onCompleted();
                }
                this.subscriber.onCompleted();
                this.cancel.unsubscribe();
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$WindowObservableFunc */
    static final class WindowObservableFunc<T> implements OnSubscribe<T> {
        final RefCountSubscription refCount;
        final Observable<T> underlying;

        /* renamed from: rx.internal.operators.OnSubscribeGroupJoin$WindowObservableFunc$WindowSubscriber */
        final class WindowSubscriber extends Subscriber<T> {
            private final Subscription ref;
            final Subscriber<? super T> subscriber;

            public WindowSubscriber(Subscriber<? super T> subscriber2, Subscription ref2) {
                super(subscriber2);
                this.subscriber = subscriber2;
                this.ref = ref2;
            }

            public void onNext(T args) {
                this.subscriber.onNext(args);
            }

            public void onError(Throwable e) {
                this.subscriber.onError(e);
                this.ref.unsubscribe();
            }

            public void onCompleted() {
                this.subscriber.onCompleted();
                this.ref.unsubscribe();
            }
        }

        public WindowObservableFunc(Observable<T> underlying2, RefCountSubscription refCount2) {
            this.refCount = refCount2;
            this.underlying = underlying2;
        }

        public void call(Subscriber<? super T> t1) {
            Subscription ref = this.refCount.get();
            WindowSubscriber wo = new WindowSubscriber<>(t1, ref);
            wo.add(ref);
            this.underlying.unsafeSubscribe(wo);
        }
    }

    public OnSubscribeGroupJoin(Observable<T1> left2, Observable<T2> right2, Func1<? super T1, ? extends Observable<D1>> leftDuration2, Func1<? super T2, ? extends Observable<D2>> rightDuration2, Func2<? super T1, ? super Observable<T2>, ? extends R> resultSelector2) {
        this.left = left2;
        this.right = right2;
        this.leftDuration = leftDuration2;
        this.rightDuration = rightDuration2;
        this.resultSelector = resultSelector2;
    }

    public void call(Subscriber<? super R> child) {
        ResultManager ro = new ResultManager<>(new SerializedSubscriber(child));
        child.add(ro);
        ro.init();
    }
}
