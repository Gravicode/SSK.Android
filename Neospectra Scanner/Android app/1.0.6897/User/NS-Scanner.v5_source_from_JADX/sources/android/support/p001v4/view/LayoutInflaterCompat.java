package android.support.p001v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.LayoutInflater.Factory2;
import android.view.View;
import java.lang.reflect.Field;
import org.apache.commons.math3.geometry.VectorFormat;

/* renamed from: android.support.v4.view.LayoutInflaterCompat */
public final class LayoutInflaterCompat {
    static final LayoutInflaterCompatBaseImpl IMPL;
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;

    /* renamed from: android.support.v4.view.LayoutInflaterCompat$Factory2Wrapper */
    static class Factory2Wrapper implements Factory2 {
        final LayoutInflaterFactory mDelegateFactory;

        Factory2Wrapper(LayoutInflaterFactory delegateFactory) {
            this.mDelegateFactory = delegateFactory;
        }

        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return this.mDelegateFactory.onCreateView(null, name, context, attrs);
        }

        public View onCreateView(View parent, String name, Context context, AttributeSet attributeSet) {
            return this.mDelegateFactory.onCreateView(parent, name, context, attributeSet);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getName());
            sb.append(VectorFormat.DEFAULT_PREFIX);
            sb.append(this.mDelegateFactory);
            sb.append(VectorFormat.DEFAULT_SUFFIX);
            return sb.toString();
        }
    }

    @RequiresApi(21)
    /* renamed from: android.support.v4.view.LayoutInflaterCompat$LayoutInflaterCompatApi21Impl */
    static class LayoutInflaterCompatApi21Impl extends LayoutInflaterCompatBaseImpl {
        LayoutInflaterCompatApi21Impl() {
        }

        public void setFactory(LayoutInflater inflater, LayoutInflaterFactory factory) {
            inflater.setFactory2(factory != null ? new Factory2Wrapper(factory) : null);
        }

        public void setFactory2(LayoutInflater inflater, Factory2 factory) {
            inflater.setFactory2(factory);
        }
    }

    /* renamed from: android.support.v4.view.LayoutInflaterCompat$LayoutInflaterCompatBaseImpl */
    static class LayoutInflaterCompatBaseImpl {
        LayoutInflaterCompatBaseImpl() {
        }

        public void setFactory(LayoutInflater inflater, LayoutInflaterFactory factory) {
            setFactory2(inflater, factory != null ? new Factory2Wrapper(factory) : null);
        }

        public void setFactory2(LayoutInflater inflater, Factory2 factory) {
            inflater.setFactory2(factory);
            Factory f = inflater.getFactory();
            if (f instanceof Factory2) {
                LayoutInflaterCompat.forceSetFactory2(inflater, (Factory2) f);
            } else {
                LayoutInflaterCompat.forceSetFactory2(inflater, factory);
            }
        }

        public LayoutInflaterFactory getFactory(LayoutInflater inflater) {
            Factory factory = inflater.getFactory();
            if (factory instanceof Factory2Wrapper) {
                return ((Factory2Wrapper) factory).mDelegateFactory;
            }
            return null;
        }
    }

    static void forceSetFactory2(LayoutInflater inflater, Factory2 factory) {
        if (!sCheckedField) {
            try {
                sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
                sLayoutInflaterFactory2Field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
                sb.append(LayoutInflater.class.getName());
                sb.append("; inflation may have unexpected results.");
                Log.e(str, sb.toString(), e);
            }
            sCheckedField = true;
        }
        if (sLayoutInflaterFactory2Field != null) {
            try {
                sLayoutInflaterFactory2Field.set(inflater, factory);
            } catch (IllegalAccessException e2) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
                sb2.append(inflater);
                sb2.append("; inflation may have unexpected results.");
                Log.e(str2, sb2.toString(), e2);
            }
        }
    }

    static {
        if (VERSION.SDK_INT >= 21) {
            IMPL = new LayoutInflaterCompatApi21Impl();
        } else {
            IMPL = new LayoutInflaterCompatBaseImpl();
        }
    }

    private LayoutInflaterCompat() {
    }

    @Deprecated
    public static void setFactory(@NonNull LayoutInflater inflater, @NonNull LayoutInflaterFactory factory) {
        IMPL.setFactory(inflater, factory);
    }

    public static void setFactory2(@NonNull LayoutInflater inflater, @NonNull Factory2 factory) {
        IMPL.setFactory2(inflater, factory);
    }

    @Deprecated
    public static LayoutInflaterFactory getFactory(LayoutInflater inflater) {
        return IMPL.getFactory(inflater);
    }
}
