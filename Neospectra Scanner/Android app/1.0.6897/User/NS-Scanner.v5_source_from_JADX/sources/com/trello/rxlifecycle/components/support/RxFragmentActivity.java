package com.trello.rxlifecycle.components.support;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.app.FragmentActivity;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import p008rx.Observable;
import p008rx.subjects.BehaviorSubject;

public abstract class RxFragmentActivity extends FragmentActivity implements LifecycleProvider<ActivityEvent> {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @CheckResult
    @NonNull
    public final Observable<ActivityEvent> lifecycle() {
        return this.lifecycleSubject.asObservable();
    }

    @CheckResult
    @NonNull
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @CheckResult
    @NonNull
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(this.lifecycleSubject);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onStart() {
        super.onStart();
        this.lifecycleSubject.onNext(ActivityEvent.START);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onResume() {
        super.onResume();
        this.lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onPause() {
        this.lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onStop() {
        this.lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onDestroy() {
        this.lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }
}
