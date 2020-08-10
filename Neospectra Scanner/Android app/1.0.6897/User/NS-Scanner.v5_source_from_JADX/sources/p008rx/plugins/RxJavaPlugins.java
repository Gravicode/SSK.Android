package p008rx.plugins;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.annotations.Experimental;

/* renamed from: rx.plugins.RxJavaPlugins */
public class RxJavaPlugins {
    static final RxJavaErrorHandler DEFAULT_ERROR_HANDLER = new RxJavaErrorHandler() {
    };
    private static final RxJavaPlugins INSTANCE = new RxJavaPlugins();
    private final AtomicReference<RxJavaCompletableExecutionHook> completableExecutionHook = new AtomicReference<>();
    private final AtomicReference<RxJavaErrorHandler> errorHandler = new AtomicReference<>();
    private final AtomicReference<RxJavaObservableExecutionHook> observableExecutionHook = new AtomicReference<>();
    private final AtomicReference<RxJavaSchedulersHook> schedulersHook = new AtomicReference<>();
    private final AtomicReference<RxJavaSingleExecutionHook> singleExecutionHook = new AtomicReference<>();

    @Deprecated
    public static RxJavaPlugins getInstance() {
        return INSTANCE;
    }

    RxJavaPlugins() {
    }

    @Experimental
    public void reset() {
        INSTANCE.errorHandler.set(null);
        INSTANCE.observableExecutionHook.set(null);
        INSTANCE.singleExecutionHook.set(null);
        INSTANCE.completableExecutionHook.set(null);
        INSTANCE.schedulersHook.set(null);
    }

