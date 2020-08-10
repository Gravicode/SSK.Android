package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.jakewharton.rxbinding.internal.Functions;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.functions.Action1;
import p008rx.functions.Func1;

public final class RxTextView {
    @CheckResult
    @NonNull
    public static Observable<Integer> editorActions(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return editorActions(view, Functions.FUNC1_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> editorActions(@NonNull TextView view, @NonNull Func1<? super Integer, Boolean> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return Observable.create((OnSubscribe<T>) new TextViewEditorActionOnSubscribe<T>(view, handled));
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return editorActionEvents(view, Functions.FUNC1_ALWAYS_TRUE);
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull TextView view, @NonNull Func1<? super TextViewEditorActionEvent, Boolean> handled) {
        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(handled, "handled == null");
        return Observable.create((OnSubscribe<T>) new TextViewEditorActionEventOnSubscribe<T>(view, handled));
    }

    @CheckResult
    @NonNull
    public static Observable<CharSequence> textChanges(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new TextViewTextOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewTextChangeEvent> textChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new TextViewTextChangeEventOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewBeforeTextChangeEvent> beforeTextChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new TextViewBeforeTextChangeEventOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Observable<TextViewAfterTextChangeEvent> afterTextChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new TextViewAfterTextChangeEventOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> text(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<CharSequence>() {
            public void call(CharSequence text) {
                view.setText(text);
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super Integer> textRes(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            public void call(Integer textRes) {
                view.setText(textRes.intValue());
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> error(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<CharSequence>() {
            public void call(CharSequence text) {
                view.setError(text);
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super Integer> errorRes(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            public void call(Integer textRes) {
                view.setError(view.getContext().getResources().getText(textRes.intValue()));
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> hint(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<CharSequence>() {
            public void call(CharSequence hint) {
                view.setHint(hint);
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super Integer> hintRes(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            public void call(Integer hintRes) {
                view.setHint(hintRes.intValue());
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super Integer> color(@NonNull final TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            public void call(Integer color) {
                view.setTextColor(color.intValue());
            }
        };
    }

    private RxTextView() {
        throw new AssertionError("No instances.");
    }
}
