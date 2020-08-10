package p008rx.observers;

import p008rx.Observer;
import p008rx.Subscriber;

/* renamed from: rx.observers.SerializedSubscriber */
public class SerializedSubscriber<T> extends Subscriber<T> {

    /* renamed from: s */
    private final Observer<T> f931s;

    public SerializedSubscriber(Subscriber<? super T> s) {
        this(s, true);
    }

    public SerializedSubscriber(Subscriber<? super T> s, boolean shareSubscriptions) {
        super(s, shareSubscriptions);
        this.f931s = new SerializedObserver(s);
    }

    public void onCompleted() {
        this.f931s.onCompleted();
    }

    public void onError(Throwable e) {
        this.f931s.onError(e);
    }

    public void onNext(T t) {
        this.f931s.onNext(t);
    }
}