    public RxJavaErrorHandler getErrorHandler() {
        if (this.errorHandler.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaErrorHandler.class, System.getProperties());
            if (impl == null) {
                this.errorHandler.compareAndSet(null, DEFAULT_ERROR_HANDLER);
            } else {
                this.errorHandler.compareAndSet(null, (RxJavaErrorHandler) impl);
            }
        }
        return (RxJavaErrorHandler) this.errorHandler.get();
    }

    public void registerErrorHandler(RxJavaErrorHandler impl) {
        if (!this.errorHandler.compareAndSet(null, impl)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.errorHandler.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    public RxJavaObservableExecutionHook getObservableExecutionHook() {
        if (this.observableExecutionHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaObservableExecutionHook.class, System.getProperties());
            if (impl == null) {
                this.observableExecutionHook.compareAndSet(null, RxJavaObservableExecutionHookDefault.getInstance());
            } else {
                this.observableExecutionHook.compareAndSet(null, (RxJavaObservableExecutionHook) impl);
            }
        }
        return (RxJavaObservableExecutionHook) this.observableExecutionHook.get();
    }

    public void registerObservableExecutionHook(RxJavaObservableExecutionHook impl) {
        if (!this.observableExecutionHook.compareAndSet(null, impl)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.observableExecutionHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    public RxJavaSingleExecutionHook getSingleExecutionHook() {
        if (this.singleExecutionHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaSingleExecutionHook.class, System.getProperties());
            if (impl == null) {
                this.singleExecutionHook.compareAndSet(null, RxJavaSingleExecutionHookDefault.getInstance());
            } else {
                this.singleExecutionHook.compareAndSet(null, (RxJavaSingleExecutionHook) impl);
            }
        }
        return (RxJavaSingleExecutionHook) this.singleExecutionHook.get();
    }

    public void registerSingleExecutionHook(RxJavaSingleExecutionHook impl) {
        if (!this.singleExecutionHook.compareAndSet(null, impl)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.singleExecutionHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    @Experimental
    public RxJavaCompletableExecutionHook getCompletableExecutionHook() {
        if (this.completableExecutionHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaCompletableExecutionHook.class, System.getProperties());
            if (impl == null) {
                this.completableExecutionHook.compareAndSet(null, new RxJavaCompletableExecutionHook() {
                });
            } else {
                this.completableExecutionHook.compareAndSet(null, (RxJavaCompletableExecutionHook) impl);
            }
        }
        return (RxJavaCompletableExecutionHook) this.completableExecutionHook.get();
    }

    @Experimental
    public void registerCompletableExecutionHook(RxJavaCompletableExecutionHook impl) {
        if (!this.completableExecutionHook.compareAndSet(null, impl)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.singleExecutionHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }

    static Object getPluginImplementationViaProperty(Class<?> pluginClass, Properties propsIn) {
        Properties props = (Properties) propsIn.clone();
        String classSimpleName = pluginClass.getSimpleName();
        String pluginPrefix = "rxjava.plugin.";
        StringBuilder sb = new StringBuilder();
        sb.append(pluginPrefix);
        sb.append(classSimpleName);
        sb.append(".implementation");
        String implementingClass = props.getProperty(sb.toString());
        if (implementingClass == null) {
            String classSuffix = ".class";
            String implSuffix = ".impl";
            Iterator i$ = props.entrySet().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                Entry<Object, Object> e = (Entry) i$.next();
                String key = e.getKey().toString();
                if (!key.startsWith(pluginPrefix) || !key.endsWith(classSuffix) || !classSimpleName.equals(e.getValue().toString())) {
                    props = props;
                } else {
                    String index = key.substring(0, key.length() - classSuffix.length()).substring(pluginPrefix.length());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(pluginPrefix);
                    sb2.append(index);
                    sb2.append(implSuffix);
                    String implKey = sb2.toString();
                    implementingClass = props.getProperty(implKey);
                    if (implementingClass == null) {
                        StringBuilder sb3 = new StringBuilder();
                        Properties properties = props;
                        sb3.append("Implementing class declaration for ");
                        sb3.append(classSimpleName);
                        sb3.append(" missing: ");
                        sb3.append(implKey);
                        throw new IllegalStateException(sb3.toString());
                    }
                }
            }
        }
        if (implementingClass != null) {
            try {
                try {
                    return Class.forName(implementingClass).asSubclass(pluginClass).newInstance();
                } catch (ClassCastException e2) {
                    e = e2;
                    ClassCastException e3 = e;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(classSimpleName);
                    sb4.append(" implementation is not an instance of ");
                    sb4.append(classSimpleName);
                    sb4.append(": ");
                    sb4.append(implementingClass);
                    throw new IllegalStateException(sb4.toString(), e3);
                } catch (ClassNotFoundException e4) {
                    e = e4;
                    ClassNotFoundException e5 = e;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(classSimpleName);
                    sb5.append(" implementation class not found: ");
                    sb5.append(implementingClass);
                    throw new IllegalStateException(sb5.toString(), e5);
                } catch (InstantiationException e6) {
                    e = e6;
                    InstantiationException e7 = e;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(classSimpleName);
                    sb6.append(" implementation not able to be instantiated: ");
                    sb6.append(implementingClass);
                    throw new IllegalStateException(sb6.toString(), e7);
                } catch (IllegalAccessException e8) {
                    e = e8;
                    IllegalAccessException e9 = e;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(classSimpleName);
                    sb7.append(" implementation not able to be accessed: ");
                    sb7.append(implementingClass);
                    throw new IllegalStateException(sb7.toString(), e9);
                }
            } catch (ClassCastException e10) {
                e = e10;
                Class<?> cls = pluginClass;
                ClassCastException e32 = e;
                StringBuilder sb42 = new StringBuilder();
                sb42.append(classSimpleName);
                sb42.append(" implementation is not an instance of ");
                sb42.append(classSimpleName);
                sb42.append(": ");
                sb42.append(implementingClass);
                throw new IllegalStateException(sb42.toString(), e32);
            } catch (ClassNotFoundException e11) {
                e = e11;
                Class<?> cls2 = pluginClass;
                ClassNotFoundException e52 = e;
                StringBuilder sb52 = new StringBuilder();
                sb52.append(classSimpleName);
                sb52.append(" implementation class not found: ");
                sb52.append(implementingClass);
                throw new IllegalStateException(sb52.toString(), e52);
            } catch (InstantiationException e12) {
                e = e12;
                Class<?> cls3 = pluginClass;
                InstantiationException e72 = e;
                StringBuilder sb62 = new StringBuilder();
                sb62.append(classSimpleName);
                sb62.append(" implementation not able to be instantiated: ");
                sb62.append(implementingClass);
                throw new IllegalStateException(sb62.toString(), e72);
            } catch (IllegalAccessException e13) {
                e = e13;
                Class<?> cls4 = pluginClass;
                IllegalAccessException e92 = e;
                StringBuilder sb72 = new StringBuilder();
                sb72.append(classSimpleName);
                sb72.append(" implementation not able to be accessed: ");
                sb72.append(implementingClass);
                throw new IllegalStateException(sb72.toString(), e92);
            }
        } else {
            Class<?> cls5 = pluginClass;
            return null;
        }
    }

    public RxJavaSchedulersHook getSchedulersHook() {
        if (this.schedulersHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaSchedulersHook.class, System.getProperties());
            if (impl == null) {
                this.schedulersHook.compareAndSet(null, RxJavaSchedulersHook.getDefaultInstance());
            } else {
                this.schedulersHook.compareAndSet(null, (RxJavaSchedulersHook) impl);
            }
        }
        return (RxJavaSchedulersHook) this.schedulersHook.get();
    }

    public void registerSchedulersHook(RxJavaSchedulersHook impl) {
        if (!this.schedulersHook.compareAndSet(null, impl)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Another strategy was already registered: ");
            sb.append(this.schedulersHook.get());
            throw new IllegalStateException(sb.toString());
        }
    }
}
