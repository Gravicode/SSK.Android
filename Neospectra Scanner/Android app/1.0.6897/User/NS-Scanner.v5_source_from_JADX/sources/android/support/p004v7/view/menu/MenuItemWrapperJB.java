package android.support.p004v7.view.menu;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.internal.view.SupportMenuItem;
import android.support.p001v4.view.ActionProvider;
import android.view.ActionProvider.VisibilityListener;
import android.view.MenuItem;
import android.view.View;

@RequiresApi(16)
@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.view.menu.MenuItemWrapperJB */
class MenuItemWrapperJB extends MenuItemWrapperICS {

    /* renamed from: android.support.v7.view.menu.MenuItemWrapperJB$ActionProviderWrapperJB */
    class ActionProviderWrapperJB extends ActionProviderWrapper implements VisibilityListener {
        ActionProvider.VisibilityListener mListener;

        public ActionProviderWrapperJB(Context context, android.view.ActionProvider inner) {
            super(context, inner);
        }

        public View onCreateActionView(MenuItem forItem) {
            return this.mInner.onCreateActionView(forItem);
        }

        public boolean overridesItemVisibility() {
            return this.mInner.overridesItemVisibility();
        }

        public boolean isVisible() {
            return this.mInner.isVisible();
        }

        public void refreshVisibility() {
            this.mInner.refreshVisibility();
        }

        public void setVisibilityListener(ActionProvider.VisibilityListener listener) {
            this.mListener = listener;
            this.mInner.setVisibilityListener(listener != null ? this : null);
        }

        public void onActionProviderVisibilityChanged(boolean isVisible) {
            if (this.mListener != null) {
                this.mListener.onActionProviderVisibilityChanged(isVisible);
            }
        }
    }

    MenuItemWrapperJB(Context context, SupportMenuItem object) {
        super(context, object);
    }

    /* access modifiers changed from: 0000 */
    public ActionProviderWrapper createActionProviderWrapper(android.view.ActionProvider provider) {
        return new ActionProviderWrapperJB(this.mContext, provider);
    }
}
