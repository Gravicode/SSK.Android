package android.support.p001v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment.SavedState;
import android.support.p001v4.app.FragmentManager.BackStackEntry;
import android.support.p001v4.app.FragmentManager.FragmentLifecycleCallbacks;
import android.support.p001v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.p001v4.util.ArraySet;
import android.support.p001v4.util.DebugUtils;
import android.support.p001v4.util.LogWriter;
import android.support.p001v4.util.Pair;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: android.support.v4.app.FragmentManagerImpl */
/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager implements Factory2 {
    static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField = null;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded = new ArrayList<>();
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = new CopyOnWriteArrayList<>();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    boolean mStopped;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimateOnHWLayerIfNeededListener */
    /* compiled from: FragmentManager */
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper {
        View mView;

        AnimateOnHWLayerIfNeededListener(View v, AnimationListener listener) {
            super(listener);
            this.mView = v;
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (ViewCompat.isAttachedToWindow(this.mView) || VERSION.SDK_INT >= 24) {
                this.mView.post(new Runnable() {
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, null);
                    }
                });
            } else {
                this.mView.setLayerType(0, null);
            }
            super.onAnimationEnd(animation);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimationListenerWrapper */
    /* compiled from: FragmentManager */
    private static class AnimationListenerWrapper implements AnimationListener {
        private final AnimationListener mWrapped;

        private AnimationListenerWrapper(AnimationListener wrapped) {
            this.mWrapped = wrapped;
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationStart(animation);
            }
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationEnd(animation);
            }
        }

        @CallSuper
        public void onAnimationRepeat(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationRepeat(animation);
            }
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimationOrAnimator */
    /* compiled from: FragmentManager */
    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        private AnimationOrAnimator(Animation animation2) {
            this.animation = animation2;
            this.animator = null;
            if (animation2 == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }

        private AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimatorOnHWLayerIfNeededListener */
    /* compiled from: FragmentManager */
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
        View mView;

        AnimatorOnHWLayerIfNeededListener(View v) {
            this.mView = v;
        }

        public void onAnimationStart(Animator animation) {
            this.mView.setLayerType(2, null);
        }

        public void onAnimationEnd(Animator animation) {
            this.mView.setLayerType(0, null);
            animation.removeListener(this);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$EndViewTransitionAnimator */
    /* compiled from: FragmentManager */
    private static class EndViewTransitionAnimator extends AnimationSet implements Runnable {
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimator(@NonNull Animation animation, @NonNull ViewGroup parent, @NonNull View child) {
            super(false);
            this.mParent = parent;
            this.mChild = child;
            addAnimation(animation);
        }

        public boolean getTransformation(long currentTime, Transformation t) {
            if (this.mEnded) {
                return !this.mTransitionEnded;
            }
            if (!super.getTransformation(currentTime, t)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
            if (this.mEnded) {
                return !this.mTransitionEnded;
            }
            if (!super.getTransformation(currentTime, outTransformation, scale)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public void run() {
            this.mParent.endViewTransition(this.mChild);
            this.mTransitionEnded = true;
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$FragmentTag */
    /* compiled from: FragmentManager */
    static class FragmentTag {
        public static final int[] Fragment = {16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$OpGenerator */
    /* compiled from: FragmentManager */
    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$PopBackStackState */
    /* compiled from: FragmentManager */
    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        public boolean generateOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                FragmentManager childManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
                if (childManager != null && childManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$StartEnterTransitionListener */
    /* compiled from: FragmentManager */
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener {
        /* access modifiers changed from: private */
        public final boolean mIsBack;
        private int mNumPostponed;
        /* access modifiers changed from: private */
        public final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void completeTransaction() {
            boolean z = false;
            boolean canceled = this.mNumPostponed > 0;
            FragmentManagerImpl manager = this.mRecord.mManager;
            int numAdded = manager.mAdded.size();
            for (int i = 0; i < numAdded; i++) {
                Fragment fragment = (Fragment) manager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (canceled && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z2 = this.mIsBack;
            if (!canceled) {
                z = true;
            }
            fragmentManagerImpl.completeExecute(backStackRecord, z2, z, true);
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
    }

    FragmentManagerImpl() {
    }

    static boolean modifiesAlpha(AnimationOrAnimator anim) {
        if (anim.animation instanceof AlphaAnimation) {
            return true;
        }
        if (!(anim.animation instanceof AnimationSet)) {
            return modifiesAlpha(anim.animator);
        }
        List<Animation> anims = ((AnimationSet) anim.animation).getAnimations();
        for (int i = 0; i < anims.size(); i++) {
            if (anims.get(i) instanceof AlphaAnimation) {
                return true;
            }
        }
        return false;
    }

    static boolean modifiesAlpha(Animator anim) {
        if (anim == null) {
            return false;
        }
        if (anim instanceof ValueAnimator) {
            PropertyValuesHolder[] values = ((ValueAnimator) anim).getValues();
            for (PropertyValuesHolder propertyName : values) {
                if ("alpha".equals(propertyName.getPropertyName())) {
                    return true;
                }
            }
        } else if (anim instanceof AnimatorSet) {
            List<Animator> animList = ((AnimatorSet) anim).getChildAnimations();
            for (int i = 0; i < animList.size(); i++) {
                if (modifiesAlpha((Animator) animList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean shouldRunOnHWLayer(View v, AnimationOrAnimator anim) {
        boolean z = false;
        if (v == null || anim == null) {
            return false;
        }
        if (VERSION.SDK_INT >= 19 && v.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(v) && modifiesAlpha(anim)) {
            z = true;
        }
        return z;
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter pw = new PrintWriter(new LogWriter(TAG));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", null, pw, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, pw, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = execPendingActions();
        forcePostponedTransactions();
        return updates;
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate(null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        return popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad id: ");
            sb.append(id);
            throw new IllegalArgumentException(sb.toString());
        }
        enqueueAction(new PopBackStackState(null, id, flags), false);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        execPendingActions();
        if (id >= 0) {
            return popBackStackImmediate(null, id, flags);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(id);
        throw new IllegalArgumentException(sb.toString());
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        execPendingActions();
        ensureExecReady(true);
        if (this.mPrimaryNav != null && id < 0 && name == null) {
            FragmentManager childManager = this.mPrimaryNav.peekChildFragmentManager();
            if (childManager != null && childManager.popBackStackImmediate()) {
                return true;
            }
        }
        boolean executePop = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        burpActive();
        return executePop;
    }

    public int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return (BackStackEntry) this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(listener);
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(sb.toString()));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        int index = bundle.getInt(key, -1);
        if (index == -1) {
            return null;
        }
        Fragment f = (Fragment) this.mActive.get(index);
        if (f == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment no longer exists for key ");
            sb.append(key);
            sb.append(": index ");
            sb.append(index);
            throwException(new IllegalStateException(sb.toString()));
        }
        return f;
    }

    public List<Fragment> getFragments() {
        List<Fragment> list;
        if (this.mAdded.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    /* access modifiers changed from: 0000 */
    public List<Fragment> getActiveFragments() {
        if (this.mActive == null) {
            return null;
        }
        int count = this.mActive.size();
        ArrayList<Fragment> fragments = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            fragments.add(this.mActive.valueAt(i));
        }
        return fragments;
    }

    /* access modifiers changed from: 0000 */
    public int getActiveFragmentCount() {
        if (this.mActive == null) {
            return 0;
        }
        return this.mActive.size();
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(sb.toString()));
        }
        SavedState savedState = null;
        if (fragment.mState <= 0) {
            return null;
        }
        Bundle result = saveFragmentBasicState(fragment);
        if (result != null) {
            savedState = new SavedState(result);
        }
        return savedState;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("    ");
        String innerPrefix = sb.toString();
        if (this.mActive != null) {
            int N = this.mActive.size();
            if (N > 0) {
                writer.print(prefix);
                writer.print("Active Fragments in ");
                writer.print(Integer.toHexString(System.identityHashCode(this)));
                writer.println(":");
                for (int i = 0; i < N; i++) {
                    Fragment f = (Fragment) this.mActive.valueAt(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f);
                    if (f != null) {
                        f.dump(innerPrefix, fd, writer, args);
                    }
                }
            }
        }
        int N2 = this.mAdded.size();
        if (N2 > 0) {
            writer.print(prefix);
            writer.println("Added Fragments:");
            for (int i2 = 0; i2 < N2; i2++) {
                Fragment f2 = (Fragment) this.mAdded.get(i2);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i2);
                writer.print(": ");
                writer.println(f2.toString());
            }
        }
        if (this.mCreatedMenus != null) {
            int N3 = this.mCreatedMenus.size();
            if (N3 > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                for (int i3 = 0; i3 < N3; i3++) {
                    Fragment f3 = (Fragment) this.mCreatedMenus.get(i3);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i3);
                    writer.print(": ");
                    writer.println(f3.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            int N4 = this.mBackStack.size();
            if (N4 > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                for (int i4 = 0; i4 < N4; i4++) {
                    BackStackRecord bs = (BackStackRecord) this.mBackStack.get(i4);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i4);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                int N5 = this.mBackStackIndices.size();
                if (N5 > 0) {
                    writer.print(prefix);
                    writer.println("Back Stack Indices:");
                    for (int i5 = 0; i5 < N5; i5++) {
                        BackStackRecord bs2 = (BackStackRecord) this.mBackStackIndices.get(i5);
                        writer.print(prefix);
                        writer.print("  #");
                        writer.print(i5);
                        writer.print(": ");
                        writer.println(bs2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null) {
            int N6 = this.mPendingActions.size();
            if (N6 > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (int i6 = 0; i6 < N6; i6++) {
                    OpGenerator r = (OpGenerator) this.mPendingActions.get(i6);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i6);
                    writer.print(": ");
                    writer.println(r);
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(this.mNoTransactionsBecause);
        }
    }

    static AnimationOrAnimator makeOpenCloseAnimation(Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, 1, 0.5f, 1, 0.5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(220);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(220);
        set.addAnimation(alpha);
        return new AnimationOrAnimator((Animation) set);
    }

    static AnimationOrAnimator makeFadeAnimation(Context context, float start, float end) {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(220);
        return new AnimationOrAnimator((Animation) anim);
    }

    /* access modifiers changed from: 0000 */
    public AnimationOrAnimator loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        int nextAnim = fragment.getNextAnim();
        Animation animation = fragment.onCreateAnimation(transit, enter, nextAnim);
        if (animation != null) {
            return new AnimationOrAnimator(animation);
        }
        Animator animator = fragment.onCreateAnimator(transit, enter, nextAnim);
        if (animator != null) {
            return new AnimationOrAnimator(animator);
        }
        if (nextAnim != 0) {
            boolean isAnim = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean successfulLoad = false;
            if (isAnim) {
                try {
                    Animation animation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (animation2 != null) {
                        return new AnimationOrAnimator(animation2);
                    }
                    successfulLoad = true;
                } catch (NotFoundException e) {
                    throw e;
                } catch (RuntimeException e2) {
                }
            }
            if (!successfulLoad) {
                try {
                    Animator animator2 = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (animator2 != null) {
                        return new AnimationOrAnimator(animator2);
                    }
                } catch (RuntimeException e3) {
                    if (isAnim) {
                        throw e3;
                    }
                    Animation animation3 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (animation3 != null) {
                        return new AnimationOrAnimator(animation3);
                    }
                }
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        switch (styleIndex) {
            case 1:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            default:
                if (transitionStyle == 0 && this.mHost.onHasWindowAnimations()) {
                    transitionStyle = this.mHost.onGetWindowAnimations();
                }
                return transitionStyle == 0 ? null : null;
        }
    }

    public void performPendingDeferredStart(Fragment f) {
        if (f.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            f.mDeferStart = false;
            moveToState(f, this.mCurState, 0, 0, false);
        }
    }

    private static void setHWLayerAnimListenerIfAlpha(View v, AnimationOrAnimator anim) {
        if (!(v == null || anim == null || !shouldRunOnHWLayer(v, anim))) {
            if (anim.animator != null) {
                anim.animator.addListener(new AnimatorOnHWLayerIfNeededListener(v));
            } else {
                AnimationListener originalListener = getAnimationListener(anim.animation);
                v.setLayerType(2, null);
                anim.animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(v, originalListener));
            }
        }
    }

    private static AnimationListener getAnimationListener(Animation animation) {
        try {
            if (sAnimationListenerField == null) {
                sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                sAnimationListenerField.setAccessible(true);
            }
            return (AnimationListener) sAnimationListenerField.get(animation);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "No field with the name mListener is found in Animation class", e);
            return null;
        } catch (IllegalAccessException e2) {
            Log.e(TAG, "Cannot access Animation's mListener field", e2);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0213, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0214, code lost:
        r2 = r0;
        r2 = android.support.p001v4.p003os.EnvironmentCompat.MEDIA_UNKNOWN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x032a, code lost:
        if (r12 >= 4) goto L_0x034c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x032e, code lost:
        if (DEBUG == false) goto L_0x0346;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x0330, code lost:
        r1 = TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("movefrom STARTED: ");
        r2.append(r8);
        android.util.Log.v(r1, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x0346, code lost:
        r17.performStop();
        dispatchOnFragmentStopped(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x034c, code lost:
        if (r12 >= 3) goto L_0x036b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x0350, code lost:
        if (DEBUG == false) goto L_0x0368;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x0352, code lost:
        r1 = TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("movefrom STOPPED: ");
        r2.append(r8);
        android.util.Log.v(r1, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x0368, code lost:
        r17.performReallyStop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x036b, code lost:
        if (r12 >= 2) goto L_0x03f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x036f, code lost:
        if (DEBUG == false) goto L_0x0387;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x0371, code lost:
        r1 = TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("movefrom ACTIVITY_CREATED: ");
        r2.append(r8);
        android.util.Log.v(r1, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x0389, code lost:
        if (r8.mView == null) goto L_0x039a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0391, code lost:
        if (r7.mHost.onShouldSaveFragmentState(r8) == false) goto L_0x039a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x0395, code lost:
        if (r8.mSavedViewState != null) goto L_0x039a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x0397, code lost:
        saveFragmentViewState(r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x039a, code lost:
        r17.performDestroyView();
        dispatchOnFragmentViewDestroyed(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x03a2, code lost:
        if (r8.mView == null) goto L_0x03e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x03a6, code lost:
        if (r8.mContainer == null) goto L_0x03e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x03a8, code lost:
        r8.mContainer.endViewTransition(r8.mView);
        r8.mView.clearAnimation();
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x03b8, code lost:
        if (r7.mCurState <= 0) goto L_0x03d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x03bc, code lost:
        if (r7.mDestroyed != false) goto L_0x03d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x03c4, code lost:
        if (r8.mView.getVisibility() != 0) goto L_0x03d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x03ca, code lost:
        if (r8.mPostponedAlpha < 0.0f) goto L_0x03d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x03cc, code lost:
        r1 = loadAnimation(r8, r19, false, r20);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x03d5, code lost:
        r2 = r19;
        r4 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x03d9, code lost:
        r8.mPostponedAlpha = 0.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x03db, code lost:
        if (r1 == null) goto L_0x03e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x03dd, code lost:
        animateRemoveFragment(r8, r1, r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x03e0, code lost:
        r8.mContainer.removeView(r8.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x03e8, code lost:
        r2 = r19;
        r4 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x03ec, code lost:
        r8.mContainer = null;
        r8.mView = null;
        r8.mInnerView = null;
        r8.mInLayout = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x03f9, code lost:
        if (r12 >= 1) goto L_0x046f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x03fd, code lost:
        if (r7.mDestroyed == false) goto L_0x0420;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:0x0403, code lost:
        if (r17.getAnimatingAway() == null) goto L_0x0410;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x0405, code lost:
        r1 = r17.getAnimatingAway();
        r8.setAnimatingAway(null);
        r1.clearAnimation();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x0414, code lost:
        if (r17.getAnimator() == null) goto L_0x0420;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:195:0x0416, code lost:
        r1 = r17.getAnimator();
        r8.setAnimator(null);
        r1.cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x0424, code lost:
        if (r17.getAnimatingAway() != null) goto L_0x046b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x042a, code lost:
        if (r17.getAnimator() == null) goto L_0x042d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x042f, code lost:
        if (DEBUG == false) goto L_0x0447;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x0431, code lost:
        r1 = TAG;
        r3 = new java.lang.StringBuilder();
        r3.append("movefrom CREATED: ");
        r3.append(r8);
        android.util.Log.v(r1, r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:204:0x0449, code lost:
        if (r8.mRetaining != false) goto L_0x0452;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:205:0x044b, code lost:
        r17.performDestroy();
        dispatchOnFragmentDestroyed(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:0x0452, code lost:
        r8.mState = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:207:0x0454, code lost:
        r17.performDetach();
        dispatchOnFragmentDetached(r8, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x045a, code lost:
        if (r21 != false) goto L_0x046f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x045e, code lost:
        if (r8.mRetaining != false) goto L_0x0464;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:211:0x0460, code lost:
        makeInactive(r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:212:0x0464, code lost:
        r8.mHost = null;
        r8.mParentFragment = null;
        r8.mFragmentManager = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:213:0x046b, code lost:
        r8.setStateAfterAnimating(r12);
        r12 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01ac, code lost:
        ensureInflatedFragmentView(r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01af, code lost:
        if (r12 <= 1) goto L_0x02ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01b3, code lost:
        if (DEBUG == false) goto L_0x01cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01b5, code lost:
        r1 = TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("moveto ACTIVITY_CREATED: ");
        r2.append(r8);
        android.util.Log.v(r1, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01cd, code lost:
        if (r8.mFromLayout != false) goto L_0x0296;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01cf, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01d2, code lost:
        if (r8.mContainerId == 0) goto L_0x0246;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01d7, code lost:
        if (r8.mContainerId != -1) goto L_0x01f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01d9, code lost:
        r3 = new java.lang.StringBuilder();
        r3.append("Cannot create fragment ");
        r3.append(r8);
        r3.append(" for a container view with no id");
        throwException(new java.lang.IllegalArgumentException(r3.toString()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x01f7, code lost:
        r1 = (android.view.ViewGroup) r7.mContainer.onFindViewById(r8.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0202, code lost:
        if (r1 != null) goto L_0x0246;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0206, code lost:
        if (r8.mRestored != false) goto L_0x0246;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:?, code lost:
        r2 = r17.getResources().getResourceName(r8.mContainerId);
     */
    /* JADX WARNING: Removed duplicated region for block: B:216:0x0473  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(android.support.p001v4.app.Fragment r17, int r18, int r19, int r20, boolean r21) {
        /*
            r16 = this;
            r7 = r16
            r8 = r17
            boolean r1 = r8.mAdded
            r9 = 1
            if (r1 == 0) goto L_0x0011
            boolean r1 = r8.mDetached
            if (r1 == 0) goto L_0x000e
            goto L_0x0011
        L_0x000e:
            r1 = r18
            goto L_0x0016
        L_0x0011:
            r1 = r18
            if (r1 <= r9) goto L_0x0016
            r1 = 1
        L_0x0016:
            boolean r2 = r8.mRemoving
            if (r2 == 0) goto L_0x002c
            int r2 = r8.mState
            if (r1 <= r2) goto L_0x002c
            int r2 = r8.mState
            if (r2 != 0) goto L_0x002a
            boolean r2 = r17.isInBackStack()
            if (r2 == 0) goto L_0x002a
            r1 = 1
            goto L_0x002c
        L_0x002a:
            int r1 = r8.mState
        L_0x002c:
            boolean r2 = r8.mDeferStart
            r10 = 4
            r11 = 3
            if (r2 == 0) goto L_0x0039
            int r2 = r8.mState
            if (r2 >= r10) goto L_0x0039
            if (r1 <= r11) goto L_0x0039
            r1 = 3
        L_0x0039:
            r12 = r1
            int r1 = r8.mState
            r13 = 2
            r14 = 0
            r15 = 0
            if (r1 > r12) goto L_0x02fd
            boolean r1 = r8.mFromLayout
            if (r1 == 0) goto L_0x004a
            boolean r1 = r8.mInLayout
            if (r1 != 0) goto L_0x004a
            return
        L_0x004a:
            android.view.View r1 = r17.getAnimatingAway()
            if (r1 != 0) goto L_0x0056
            android.animation.Animator r1 = r17.getAnimator()
            if (r1 == 0) goto L_0x0068
        L_0x0056:
            r8.setAnimatingAway(r14)
            r8.setAnimator(r14)
            int r3 = r17.getStateAfterAnimating()
            r4 = 0
            r5 = 0
            r6 = 1
            r1 = r7
            r2 = r8
            r1.moveToState(r2, r3, r4, r5, r6)
        L_0x0068:
            int r1 = r8.mState
            switch(r1) {
                case 0: goto L_0x006f;
                case 1: goto L_0x01ac;
                case 2: goto L_0x02ab;
                case 3: goto L_0x02af;
                case 4: goto L_0x02d1;
                default: goto L_0x006d;
            }
        L_0x006d:
            goto L_0x02f7
        L_0x006f:
            if (r12 <= 0) goto L_0x01ac
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x008b
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto CREATED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x008b:
            android.os.Bundle r1 = r8.mSavedFragmentState
            if (r1 == 0) goto L_0x00e2
            android.os.Bundle r1 = r8.mSavedFragmentState
            android.support.v4.app.FragmentHostCallback r2 = r7.mHost
            android.content.Context r2 = r2.getContext()
            java.lang.ClassLoader r2 = r2.getClassLoader()
            r1.setClassLoader(r2)
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:view_state"
            android.util.SparseArray r1 = r1.getSparseParcelableArray(r2)
            r8.mSavedViewState = r1
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:target_state"
            android.support.v4.app.Fragment r1 = r7.getFragment(r1, r2)
            r8.mTarget = r1
            android.support.v4.app.Fragment r1 = r8.mTarget
            if (r1 == 0) goto L_0x00c0
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:target_req_state"
            int r1 = r1.getInt(r2, r15)
            r8.mTargetRequestCode = r1
        L_0x00c0:
            java.lang.Boolean r1 = r8.mSavedUserVisibleHint
            if (r1 == 0) goto L_0x00cf
            java.lang.Boolean r1 = r8.mSavedUserVisibleHint
            boolean r1 = r1.booleanValue()
            r8.mUserVisibleHint = r1
            r8.mSavedUserVisibleHint = r14
            goto L_0x00d9
        L_0x00cf:
            android.os.Bundle r1 = r8.mSavedFragmentState
            java.lang.String r2 = "android:user_visible_hint"
            boolean r1 = r1.getBoolean(r2, r9)
            r8.mUserVisibleHint = r1
        L_0x00d9:
            boolean r1 = r8.mUserVisibleHint
            if (r1 != 0) goto L_0x00e2
            r8.mDeferStart = r9
            if (r12 <= r11) goto L_0x00e2
            r12 = 3
        L_0x00e2:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            r8.mHost = r1
            android.support.v4.app.Fragment r1 = r7.mParent
            r8.mParentFragment = r1
            android.support.v4.app.Fragment r1 = r7.mParent
            if (r1 == 0) goto L_0x00f3
            android.support.v4.app.Fragment r1 = r7.mParent
            android.support.v4.app.FragmentManagerImpl r1 = r1.mChildFragmentManager
            goto L_0x00f9
        L_0x00f3:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.support.v4.app.FragmentManagerImpl r1 = r1.getFragmentManagerImpl()
        L_0x00f9:
            r8.mFragmentManager = r1
            android.support.v4.app.Fragment r1 = r8.mTarget
            if (r1 == 0) goto L_0x0143
            android.util.SparseArray<android.support.v4.app.Fragment> r1 = r7.mActive
            android.support.v4.app.Fragment r2 = r8.mTarget
            int r2 = r2.mIndex
            java.lang.Object r1 = r1.get(r2)
            android.support.v4.app.Fragment r2 = r8.mTarget
            if (r1 == r2) goto L_0x0133
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Fragment "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = " declared target fragment "
            r2.append(r3)
            android.support.v4.app.Fragment r3 = r8.mTarget
            r2.append(r3)
            java.lang.String r3 = " that does not belong to this FragmentManager!"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0133:
            android.support.v4.app.Fragment r1 = r8.mTarget
            int r1 = r1.mState
            if (r1 >= r9) goto L_0x0143
            android.support.v4.app.Fragment r2 = r8.mTarget
            r3 = 1
            r4 = 0
            r5 = 0
            r6 = 1
            r1 = r7
            r1.moveToState(r2, r3, r4, r5, r6)
        L_0x0143:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r7.dispatchOnFragmentPreAttached(r8, r1, r15)
            r8.mCalled = r15
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r8.onAttach(r1)
            boolean r1 = r8.mCalled
            if (r1 != 0) goto L_0x0177
            android.support.v4.app.SuperNotCalledException r1 = new android.support.v4.app.SuperNotCalledException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Fragment "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = " did not call through to super.onAttach()"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0177:
            android.support.v4.app.Fragment r1 = r8.mParentFragment
            if (r1 != 0) goto L_0x0181
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            r1.onAttachFragment(r8)
            goto L_0x0186
        L_0x0181:
            android.support.v4.app.Fragment r1 = r8.mParentFragment
            r1.onAttachFragment(r8)
        L_0x0186:
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            android.content.Context r1 = r1.getContext()
            r7.dispatchOnFragmentAttached(r8, r1, r15)
            boolean r1 = r8.mIsCreated
            if (r1 != 0) goto L_0x01a3
            android.os.Bundle r1 = r8.mSavedFragmentState
            r7.dispatchOnFragmentPreCreated(r8, r1, r15)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.performCreate(r1)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r7.dispatchOnFragmentCreated(r8, r1, r15)
            goto L_0x01aa
        L_0x01a3:
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.restoreChildFragmentState(r1)
            r8.mState = r9
        L_0x01aa:
            r8.mRetaining = r15
        L_0x01ac:
            r16.ensureInflatedFragmentView(r17)
            if (r12 <= r9) goto L_0x02ab
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x01cb
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto ACTIVITY_CREATED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x01cb:
            boolean r1 = r8.mFromLayout
            if (r1 != 0) goto L_0x0296
            r1 = 0
            int r2 = r8.mContainerId
            if (r2 == 0) goto L_0x0246
            int r2 = r8.mContainerId
            r3 = -1
            if (r2 != r3) goto L_0x01f7
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Cannot create fragment "
            r3.append(r4)
            r3.append(r8)
            java.lang.String r4 = " for a container view with no id"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            r7.throwException(r2)
        L_0x01f7:
            android.support.v4.app.FragmentContainer r2 = r7.mContainer
            int r3 = r8.mContainerId
            android.view.View r2 = r2.onFindViewById(r3)
            r1 = r2
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            if (r1 != 0) goto L_0x0246
            boolean r2 = r8.mRestored
            if (r2 != 0) goto L_0x0246
            android.content.res.Resources r2 = r17.getResources()     // Catch:{ NotFoundException -> 0x0213 }
            int r3 = r8.mContainerId     // Catch:{ NotFoundException -> 0x0213 }
            java.lang.String r2 = r2.getResourceName(r3)     // Catch:{ NotFoundException -> 0x0213 }
            goto L_0x0217
        L_0x0213:
            r0 = move-exception
            r2 = r0
            java.lang.String r2 = "unknown"
        L_0x0217:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "No view found for id 0x"
            r4.append(r5)
            int r5 = r8.mContainerId
            java.lang.String r5 = java.lang.Integer.toHexString(r5)
            r4.append(r5)
            java.lang.String r5 = " ("
            r4.append(r5)
            r4.append(r2)
            java.lang.String r5 = ") for fragment "
            r4.append(r5)
            r4.append(r8)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            r7.throwException(r3)
        L_0x0246:
            r8.mContainer = r1
            android.os.Bundle r2 = r8.mSavedFragmentState
            android.view.LayoutInflater r2 = r8.performGetLayoutInflater(r2)
            android.os.Bundle r3 = r8.mSavedFragmentState
            android.view.View r2 = r8.performCreateView(r2, r1, r3)
            r8.mView = r2
            android.view.View r2 = r8.mView
            if (r2 == 0) goto L_0x0294
            android.view.View r2 = r8.mView
            r8.mInnerView = r2
            android.view.View r2 = r8.mView
            r2.setSaveFromParentEnabled(r15)
            if (r1 == 0) goto L_0x026a
            android.view.View r2 = r8.mView
            r1.addView(r2)
        L_0x026a:
            boolean r2 = r8.mHidden
            if (r2 == 0) goto L_0x0275
            android.view.View r2 = r8.mView
            r3 = 8
            r2.setVisibility(r3)
        L_0x0275:
            android.view.View r2 = r8.mView
            android.os.Bundle r3 = r8.mSavedFragmentState
            r8.onViewCreated(r2, r3)
            android.view.View r2 = r8.mView
            android.os.Bundle r3 = r8.mSavedFragmentState
            r7.dispatchOnFragmentViewCreated(r8, r2, r3, r15)
            android.view.View r2 = r8.mView
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x0290
            android.view.ViewGroup r2 = r8.mContainer
            if (r2 == 0) goto L_0x0290
            goto L_0x0291
        L_0x0290:
            r9 = 0
        L_0x0291:
            r8.mIsNewlyAdded = r9
            goto L_0x0296
        L_0x0294:
            r8.mInnerView = r14
        L_0x0296:
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.performActivityCreated(r1)
            android.os.Bundle r1 = r8.mSavedFragmentState
            r7.dispatchOnFragmentActivityCreated(r8, r1, r15)
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x02a9
            android.os.Bundle r1 = r8.mSavedFragmentState
            r8.restoreViewState(r1)
        L_0x02a9:
            r8.mSavedFragmentState = r14
        L_0x02ab:
            if (r12 <= r13) goto L_0x02af
            r8.mState = r11
        L_0x02af:
            if (r12 <= r11) goto L_0x02d1
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x02cb
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto STARTED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x02cb:
            r17.performStart()
            r7.dispatchOnFragmentStarted(r8, r15)
        L_0x02d1:
            if (r12 <= r10) goto L_0x02f7
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x02ed
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "moveto RESUMED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x02ed:
            r17.performResume()
            r7.dispatchOnFragmentResumed(r8, r15)
            r8.mSavedFragmentState = r14
            r8.mSavedViewState = r14
        L_0x02f7:
            r2 = r19
            r4 = r20
            goto L_0x046f
        L_0x02fd:
            int r1 = r8.mState
            if (r1 <= r12) goto L_0x02f7
            int r1 = r8.mState
            switch(r1) {
                case 1: goto L_0x03f5;
                case 2: goto L_0x036b;
                case 3: goto L_0x034c;
                case 4: goto L_0x032a;
                case 5: goto L_0x0307;
                default: goto L_0x0306;
            }
        L_0x0306:
            goto L_0x02f7
        L_0x0307:
            r1 = 5
            if (r12 >= r1) goto L_0x032a
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0324
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom RESUMED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0324:
            r17.performPause()
            r7.dispatchOnFragmentPaused(r8, r15)
        L_0x032a:
            if (r12 >= r10) goto L_0x034c
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0346
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom STARTED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0346:
            r17.performStop()
            r7.dispatchOnFragmentStopped(r8, r15)
        L_0x034c:
            if (r12 >= r11) goto L_0x036b
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0368
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom STOPPED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0368:
            r17.performReallyStop()
        L_0x036b:
            if (r12 >= r13) goto L_0x03f5
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0387
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "movefrom ACTIVITY_CREATED: "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            android.util.Log.v(r1, r2)
        L_0x0387:
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x039a
            android.support.v4.app.FragmentHostCallback r1 = r7.mHost
            boolean r1 = r1.onShouldSaveFragmentState(r8)
            if (r1 == 0) goto L_0x039a
            android.util.SparseArray<android.os.Parcelable> r1 = r8.mSavedViewState
            if (r1 != 0) goto L_0x039a
            r16.saveFragmentViewState(r17)
        L_0x039a:
            r17.performDestroyView()
            r7.dispatchOnFragmentViewDestroyed(r8, r15)
            android.view.View r1 = r8.mView
            if (r1 == 0) goto L_0x03e8
            android.view.ViewGroup r1 = r8.mContainer
            if (r1 == 0) goto L_0x03e8
            android.view.ViewGroup r1 = r8.mContainer
            android.view.View r2 = r8.mView
            r1.endViewTransition(r2)
            android.view.View r1 = r8.mView
            r1.clearAnimation()
            r1 = 0
            int r2 = r7.mCurState
            r3 = 0
            if (r2 <= 0) goto L_0x03d5
            boolean r2 = r7.mDestroyed
            if (r2 != 0) goto L_0x03d5
            android.view.View r2 = r8.mView
            int r2 = r2.getVisibility()
            if (r2 != 0) goto L_0x03d5
            float r2 = r8.mPostponedAlpha
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x03d5
            r2 = r19
            r4 = r20
            android.support.v4.app.FragmentManagerImpl$AnimationOrAnimator r1 = r7.loadAnimation(r8, r2, r15, r4)
            goto L_0x03d9
        L_0x03d5:
            r2 = r19
            r4 = r20
        L_0x03d9:
            r8.mPostponedAlpha = r3
            if (r1 == 0) goto L_0x03e0
            r7.animateRemoveFragment(r8, r1, r12)
        L_0x03e0:
            android.view.ViewGroup r3 = r8.mContainer
            android.view.View r5 = r8.mView
            r3.removeView(r5)
            goto L_0x03ec
        L_0x03e8:
            r2 = r19
            r4 = r20
        L_0x03ec:
            r8.mContainer = r14
            r8.mView = r14
            r8.mInnerView = r14
            r8.mInLayout = r15
            goto L_0x03f9
        L_0x03f5:
            r2 = r19
            r4 = r20
        L_0x03f9:
            if (r12 >= r9) goto L_0x046f
            boolean r1 = r7.mDestroyed
            if (r1 == 0) goto L_0x0420
            android.view.View r1 = r17.getAnimatingAway()
            if (r1 == 0) goto L_0x0410
            android.view.View r1 = r17.getAnimatingAway()
            r8.setAnimatingAway(r14)
            r1.clearAnimation()
            goto L_0x0420
        L_0x0410:
            android.animation.Animator r1 = r17.getAnimator()
            if (r1 == 0) goto L_0x0420
            android.animation.Animator r1 = r17.getAnimator()
            r8.setAnimator(r14)
            r1.cancel()
        L_0x0420:
            android.view.View r1 = r17.getAnimatingAway()
            if (r1 != 0) goto L_0x046b
            android.animation.Animator r1 = r17.getAnimator()
            if (r1 == 0) goto L_0x042d
            goto L_0x046b
        L_0x042d:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x0447
            java.lang.String r1 = "FragmentManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "movefrom CREATED: "
            r3.append(r5)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.v(r1, r3)
        L_0x0447:
            boolean r1 = r8.mRetaining
            if (r1 != 0) goto L_0x0452
            r17.performDestroy()
            r7.dispatchOnFragmentDestroyed(r8, r15)
            goto L_0x0454
        L_0x0452:
            r8.mState = r15
        L_0x0454:
            r17.performDetach()
            r7.dispatchOnFragmentDetached(r8, r15)
            if (r21 != 0) goto L_0x046f
            boolean r3 = r8.mRetaining
            if (r3 != 0) goto L_0x0464
            r16.makeInactive(r17)
            goto L_0x046f
        L_0x0464:
            r8.mHost = r14
            r8.mParentFragment = r14
            r8.mFragmentManager = r14
            goto L_0x046f
        L_0x046b:
            r8.setStateAfterAnimating(r12)
            r12 = 1
        L_0x046f:
            int r3 = r8.mState
            if (r3 == r12) goto L_0x04a2
            java.lang.String r3 = "FragmentManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "moveToState: Fragment state for "
            r5.append(r6)
            r5.append(r8)
            java.lang.String r6 = " not updated inline; "
            r5.append(r6)
            java.lang.String r6 = "expected state "
            r5.append(r6)
            r5.append(r12)
            java.lang.String r6 = " found "
            r5.append(r6)
            int r6 = r8.mState
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            android.util.Log.w(r3, r5)
            r8.mState = r12
        L_0x04a2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull AnimationOrAnimator anim, int newState) {
        final View viewToAnimate = fragment.mView;
        final ViewGroup container = fragment.mContainer;
        container.startViewTransition(viewToAnimate);
        fragment.setStateAfterAnimating(newState);
        if (anim.animation != null) {
            Animation animation = new EndViewTransitionAnimator(anim.animation, container, viewToAnimate);
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener(new AnimationListenerWrapper(getAnimationListener(animation)) {
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    container.post(new Runnable() {
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway(null);
                                FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                            }
                        }
                    });
                }
            });
            setHWLayerAnimListenerIfAlpha(viewToAnimate, anim);
            fragment.mView.startAnimation(animation);
            return;
        }
        Animator animator = anim.animator;
        fragment.setAnimator(anim.animator);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                container.endViewTransition(viewToAnimate);
                Animator animator = fragment.getAnimator();
                fragment.setAnimator(null);
                if (animator != null && container.indexOfChild(viewToAnimate) < 0) {
                    FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget(fragment.mView);
        setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
        animator.start();
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, false);
    }

    /* access modifiers changed from: 0000 */
    public void ensureInflatedFragmentView(Fragment f) {
        if (f.mFromLayout && !f.mPerformedCreateView) {
            f.mView = f.performCreateView(f.performGetLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
            if (f.mView != null) {
                f.mInnerView = f.mView;
                f.mView.setSaveFromParentEnabled(false);
                if (f.mHidden) {
                    f.mView.setVisibility(8);
                }
                f.onViewCreated(f.mView, f.mSavedFragmentState);
                dispatchOnFragmentViewCreated(f, f.mView, f.mSavedFragmentState, false);
                return;
            }
            f.mInnerView = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator anim = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (anim == null || anim.animator == null) {
                if (anim != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    fragment.mView.startAnimation(anim.animation);
                    anim.animation.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                anim.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup container = fragment.mContainer;
                    final View animatingView = fragment.mView;
                    container.startViewTransition(animatingView);
                    anim.animator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            container.endViewTransition(animatingView);
                            animation.removeListener(this);
                            if (fragment.mView != null) {
                                fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                anim.animator.start();
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /* access modifiers changed from: 0000 */
    public void moveFragmentToExpectedState(Fragment f) {
        if (f != null) {
            int nextState = this.mCurState;
            if (f.mRemoving) {
                if (f.isInBackStack()) {
                    nextState = Math.min(nextState, 1);
                } else {
                    nextState = Math.min(nextState, 0);
                }
            }
            moveToState(f, nextState, f.getNextTransition(), f.getNextTransitionStyle(), false);
            if (f.mView != null) {
                Fragment underFragment = findFragmentUnder(f);
                if (underFragment != null) {
                    View underView = underFragment.mView;
                    ViewGroup container = f.mContainer;
                    int underIndex = container.indexOfChild(underView);
                    int viewIndex = container.indexOfChild(f.mView);
                    if (viewIndex < underIndex) {
                        container.removeViewAt(viewIndex);
                        container.addView(f.mView, underIndex);
                    }
                }
                if (f.mIsNewlyAdded && f.mContainer != null) {
                    if (f.mPostponedAlpha > 0.0f) {
                        f.mView.setAlpha(f.mPostponedAlpha);
                    }
                    f.mPostponedAlpha = 0.0f;
                    f.mIsNewlyAdded = false;
                    AnimationOrAnimator anim = loadAnimation(f, f.getNextTransition(), true, f.getNextTransitionStyle());
                    if (anim != null) {
                        setHWLayerAnimListenerIfAlpha(f.mView, anim);
                        if (anim.animation != null) {
                            f.mView.startAnimation(anim.animation);
                        } else {
                            anim.animator.setTarget(f.mView);
                            anim.animator.start();
                        }
                    }
                }
            }
            if (f.mHiddenChanged) {
                completeShowHideFragment(f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(int newState, boolean always) {
        if (this.mHost == null && newState != 0) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            if (this.mActive != null) {
                int numAdded = this.mAdded.size();
                for (int i = 0; i < numAdded; i++) {
                    moveFragmentToExpectedState((Fragment) this.mAdded.get(i));
                }
                int numActive = this.mActive.size();
                for (int i2 = 0; i2 < numActive; i2++) {
                    Fragment f = (Fragment) this.mActive.valueAt(i2);
                    if (f != null && ((f.mRemoving || f.mDetached) && !f.mIsNewlyAdded)) {
                        moveFragmentToExpectedState(f);
                    }
                }
                startPendingDeferredFragments();
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeActive(Fragment f) {
        if (f.mIndex < 0) {
            int i = this.mNextFragmentIndex;
            this.mNextFragmentIndex = i + 1;
            f.setIndex(i, this.mParent);
            if (this.mActive == null) {
                this.mActive = new SparseArray<>();
            }
            this.mActive.put(f.mIndex, f);
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Allocated fragment index ");
                sb.append(f);
                Log.v(str, sb.toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeInactive(Fragment f) {
        if (f.mIndex >= 0) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Freeing fragment index ");
                sb.append(f);
                Log.v(str, sb.toString());
            }
            this.mActive.put(f.mIndex, null);
            f.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Fragment already added: ");
            sb2.append(fragment);
            throw new IllegalStateException(sb2.toString());
        }
        synchronized (this.mAdded) {
            this.mAdded.add(fragment);
        }
        fragment.mAdded = true;
        fragment.mRemoving = false;
        if (fragment.mView == null) {
            fragment.mHiddenChanged = false;
        }
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        if (moveToStateNow) {
            moveToState(fragment);
        }
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v(str, sb.toString());
        }
        boolean inactive = !fragment.isInBackStack();
        if (!fragment.mDetached || inactive) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        }
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (DEBUG) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("remove from detach: ");
                    sb2.append(fragment);
                    Log.v(str2, sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (this.mAdded.contains(fragment)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Fragment already added: ");
                sb2.append(fragment);
                throw new IllegalStateException(sb2.toString());
            }
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("add from attach: ");
                sb3.append(fragment);
                Log.v(str2, sb3.toString());
            }
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
        }
    }

    public Fragment findFragmentById(int id) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.mFragmentId == id) {
                return f;
            }
        }
        if (this.mActive != null) {
            for (int i2 = this.mActive.size() - 1; i2 >= 0; i2--) {
                Fragment f2 = (Fragment) this.mActive.valueAt(i2);
                if (f2 != null && f2.mFragmentId == id) {
                    return f2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        if (tag != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if (!(this.mActive == null || tag == null)) {
            for (int i2 = this.mActive.size() - 1; i2 >= 0; i2--) {
                Fragment f2 = (Fragment) this.mActive.valueAt(i2);
                if (f2 != null && tag.equals(f2.mTag)) {
                    return f2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        if (!(this.mActive == null || who == null)) {
            for (int i = this.mActive.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (f != null) {
                    Fragment findFragmentByWho = f.findFragmentByWho(who);
                    Fragment f2 = findFragmentByWho;
                    if (findFragmentByWho != null) {
                        return f2;
                    }
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can not perform this action inside of ");
            sb.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(sb.toString());
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed) {
                if (this.mHost != null) {
                    if (this.mPendingActions == null) {
                        this.mPendingActions = new ArrayList<>();
                    }
                    this.mPendingActions.add(action);
                    scheduleCommit();
                    return;
                }
            }
            if (!allowStateLoss) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    /* access modifiers changed from: private */
    public void scheduleCommit() {
        synchronized (this) {
            boolean pendingReady = false;
            boolean postponeReady = this.mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                pendingReady = true;
            }
            if (postponeReady || pendingReady) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null) {
                if (this.mAvailBackStackIndices.size() > 0) {
                    int index = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                    if (DEBUG) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Adding back stack index ");
                        sb.append(index);
                        sb.append(" with ");
                        sb.append(bse);
                        Log.v(str, sb.toString());
                    }
                    this.mBackStackIndices.set(index, bse);
                    return index;
                }
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int index2 = this.mBackStackIndices.size();
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting back stack index ");
                sb2.append(index2);
                sb2.append(" to ");
                sb2.append(bse);
                Log.v(str2, sb2.toString());
            }
            this.mBackStackIndices.add(bse);
            return index2;
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int N = this.mBackStackIndices.size();
            if (index < N) {
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Setting back stack index ");
                    sb.append(index);
                    sb.append(" to ");
                    sb.append(bse);
                    Log.v(str, sb.toString());
                }
                this.mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<>();
                    }
                    if (DEBUG) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding available back stack index ");
                        sb2.append(N);
                        Log.v(str2, sb2.toString());
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(N));
                    N++;
                }
                if (DEBUG) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Adding back stack index ");
                    sb3.append(index);
                    sb3.append(" with ");
                    sb3.append(bse);
                    Log.v(str3, sb3.toString());
                }
                this.mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<>();
            }
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Freeing back stack index ");
                sb.append(index);
                Log.v(str, sb.toString());
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost == null) {
            throw new IllegalStateException("Fragment host has been destroyed");
        } else if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList<>();
                this.mTmpIsPop = new ArrayList<>();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction(null, null);
            } finally {
                this.mExecutingActions = false;
            }
        }
    }

    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            doPendingDeferredStart();
            burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    /* JADX INFO: finally extract failed */
    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean didSomething = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                didSomething = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        doPendingDeferredStart();
        burpActive();
        return didSomething;
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        int numPostponed = this.mPostponedTransactions == null ? 0 : this.mPostponedTransactions.size();
        int i = 0;
        while (i < numPostponed) {
            StartEnterTransitionListener listener = (StartEnterTransitionListener) this.mPostponedTransactions.get(i);
            if (records != null && !listener.mIsBack) {
                int index = records.indexOf(listener.mRecord);
                if (index != -1 && ((Boolean) isRecordPop.get(index)).booleanValue()) {
                    listener.cancelTransaction();
                    i++;
                }
            }
            if (listener.isReady() != 0 || (records != null && listener.mRecord.interactsWith(records, 0, records.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                if (records != null && !listener.mIsBack) {
                    int indexOf = records.indexOf(listener.mRecord);
                    int index2 = indexOf;
                    if (indexOf != -1 && ((Boolean) isRecordPop.get(index2)).booleanValue()) {
                        listener.cancelTransaction();
                    }
                }
                listener.completeTransaction();
            }
            i++;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        if (records != null && !records.isEmpty()) {
            if (isRecordPop == null || records.size() != isRecordPop.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(records, isRecordPop);
            int numRecords = records.size();
            int startIndex = 0;
            int recordNum = 0;
            while (recordNum < numRecords) {
                if (!((BackStackRecord) records.get(recordNum)).mReorderingAllowed) {
                    if (startIndex != recordNum) {
                        executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                    }
                    int reorderingEnd = recordNum + 1;
                    if (((Boolean) isRecordPop.get(recordNum)).booleanValue()) {
                        while (reorderingEnd < numRecords && ((Boolean) isRecordPop.get(reorderingEnd)).booleanValue() && !((BackStackRecord) records.get(reorderingEnd)).mReorderingAllowed) {
                            reorderingEnd++;
                        }
                    }
                    executeOpsTogether(records, isRecordPop, recordNum, reorderingEnd);
                    startIndex = reorderingEnd;
                    recordNum = reorderingEnd - 1;
                }
                recordNum++;
            }
            if (startIndex != numRecords) {
                executeOpsTogether(records, isRecordPop, startIndex, numRecords);
            }
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        Fragment oldPrimaryNav;
        ArrayList<BackStackRecord> arrayList = records;
        ArrayList<Boolean> arrayList2 = isRecordPop;
        int i = startIndex;
        int i2 = endIndex;
        boolean allowReordering = ((BackStackRecord) arrayList.get(i)).mReorderingAllowed;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            this.mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        boolean addToBackStack = false;
        Fragment oldPrimaryNav2 = getPrimaryNavigationFragment();
        int recordNum = i;
        while (true) {
            boolean z = true;
            if (recordNum >= i2) {
                break;
            }
            BackStackRecord record = (BackStackRecord) arrayList.get(recordNum);
            if (!((Boolean) arrayList2.get(recordNum)).booleanValue()) {
                oldPrimaryNav = record.expandOps(this.mTmpAddedFragments, oldPrimaryNav2);
            } else {
                oldPrimaryNav = record.trackAddedFragmentsInPop(this.mTmpAddedFragments, oldPrimaryNav2);
            }
            oldPrimaryNav2 = oldPrimaryNav;
            if (!addToBackStack && !record.mAddToBackStack) {
                z = false;
            }
            addToBackStack = z;
            recordNum++;
        }
        this.mTmpAddedFragments.clear();
        if (!allowReordering) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, i, i2, false);
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        int postponeIndex = i2;
        if (allowReordering) {
            ArraySet arraySet = new ArraySet();
            addAddedFragments(arraySet);
            ArraySet arraySet2 = arraySet;
            int postponeIndex2 = postponePostponableTransactions(arrayList, arrayList2, i, i2, arraySet);
            makeRemovedFragmentsInvisible(arraySet2);
            postponeIndex = postponeIndex2;
        }
        if (postponeIndex != i && allowReordering) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, i, postponeIndex, true);
            moveToState(this.mCurState, true);
        }
        for (int recordNum2 = i; recordNum2 < i2; recordNum2++) {
            BackStackRecord record2 = (BackStackRecord) arrayList.get(recordNum2);
            if (((Boolean) arrayList2.get(recordNum2)).booleanValue() && record2.mIndex >= 0) {
                freeBackStackIndex(record2.mIndex);
                record2.mIndex = -1;
            }
            record2.runOnCommitRunnables();
        }
        if (addToBackStack) {
            reportBackStackChanged();
        }
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> fragments) {
        int numAdded = fragments.size();
        for (int i = 0; i < numAdded; i++) {
            Fragment fragment = (Fragment) fragments.valueAt(i);
            if (!fragment.mAdded) {
                View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, ArraySet<Fragment> added) {
        int postponeIndex = endIndex;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            boolean isPop = ((Boolean) isRecordPop.get(i)).booleanValue();
            if (record.isPostponed() && !record.interactsWith(records, i + 1, endIndex)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener listener = new StartEnterTransitionListener(record, isPop);
                this.mPostponedTransactions.add(listener);
                record.setOnStartPostponedListener(listener);
                if (isPop) {
                    record.executeOps();
                } else {
                    record.executePopOps(false);
                }
                postponeIndex--;
                if (i != postponeIndex) {
                    records.remove(i);
                    records.add(postponeIndex, record);
                }
                addAddedFragments(added);
            }
        }
        return postponeIndex;
    }

    /* access modifiers changed from: private */
    public void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        if (isPop) {
            record.executePopOps(moveToState);
        } else {
            record.executeOps();
        }
        ArrayList<BackStackRecord> records = new ArrayList<>(1);
        ArrayList arrayList = new ArrayList(1);
        records.add(record);
        arrayList.add(Boolean.valueOf(isPop));
        if (runTransitions) {
            FragmentTransition.startTransitions(this, records, arrayList, 0, 1, true);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        }
        if (this.mActive != null) {
            int numActive = this.mActive.size();
            for (int i = 0; i < numActive; i++) {
                Fragment fragment = (Fragment) this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && record.interactsWith(fragment.mContainerId)) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    if (moveToState) {
                        fragment.mPostponedAlpha = 0.0f;
                    } else {
                        fragment.mPostponedAlpha = -1.0f;
                        fragment.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }

    private Fragment findFragmentUnder(Fragment f) {
        ViewGroup container = f.mContainer;
        View view = f.mView;
        if (container == null || view == null) {
            return null;
        }
        for (int i = this.mAdded.indexOf(f) - 1; i >= 0; i--) {
            Fragment underFragment = (Fragment) this.mAdded.get(i);
            if (underFragment.mContainer == container && underFragment.mView != null) {
                return underFragment;
            }
        }
        return null;
    }

    private static void executeOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            boolean moveToState = true;
            if (((Boolean) isRecordPop.get(i)).booleanValue()) {
                record.bumpBackStackNesting(-1);
                if (i != endIndex - 1) {
                    moveToState = false;
                }
                record.executePopOps(moveToState);
            } else {
                record.bumpBackStackNesting(1);
                record.executeOps();
            }
        }
    }

    private void addAddedFragments(ArraySet<Fragment> added) {
        if (this.mCurState >= 1) {
            int state = Math.min(this.mCurState, 4);
            int numAdded = this.mAdded.size();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < numAdded) {
                    Fragment fragment = (Fragment) this.mAdded.get(i2);
                    if (fragment.mState < state) {
                        moveToState(fragment, state, fragment.getNextAnim(), fragment.getNextTransition(), false);
                        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                            added.add(fragment);
                        }
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                ((StartEnterTransitionListener) this.mPostponedTransactions.remove(0)).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        int numFragments = this.mActive == null ? 0 : this.mActive.size();
        for (int i = 0; i < numFragments; i++) {
            Fragment fragment = (Fragment) this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    int stateAfterAnimating = fragment.getStateAfterAnimating();
                    View animatingAway = fragment.getAnimatingAway();
                    Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    fragment.setAnimatingAway(null);
                    moveToState(fragment, stateAfterAnimating, 0, 0, false);
                } else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003c, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean generateOpsForPendingActions(java.util.ArrayList<android.support.p001v4.app.BackStackRecord> r5, java.util.ArrayList<java.lang.Boolean> r6) {
        /*
            r4 = this;
            r0 = 0
            monitor-enter(r4)
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r1 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            r2 = 0
            if (r1 == 0) goto L_0x003b
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r1 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            int r1 = r1.size()     // Catch:{ all -> 0x003d }
            if (r1 != 0) goto L_0x0010
            goto L_0x003b
        L_0x0010:
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r1 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            int r1 = r1.size()     // Catch:{ all -> 0x003d }
        L_0x0017:
            if (r2 >= r1) goto L_0x0029
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r3 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x003d }
            android.support.v4.app.FragmentManagerImpl$OpGenerator r3 = (android.support.p001v4.app.FragmentManagerImpl.OpGenerator) r3     // Catch:{ all -> 0x003d }
            boolean r3 = r3.generateOps(r5, r6)     // Catch:{ all -> 0x003d }
            r0 = r0 | r3
            int r2 = r2 + 1
            goto L_0x0017
        L_0x0029:
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r2 = r4.mPendingActions     // Catch:{ all -> 0x003d }
            r2.clear()     // Catch:{ all -> 0x003d }
            android.support.v4.app.FragmentHostCallback r2 = r4.mHost     // Catch:{ all -> 0x003d }
            android.os.Handler r2 = r2.getHandler()     // Catch:{ all -> 0x003d }
            java.lang.Runnable r3 = r4.mExecCommit     // Catch:{ all -> 0x003d }
            r2.removeCallbacks(r3)     // Catch:{ all -> 0x003d }
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            return r0
        L_0x003b:
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            return r2
        L_0x003d:
            r1 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.app.FragmentManagerImpl.generateOpsForPendingActions(java.util.ArrayList, java.util.ArrayList):boolean");
    }

    /* access modifiers changed from: 0000 */
    public void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    /* access modifiers changed from: 0000 */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(state);
    }

    /* access modifiers changed from: 0000 */
    public boolean popBackStackState(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, String name, int id, int flags) {
        if (this.mBackStack == null) {
            return false;
        }
        if (name == null && id < 0 && (flags & 1) == 0) {
            int last = this.mBackStack.size() - 1;
            if (last < 0) {
                return false;
            }
            records.add(this.mBackStack.remove(last));
            isRecordPop.add(Boolean.valueOf(true));
        } else {
            int index = -1;
            if (name != null || id >= 0) {
                int index2 = this.mBackStack.size() - 1;
                while (index >= 0) {
                    BackStackRecord bss = (BackStackRecord) this.mBackStack.get(index);
                    if ((name != null && name.equals(bss.getName())) || (id >= 0 && id == bss.mIndex)) {
                        break;
                    }
                    index2 = index - 1;
                }
                if (index < 0) {
                    return false;
                }
                if ((flags & 1) != 0) {
                    index--;
                    while (index >= 0) {
                        BackStackRecord bss2 = (BackStackRecord) this.mBackStack.get(index);
                        if ((name == null || !name.equals(bss2.getName())) && (id < 0 || id != bss2.mIndex)) {
                            break;
                        }
                        index--;
                    }
                }
            }
            if (index == this.mBackStack.size() - 1) {
                return false;
            }
            for (int i = this.mBackStack.size() - 1; i > index; i--) {
                records.add(this.mBackStack.remove(i));
                isRecordPop.add(Boolean.valueOf(true));
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    private static void setRetaining(FragmentManagerNonConfig nonConfig) {
        if (nonConfig != null) {
            List<Fragment> fragments = nonConfig.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fragment.mRetaining = true;
                }
            }
            List<FragmentManagerNonConfig> children = nonConfig.getChildNonConfigs();
            if (children != null) {
                for (FragmentManagerNonConfig child : children) {
                    setRetaining(child);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void saveNonConfig() {
        FragmentManagerNonConfig child;
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        ArrayList arrayList3 = null;
        if (this.mActive != null) {
            ArrayList arrayList4 = null;
            ArrayList arrayList5 = null;
            ArrayList arrayList6 = null;
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (f != null) {
                    if (f.mRetainInstance) {
                        if (arrayList6 == null) {
                            arrayList6 = new ArrayList();
                        }
                        arrayList6.add(f);
                        f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                        if (DEBUG) {
                            String str = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("retainNonConfig: keeping retained ");
                            sb.append(f);
                            Log.v(str, sb.toString());
                        }
                    }
                    if (f.mChildFragmentManager != null) {
                        f.mChildFragmentManager.saveNonConfig();
                        child = f.mChildFragmentManager.mSavedNonConfig;
                    } else {
                        child = f.mChildNonConfig;
                    }
                    if (arrayList5 == null && child != null) {
                        arrayList5 = new ArrayList(this.mActive.size());
                        for (int j = 0; j < i; j++) {
                            arrayList5.add(null);
                        }
                    }
                    if (arrayList5 != null) {
                        arrayList5.add(child);
                    }
                    if (arrayList4 == null && f.mViewModelStore != null) {
                        arrayList4 = new ArrayList(this.mActive.size());
                        for (int j2 = 0; j2 < i; j2++) {
                            arrayList4.add(null);
                        }
                    }
                    if (arrayList4 != null) {
                        arrayList4.add(f.mViewModelStore);
                    }
                }
            }
            arrayList = arrayList6;
            arrayList2 = arrayList5;
            arrayList3 = arrayList4;
        }
        if (arrayList == null && arrayList2 == null && arrayList3 == null) {
            this.mSavedNonConfig = null;
        } else {
            this.mSavedNonConfig = new FragmentManagerNonConfig(arrayList, arrayList2, arrayList3);
        }
    }

    /* access modifiers changed from: 0000 */
    public void saveFragmentViewState(Fragment f) {
        if (f.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray<>();
            } else {
                this.mStateArray.clear();
            }
            f.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(f, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public Parcelable saveAllState() {
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int N = this.mActive.size();
        FragmentState[] active = new FragmentState[N];
        boolean haveFragments = false;
        for (int i = 0; i < N; i++) {
            Fragment f = (Fragment) this.mActive.valueAt(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failure saving state: active ");
                    sb.append(f);
                    sb.append(" has cleared index: ");
                    sb.append(f.mIndex);
                    throwException(new IllegalStateException(sb.toString()));
                }
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active[i] = fs;
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failure saving state: ");
                            sb2.append(f);
                            sb2.append(" has target not in fragment manager: ");
                            sb2.append(f.mTarget);
                            throwException(new IllegalStateException(sb2.toString()));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Saved state of ");
                    sb3.append(f);
                    sb3.append(": ");
                    sb3.append(fs.mSavedFragmentState);
                    Log.v(str, sb3.toString());
                }
            }
        }
        if (!haveFragments) {
            if (DEBUG) {
                Log.v(TAG, "saveAllState: no fragments!");
            }
            return null;
        }
        int[] added = null;
        BackStackState[] backStack = null;
        int N2 = this.mAdded.size();
        if (N2 > 0) {
            added = new int[N2];
            for (int i2 = 0; i2 < N2; i2++) {
                added[i2] = ((Fragment) this.mAdded.get(i2)).mIndex;
                if (added[i2] < 0) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failure saving state: active ");
                    sb4.append(this.mAdded.get(i2));
                    sb4.append(" has cleared index: ");
                    sb4.append(added[i2]);
                    throwException(new IllegalStateException(sb4.toString()));
                }
                if (DEBUG) {
                    String str2 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveAllState: adding fragment #");
                    sb5.append(i2);
                    sb5.append(": ");
                    sb5.append(this.mAdded.get(i2));
                    Log.v(str2, sb5.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            int N3 = this.mBackStack.size();
            if (N3 > 0) {
                backStack = new BackStackState[N3];
                for (int i3 = 0; i3 < N3; i3++) {
                    backStack[i3] = new BackStackState((BackStackRecord) this.mBackStack.get(i3));
                    if (DEBUG) {
                        String str3 = TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("saveAllState: adding back stack #");
                        sb6.append(i3);
                        sb6.append(": ");
                        sb6.append(this.mBackStack.get(i3));
                        Log.v(str3, sb6.toString());
                    }
                }
            }
        }
        FragmentManagerState fms = new FragmentManagerState();
        fms.mActive = active;
        fms.mAdded = added;
        fms.mBackStack = backStack;
        if (this.mPrimaryNav != null) {
            fms.mPrimaryNavActiveIndex = this.mPrimaryNav.mIndex;
        }
        fms.mNextFragmentIndex = this.mNextFragmentIndex;
        saveNonConfig();
        return fms;
    }

    /* access modifiers changed from: 0000 */
    public void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                List<FragmentManagerNonConfig> childNonConfigs = null;
                List<ViewModelStore> viewModelStores = null;
                if (nonConfig != null) {
                    List<Fragment> nonConfigFragments = nonConfig.getFragments();
                    childNonConfigs = nonConfig.getChildNonConfigs();
                    viewModelStores = nonConfig.getViewModelStores();
                    int count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (int i = 0; i < count; i++) {
                        Fragment f = (Fragment) nonConfigFragments.get(i);
                        if (DEBUG) {
                            String str = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("restoreAllState: re-attaching retained ");
                            sb.append(f);
                            Log.v(str, sb.toString());
                        }
                        int index = 0;
                        while (index < fms.mActive.length && fms.mActive[index].mIndex != f.mIndex) {
                            index++;
                        }
                        if (index == fms.mActive.length) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Could not find active fragment with index ");
                            sb2.append(f.mIndex);
                            throwException(new IllegalStateException(sb2.toString()));
                        }
                        FragmentState fs = fms.mActive[index];
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = false;
                        f.mAdded = false;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            f.mSavedFragmentState = fs.mSavedFragmentState;
                        }
                    }
                }
                this.mActive = new SparseArray<>(fms.mActive.length);
                for (int i2 = 0; i2 < fms.mActive.length; i2++) {
                    FragmentState fs2 = fms.mActive[i2];
                    if (fs2 != null) {
                        FragmentManagerNonConfig childNonConfig = null;
                        if (childNonConfigs != null && i2 < childNonConfigs.size()) {
                            childNonConfig = (FragmentManagerNonConfig) childNonConfigs.get(i2);
                        }
                        FragmentManagerNonConfig childNonConfig2 = childNonConfig;
                        ViewModelStore viewModelStore = null;
                        if (viewModelStores != null && i2 < viewModelStores.size()) {
                            viewModelStore = (ViewModelStore) viewModelStores.get(i2);
                        }
                        Fragment f2 = fs2.instantiate(this.mHost, this.mContainer, this.mParent, childNonConfig2, viewModelStore);
                        if (DEBUG) {
                            String str2 = TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("restoreAllState: active #");
                            sb3.append(i2);
                            sb3.append(": ");
                            sb3.append(f2);
                            Log.v(str2, sb3.toString());
                        }
                        this.mActive.put(f2.mIndex, f2);
                        fs2.mInstance = null;
                    }
                }
                if (nonConfig != null) {
                    List<Fragment> nonConfigFragments2 = nonConfig.getFragments();
                    int count2 = nonConfigFragments2 != null ? nonConfigFragments2.size() : 0;
                    for (int i3 = 0; i3 < count2; i3++) {
                        Fragment f3 = (Fragment) nonConfigFragments2.get(i3);
                        if (f3.mTargetIndex >= 0) {
                            f3.mTarget = (Fragment) this.mActive.get(f3.mTargetIndex);
                            if (f3.mTarget == null) {
                                String str3 = TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("Re-attaching retained fragment ");
                                sb4.append(f3);
                                sb4.append(" target no longer exists: ");
                                sb4.append(f3.mTargetIndex);
                                Log.w(str3, sb4.toString());
                            }
                        }
                    }
                }
                this.mAdded.clear();
                if (fms.mAdded != null) {
                    for (int i4 = 0; i4 < fms.mAdded.length; i4++) {
                        Fragment f4 = (Fragment) this.mActive.get(fms.mAdded[i4]);
                        if (f4 == null) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("No instantiated fragment for index #");
                            sb5.append(fms.mAdded[i4]);
                            throwException(new IllegalStateException(sb5.toString()));
                        }
                        f4.mAdded = true;
                        if (DEBUG) {
                            String str4 = TAG;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("restoreAllState: added #");
                            sb6.append(i4);
                            sb6.append(": ");
                            sb6.append(f4);
                            Log.v(str4, sb6.toString());
                        }
                        if (this.mAdded.contains(f4)) {
                            throw new IllegalStateException("Already added!");
                        }
                        synchronized (this.mAdded) {
                            try {
                                this.mAdded.add(f4);
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fms.mBackStack.length);
                    for (int i5 = 0; i5 < fms.mBackStack.length; i5++) {
                        BackStackRecord bse = fms.mBackStack[i5].instantiate(this);
                        if (DEBUG) {
                            String str5 = TAG;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("restoreAllState: back stack #");
                            sb7.append(i5);
                            sb7.append(" (index ");
                            sb7.append(bse.mIndex);
                            sb7.append("): ");
                            sb7.append(bse);
                            Log.v(str5, sb7.toString());
                            PrintWriter pw = new PrintWriter(new LogWriter(TAG));
                            bse.dump("  ", pw, false);
                            pw.close();
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                    }
                } else {
                    this.mBackStack = null;
                }
                if (fms.mPrimaryNavActiveIndex >= 0) {
                    this.mPrimaryNav = (Fragment) this.mActive.get(fms.mPrimaryNavActiveIndex);
                }
                this.mNextFragmentIndex = fms.mNextFragmentIndex;
            }
        }
    }

    private void burpActive() {
        if (this.mActive != null) {
            for (int i = this.mActive.size() - 1; i >= 0; i--) {
                if (this.mActive.valueAt(i) == null) {
                    this.mActive.delete(this.mActive.keyAt(i));
                }
            }
        }
    }

    public void attachController(FragmentHostCallback host, FragmentContainer container, Fragment parent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = host;
        this.mContainer = container;
        this.mParent = parent;
    }

    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        this.mStopped = false;
        int addedCount = this.mAdded.size();
        for (int i = 0; i < addedCount; i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(1);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(2);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(4);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(5);
    }

    public void dispatchPause() {
        dispatchStateChange(4);
    }

    public void dispatchStop() {
        this.mStopped = true;
        dispatchStateChange(3);
    }

    public void dispatchReallyStop() {
        dispatchStateChange(2);
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    /* JADX INFO: finally extract failed */
    private void dispatchStateChange(int nextState) {
        try {
            this.mExecutingActions = true;
            moveToState(nextState, false);
            this.mExecutingActions = false;
            execPendingActions();
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performConfigurationChanged(newConfig);
            }
        }
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performLowMemory();
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int i = 0;
        if (this.mCurState < 1) {
            return false;
        }
        ArrayList<Fragment> newMenus = null;
        boolean show = false;
        for (int i2 = 0; i2 < this.mAdded.size(); i2++) {
            Fragment f = (Fragment) this.mAdded.get(i2);
            if (f != null && f.performCreateOptionsMenu(menu, inflater)) {
                show = true;
                if (newMenus == null) {
                    newMenus = new ArrayList<>();
                }
                newMenus.add(f);
            }
        }
        if (this.mCreatedMenus != null) {
            while (true) {
                int i3 = i;
                if (i3 >= this.mCreatedMenus.size()) {
                    break;
                }
                Fragment f2 = (Fragment) this.mCreatedMenus.get(i3);
                if (newMenus == null || !newMenus.contains(f2)) {
                    f2.onDestroyOptionsMenu();
                }
                i = i3 + 1;
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean show = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performPrepareOptionsMenu(menu)) {
                show = true;
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState >= 1) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment f) {
        if (f == null || (this.mActive.get(f.mIndex) == f && (f.mHost == null || f.getFragmentManager() == this))) {
            this.mPrimaryNav = f;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(f);
        sb.append(" is not an active fragment of FragmentManager ");
        sb.append(this);
        throw new IllegalArgumentException(sb.toString());
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacks.add(new Pair(cb, Boolean.valueOf(recursive)));
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            int N = this.mLifecycleCallbacks.size();
            while (true) {
                if (i >= N) {
                    break;
                } else if (((Pair) this.mLifecycleCallbacks.get(i)).first == cb) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPreAttached(Fragment f, Context context, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreAttached(f, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentPreAttached(this, f, context);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentAttached(Fragment f, Context context, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentAttached(f, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentAttached(this, f, context);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPreCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreCreated(f, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentPreCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentCreated(f, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentActivityCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentActivityCreated(f, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentActivityCreated(this, f, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentViewCreated(Fragment f, View v, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewCreated(f, v, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentViewCreated(this, f, v, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentStarted(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStarted(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentStarted(this, f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentResumed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentResumed(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentResumed(this, f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPaused(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPaused(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentPaused(this, f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentStopped(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStopped(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentStopped(this, f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentSaveInstanceState(Fragment f, Bundle outState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentSaveInstanceState(f, outState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentSaveInstanceState(this, f, outState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentViewDestroyed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewDestroyed(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentViewDestroyed(this, f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentDestroyed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDestroyed(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentDestroyed(this, f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentDetached(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDetached(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentDetached(this, f);
            }
        }
    }

    public static int reverseTransit(int transit) {
        if (transit == 4097) {
            return 8194;
        }
        if (transit == 4099) {
            return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
        }
        if (transit != 8194) {
            return 0;
        }
        return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        if (transit == 4097) {
            return enter ? 1 : 2;
        } else if (transit == 4099) {
            return enter ? 5 : 6;
        } else if (transit != 8194) {
            return -1;
        } else {
            return enter ? 3 : 4;
        }
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Fragment fragment;
        Fragment fragment2;
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        if (!"fragment".equals(name)) {
            return null;
        }
        String fname = attributeSet.getAttributeValue(null, "class");
        TypedArray a = context2.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
        int i = 0;
        if (fname == null) {
            fname = a.getString(0);
        }
        String fname2 = fname;
        int id = a.getResourceId(1, -1);
        String tag = a.getString(2);
        a.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), fname2)) {
            return null;
        }
        if (parent != null) {
            i = parent.getId();
        }
        int containerId = i;
        if (containerId == -1 && id == -1 && tag == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(attrs.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(fname2);
            throw new IllegalArgumentException(sb.toString());
        }
        Fragment fragment3 = id != -1 ? findFragmentById(id) : null;
        if (fragment3 == null && tag != null) {
            fragment3 = findFragmentByTag(tag);
        }
        if (fragment3 == null && containerId != -1) {
            fragment3 = findFragmentById(containerId);
        }
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(id));
            sb2.append(" fname=");
            sb2.append(fname2);
            sb2.append(" existing=");
            sb2.append(fragment3);
            Log.v(str, sb2.toString());
        }
        if (fragment3 == null) {
            Fragment fragment4 = this.mContainer.instantiate(context2, fname2, null);
            fragment4.mFromLayout = true;
            fragment4.mFragmentId = id != 0 ? id : containerId;
            fragment4.mContainerId = containerId;
            fragment4.mTag = tag;
            fragment4.mInLayout = true;
            fragment4.mFragmentManager = this;
            fragment4.mHost = this.mHost;
            fragment4.onInflate(this.mHost.getContext(), attributeSet, fragment4.mSavedFragmentState);
            addFragment(fragment4, true);
            fragment = fragment4;
        } else if (fragment3.mInLayout) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(attrs.getPositionDescription());
            sb3.append(": Duplicate id 0x");
            sb3.append(Integer.toHexString(id));
            sb3.append(", tag ");
            sb3.append(tag);
            sb3.append(", or parent id 0x");
            sb3.append(Integer.toHexString(containerId));
            sb3.append(" with another fragment for ");
            sb3.append(fname2);
            throw new IllegalArgumentException(sb3.toString());
        } else {
            fragment3.mInLayout = true;
            fragment3.mHost = this.mHost;
            if (!fragment3.mRetaining) {
                fragment3.onInflate(this.mHost.getContext(), attributeSet, fragment3.mSavedFragmentState);
            }
            fragment = fragment3;
        }
        if (this.mCurState >= 1 || !fragment.mFromLayout) {
            fragment2 = fragment;
            moveToState(fragment2);
        } else {
            fragment2 = fragment;
            moveToState(fragment, 1, 0, 0, false);
        }
        if (fragment2.mView == null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Fragment ");
            sb4.append(fname2);
            sb4.append(" did not create a view.");
            throw new IllegalStateException(sb4.toString());
        }
        if (id != 0) {
            fragment2.mView.setId(id);
        }
        if (fragment2.mView.getTag() == null) {
            fragment2.mView.setTag(tag);
        }
        return fragment2.mView;
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    /* access modifiers changed from: 0000 */
    public Factory2 getLayoutInflaterFactory() {
        return this;
    }
}
