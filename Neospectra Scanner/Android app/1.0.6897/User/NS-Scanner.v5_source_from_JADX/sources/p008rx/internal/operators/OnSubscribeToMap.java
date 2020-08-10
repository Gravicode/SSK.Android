package p008rx.internal.operators;

import java.util.HashMap;
import java.util.Map;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.OnSubscribeToMap */
public final class OnSubscribeToMap<T, K, V> implements OnSubscribe<Map<K, V>>, Func0<Map<K, V>> {
    final Func1<? super T, ? extends K> keySelector;
    final Func0<? extends Map<K, V>> mapFactory;
    final Observable<T> source;
    final Func1<? super T, ? extends V> valueSelector;

    /* renamed from: rx.internal.operators.OnSubscribeToMap$ToMapSubscriber */
    static final class ToMapSubscriber<T, K, V> extends DeferredScalarSubscriberSafe<T, Map<K, V>> {
        final Func1<? super T, ? extends K> keySelector;
        final Func1<? super T, ? extends V> valueSelector;

        ToMapSubscriber(Subscriber<? super Map<K, V>> actual, Map<K, V> map, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2) {
            super(actual);
            this.value = map;
            this.hasValue = true;
            this.keySelector = keySelector2;
            this.valueSelector = valueSelector2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    ((Map) this.value).put(this.keySelector.call(t), this.valueSelector.call(t));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    unsubscribe();
                    onError(ex);
                }
            }
        }
    }

    public OnSubscribeToMap(Observable<T> source2, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2) {
        this(source2, keySelector2, valueSelector2, null);
    }

    public OnSubscribeToMap(Observable<T> source2, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, Func0<? extends Map<K, V>> mapFactory2) {
        this.source = source2;
        this.keySelector = keySelector2;
        this.valueSelector = valueSelector2;
        if (mapFactory2 == null) {
            this.mapFactory = this;
        } else {
            this.mapFactory = mapFactory2;
        }
    }

    public Map<K, V> call() {
        return new HashMap();
    }

    public void call(Subscriber<? super Map<K, V>> subscriber) {
        try {
            new ToMapSubscriber(subscriber, (Map) this.mapFactory.call(), this.keySelector, this.valueSelector).subscribeTo(this.source);
        } catch (Throwable ex) {
            Exceptions.throwOrReport(ex, (Observer<?>) subscriber);
        }
    }
}
