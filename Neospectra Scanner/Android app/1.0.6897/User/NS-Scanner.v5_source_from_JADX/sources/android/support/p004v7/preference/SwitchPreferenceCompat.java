package android.support.p004v7.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p004v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/* renamed from: android.support.v7.preference.SwitchPreferenceCompat */
public class SwitchPreferenceCompat extends TwoStatePreference {
    private final Listener mListener;
    private CharSequence mSwitchOff;
    private CharSequence mSwitchOn;

    /* renamed from: android.support.v7.preference.SwitchPreferenceCompat$Listener */
    private class Listener implements OnCheckedChangeListener {
        private Listener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!SwitchPreferenceCompat.this.callChangeListener(Boolean.valueOf(isChecked))) {
                buttonView.setChecked(!isChecked);
            } else {
                SwitchPreferenceCompat.this.setChecked(isChecked);
            }
        }
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mListener = new Listener();
        TypedArray a = context.obtainStyledAttributes(attrs, C1126R.styleable.SwitchPreferenceCompat, defStyleAttr, defStyleRes);
        setSummaryOn((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreferenceCompat_summaryOn, C1126R.styleable.SwitchPreferenceCompat_android_summaryOn));
        setSummaryOff((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreferenceCompat_summaryOff, C1126R.styleable.SwitchPreferenceCompat_android_summaryOff));
        setSwitchTextOn((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreferenceCompat_switchTextOn, C1126R.styleable.SwitchPreferenceCompat_android_switchTextOn));
        setSwitchTextOff((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreferenceCompat_switchTextOff, C1126R.styleable.SwitchPreferenceCompat_android_switchTextOff));
        setDisableDependentsState(TypedArrayUtils.getBoolean(a, C1126R.styleable.SwitchPreferenceCompat_disableDependentsState, C1126R.styleable.SwitchPreferenceCompat_android_disableDependentsState, false));
        a.recycle();
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attrs) {
        this(context, attrs, C1126R.attr.switchPreferenceCompatStyle);
    }

    public SwitchPreferenceCompat(Context context) {
        this(context, null);
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        syncSwitchView(holder.findViewById(C1126R.C1128id.switchWidget));
        syncSummaryView(holder);
    }

    public void setSwitchTextOn(CharSequence onText) {
        this.mSwitchOn = onText;
        notifyChanged();
    }

    public void setSwitchTextOff(CharSequence offText) {
        this.mSwitchOff = offText;
        notifyChanged();
    }

    public void setSwitchTextOn(int resId) {
        setSwitchTextOn((CharSequence) getContext().getString(resId));
    }

    public void setSwitchTextOff(int resId) {
        setSwitchTextOff((CharSequence) getContext().getString(resId));
    }

    public CharSequence getSwitchTextOn() {
        return this.mSwitchOn;
    }

    public CharSequence getSwitchTextOff() {
        return this.mSwitchOff;
    }

    /* access modifiers changed from: protected */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void performClick(View view) {
        super.performClick(view);
        syncViewIfAccessibilityEnabled(view);
    }

    private void syncViewIfAccessibilityEnabled(View view) {
        if (((AccessibilityManager) getContext().getSystemService("accessibility")).isEnabled()) {
            syncSwitchView(view.findViewById(C1126R.C1128id.switchWidget));
            syncSummaryView(view.findViewById(16908304));
        }
    }

    private void syncSwitchView(View view) {
        if (view instanceof SwitchCompat) {
            ((SwitchCompat) view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.mChecked);
        }
        if (view instanceof SwitchCompat) {
            SwitchCompat switchView = (SwitchCompat) view;
            switchView.setTextOn(this.mSwitchOn);
            switchView.setTextOff(this.mSwitchOff);
            switchView.setOnCheckedChangeListener(this.mListener);
        }
    }
}
