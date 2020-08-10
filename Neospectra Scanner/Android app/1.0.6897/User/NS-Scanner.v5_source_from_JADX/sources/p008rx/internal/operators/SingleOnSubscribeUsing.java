package p008rx.internal.operators;

import java.util.Arrays;
import java.util.Collection;
import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action1;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleOnSubscribeUsing */
public final class SingleOnSubscribeUsing<T, Resource> implements OnSubscribe<T> {
    final Action1<? super Resource> disposeAction;
    final boolean disposeEagerly;
    final Func0<Resource> resourceFactory;
    final Func1<? super Resource, ? extends Single<? extends T>> singleFactory;

    public SingleOnSubscribeUsing(Func0<Resource> resourceFactory2, Func1<? super Resource, ? extends Single<? extends T>> observableFactory, Action1<? super Resource> disposeAction2, boolean disposeEagerly2) {
        this.resourceFactory = resourceFactory2;
        this.singleFactory = observableFactory;
        this.disposeAction = disposeAction2;
        this.disposeEagerly = disposeEagerly2;
    }

    public void call(final SingleSubscriber<? super T> child) {
        try {
            final Resource resource = this.resourceFactory.call();
            try {
                Single<? extends T> single = (Single) this.singleFactory.call(resource);
                if (single == null) {
                    handleSubscriptionTimeError(child, resource, new NullPointerException("The single"));
                    return;
                }
                SingleSubscriber<T> parent = new SingleSubscriber<T>() {
                    public void onSuccess(T value) {
                        if (SingleOnSubscribeUsing.this.disposeEagerly) {
                            try {
                                SingleOnSubscribeUsing.this.disposeAction.call(resource);
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                child.onError(ex);
                                return;
                            }
                        }
                        child.onSuccess(value);
                        if (!SingleOnSubscribeUsing.this.disposeEagerly) {
                            try {
                                SingleOnSubscribeUsing.this.disposeAction.call(resource);
                            } catch (Throwable ex2) {
                                Exceptions.throwIfFatal(ex2);
                                RxJavaHooks.onError(ex2);
                            }
                        }
                    }

                    public void onError(Throwable error) {
                        SingleOnSubscribeUsing.this.handleSubscriptionTimeError(child, resource, error);
                    }
                };
                child.add(parent);
                single.subscribe(parent);
            } catch (Throwable ex) {
                handleSubscriptionTimeError(child, resource, ex);
            }
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            child.onError(ex2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void handleSubscriptionTimeError(SingleSubscriber<? super T> t, Resource resource, Throwable ex) {
        Exceptions.throwIfFatal(ex);
        if (this.disposeEagerly) {
            try {
                this.disposeAction.call(resource);
            } catch (Throwable ex2) {
                Exceptions.throwIfFatal(ex2);
                ex = new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{ex, ex2}));
            }
        }
        t.onError(ex);
        if (!this.disposeEagerly) {
            try {
                this.disposeAction.call(resource);
            } catch (Throwable ex22) {
                Exceptions.throwIfFatal(ex22);
                RxJavaHooks.onError(ex22);
            }
        }
    }
}
