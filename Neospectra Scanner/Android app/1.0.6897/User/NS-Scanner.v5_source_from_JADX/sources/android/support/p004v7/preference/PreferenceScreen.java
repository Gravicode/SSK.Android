package android.support.p004v7.preference;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p004v7.preference.PreferenceManager.OnNavigateToScreenListener;
import android.util.AttributeSet;

/* renamed from: android.support.v7.preference.PreferenceScreen */
public final class PreferenceScreen extends PreferenceGroup {
    private boolean mShouldUseGeneratedIds = true;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public PreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs, TypedArrayUtils.getAttr(context, C1126R.attr.preferenceScreenStyle, 16842891));
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        if (getIntent() == null && getFragment() == null && getPreferenceCount() != 0) {
            OnNavigateToScreenListener listener = getPreferenceManager().getOnNavigateToScreenListener();
            if (listener != null) {
                listener.onNavigateToScreen(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isOnSameScreenAsChildren() {
        return false;
    }

    public boolean shouldUseGeneratedIds() {
        return this.mShouldUseGeneratedIds;
    }

    public void setShouldUseGeneratedIds(boolean shouldUseGeneratedIds) {
        if (isAttached()) {
            throw new IllegalStateException("Cannot change the usage of generated IDs while attached to the preference hierarchy");
        }
        this.mShouldUseGeneratedIds = shouldUseGeneratedIds;
    }
}
