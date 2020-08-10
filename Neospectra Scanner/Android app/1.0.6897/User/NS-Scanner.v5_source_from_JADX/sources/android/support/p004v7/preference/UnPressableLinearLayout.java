package android.support.p004v7.preference;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.preference.UnPressableLinearLayout */
public class UnPressableLinearLayout extends LinearLayout {
    public UnPressableLinearLayout(Context context) {
        this(context, null);
    }

    public UnPressableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void dispatchSetPressed(boolean pressed) {
    }
}
