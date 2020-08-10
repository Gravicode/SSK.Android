package p008rx.plugins;

import p008rx.annotations.Beta;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.plugins.RxJavaErrorHandler */
public abstract class RxJavaErrorHandler {
    protected static final String ERROR_IN_RENDERING_SUFFIX = ".errorRendering";

    @Deprecated
    public void handleError(Throwable e) {
    }

    @Beta
    public final String handleOnNextValueRendering(Object item) {
        try {
            return render(item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            StringBuilder sb = new StringBuilder();
            sb.append(item.getClass().getName());
            sb.append(ERROR_IN_RENDERING_SUFFIX);
            return sb.toString();
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(item.getClass().getName());
            sb2.append(ERROR_IN_RENDERING_SUFFIX);
            return sb2.toString();
        }
    }

    /* access modifiers changed from: protected */
    @Beta
    public String render(Object item) throws InterruptedException {
        return null;
    }
}
