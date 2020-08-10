package android.support.v14.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p004v7.preference.AndroidResources;
import android.support.p004v7.preference.C1126R;
import android.support.p004v7.preference.PreferenceViewHolder;
import android.support.p004v7.preference.TwoStatePreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SwitchPreference extends TwoStatePreference {
    private final Listener mListener;
    private CharSequence mSwitchOff;
    private CharSequence mSwitchOn;

    private class Listener implements OnCheckedChangeListener {
        private Listener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!SwitchPreference.this.callChangeListener(Boolean.valueOf(isChecked))) {
                buttonView.setChecked(!isChecked);
            } else {
                SwitchPreference.this.setChecked(isChecked);
            }
        }
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mListener = new Listener();
        TypedArray a = context.obtainStyledAttributes(attrs, C1126R.styleable.SwitchPreference, defStyleAttr, defStyleRes);
        setSummaryOn((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreference_summaryOn, C1126R.styleable.SwitchPreference_android_summaryOn));
        setSummaryOff((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreference_summaryOff, C1126R.styleable.SwitchPreference_android_summaryOff));
        setSwitchTextOn((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreference_switchTextOn, C1126R.styleable.SwitchPreference_android_switchTextOn));
        setSwitchTextOff((CharSequence) TypedArrayUtils.getString(a, C1126R.styleable.SwitchPreference_switchTextOff, C1126R.styleable.SwitchPreference_android_switchTextOff));
        setDisableDependentsState(TypedArrayUtils.getBoolean(a, C1126R.styleable.SwitchPreference_disableDependentsState, C1126R.styleable.SwitchPreference_android_disableDependentsState, false));
        a.recycle();
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, C1126R.attr.switchPreferenceStyle, 16843629));
    }

    public SwitchPreference(Context context) {
        this(context, null);
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        syncSwitchView(holder.findViewById(AndroidResources.ANDROID_R_SWITCH_WIDGET));
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
            syncSwitchView(view.findViewById(AndroidResources.ANDROID_R_SWITCH_WIDGET));
            syncSummaryView(view.findViewById(16908304));
        }
    }

    private void syncSwitchView(View view) {
        if (view instanceof Switch) {
            ((Switch) view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.mChecked);
        }
        if (view instanceof Switch) {
            Switch switchView = (Switch) view;
            switchView.setTextOn(this.mSwitchOn);
            switchView.setTextOff(this.mSwitchOff);
            switchView.setOnCheckedChangeListener(this.mListener);
        }
    }
}
