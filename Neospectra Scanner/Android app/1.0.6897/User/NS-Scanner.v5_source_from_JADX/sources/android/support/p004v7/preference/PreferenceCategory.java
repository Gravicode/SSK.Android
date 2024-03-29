package android.support.p004v7.preference;

import android.content.Context;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import android.util.AttributeSet;

/* renamed from: android.support.v7.preference.PreferenceCategory */
public class PreferenceCategory extends PreferenceGroup {
    private static final String TAG = "PreferenceCategory";

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCategory(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, C1126R.attr.preferenceCategoryStyle, 16842892));
    }

    public PreferenceCategory(Context context) {
        this(context, null);
    }

    /* access modifiers changed from: protected */
    public boolean onPrepareAddPreference(Preference preference) {
        if (!(preference instanceof PreferenceCategory)) {
            return super.onPrepareAddPreference(preference);
        }
        throw new IllegalArgumentException("Cannot add a PreferenceCategory directly to a PreferenceCategory");
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean shouldDisableDependents() {
        return !super.isEnabled();
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(info);
        CollectionItemInfoCompat existingItemInfo = info.getCollectionItemInfo();
        if (existingItemInfo != null) {
            info.setCollectionItemInfo(CollectionItemInfoCompat.obtain(existingItemInfo.getRowIndex(), existingItemInfo.getRowSpan(), existingItemInfo.getColumnIndex(), existingItemInfo.getColumnSpan(), true, existingItemInfo.isSelected()));
        }
    }
}
