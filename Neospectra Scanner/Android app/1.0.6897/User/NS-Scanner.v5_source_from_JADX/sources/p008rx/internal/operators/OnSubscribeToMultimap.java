package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.OnSubscribeToMultimap */
public final class OnSubscribeToMultimap<T, K, V> implements OnSubscribe<Map<K, Collection<V>>>, Func0<Map<K, Collection<V>>> {
    private final Func1<? super K, ? extends Collection<V>> collectionFactory;
    private final Func1<? super T, ? extends K> keySelector;
    private final Func0<? extends Map<K, Collection<V>>> mapFactory;
    private final Observable<T> source;
    private final Func1<? super T, ? extends V> valueSelector;

    /* renamed from: rx.internal.operators.OnSubscribeToMultimap$DefaultMultimapCollectionFactory */
    private static final class DefaultMultimapCollectionFactory<K, V> implements Func1<K, Collection<V>> {
        private static final DefaultMultimapCollectionFactory<Object, Object> INSTANCE = new DefaultMultimapCollectionFactory<>();

        private DefaultMultimapCollectionFactory() {
        }

        static <K, V> DefaultMultimapCollectionFactory<K, V> instance() {
            return INSTANCE;
        }

        public Collection<V> call(K k) {
            return new ArrayList();
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeToMultimap$ToMultimapSubscriber */
    private static final class ToMultimapSubscriber<T, K, V> extends DeferredScalarSubscriberSafe<T, Map<K, Collection<V>>> {
        private final Func1<? super K, ? extends Collection<V>> collectionFactory;
        private final Func1<? super T, ? extends K> keySelector;
        private final Func1<? super T, ? extends V> valueSelector;

        ToMultimapSubscriber(Subscriber<? super Map<K, Collection<V>>> subscriber, Map<K, Collection<V>> map, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, Func1<? super K, ? extends Collection<V>> collectionFactory2) {
            super(subscriber);
            this.value = map;
            this.hasValue = true;
            this.keySelector = keySelector2;
            this.valueSelector = valueSelector2;
            this.collectionFactory = collectionFactory2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    K key = this.keySelector.call(t);
                    V v = this.valueSelector.call(t);
                    Collection collection = (Collection) ((Map) this.value).get(key);
                    if (collection == null) {
                        collection = (Collection) this.collectionFactory.call(key);
                        ((Map) this.value).put(key, collection);
                    }
                    collection.add(v);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    unsubscribe();
                    onError(ex);
                }
            }
        }
    }

    public OnSubscribeToMultimap(Observable<T> source2, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2) {
        this(source2, keySelector2, valueSelector2, null, DefaultMultimapCollectionFactory.instance());
    }

    public OnSubscribeToMultimap(Observable<T> source2, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, Func0<? extends Map<K, Collection<V>>> mapFactory2) {
        this(source2, keySelector2, valueSelector2, mapFactory2, DefaultMultimapCollectionFactory.instance());
    }

    public OnSubscribeToMultimap(Observable<T> source2, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, Func0<? extends Map<K, Collection<V>>> mapFactory2, Func1<? super K, ? extends Collection<V>> collectionFactory2) {
        this.source = source2;
        this.keySelector = keySelector2;
        this.valueSelector = valueSelector2;
        if (mapFactory2 == null) {
            this.mapFactory = this;
        } else {
            this.mapFactory = mapFactory2;
        }
        this.collectionFactory = collectionFactory2;
    }

    public Map<K, Collection<V>> call() {
        return new HashMap();
    }

    public void call(Subscriber<? super Map<K, Collection<V>>> subscriber) {
        try {
            ToMultimapSubscriber toMultimapSubscriber = new ToMultimapSubscriber(subscriber, (Map) this.mapFactory.call(), this.keySelector, this.valueSelector, this.collectionFactory);
            toMultimapSubscriber.subscribeTo(this.source);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            subscriber.onError(ex);
        }
    }
}
