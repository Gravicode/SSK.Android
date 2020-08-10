package p005io.reactivex.internal.disposables;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.internal.disposables.ListCompositeDisposable */
public final class ListCompositeDisposable implements Disposable, DisposableContainer {
    volatile boolean disposed;
    List<Disposable> resources;

    public ListCompositeDisposable() {
    }

    public ListCompositeDisposable(Disposable... resources2) {
        ObjectHelper.requireNonNull(resources2, "resources is null");
        this.resources = new LinkedList();
        for (Disposable d : resources2) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public ListCompositeDisposable(Iterable<? extends Disposable> resources2) {
        ObjectHelper.requireNonNull(resources2, "resources is null");
        this.resources = new LinkedList();
        for (Disposable d : resources2) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public void dispose() {
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    this.disposed = true;
                    List<Disposable> set = this.resources;
                    this.resources = null;
                    dispose(set);
                }
            }
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public boolean add(Disposable d) {
        ObjectHelper.requireNonNull(d, "d is null");
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    List<Disposable> set = this.resources;
                    if (set == null) {
                        set = new LinkedList<>();
                        this.resources = set;
                    }
                    set.add(d);
                    return true;
                }
            }
        }
        d.dispose();
        return false;
    }

    public boolean addAll(Disposable... ds) {
        ObjectHelper.requireNonNull(ds, "ds is null");
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    List<Disposable> set = this.resources;
                    if (set == null) {
                        set = new LinkedList<>();
                        this.resources = set;
                    }
                    for (Disposable d : ds) {
                        ObjectHelper.requireNonNull(d, "d is null");
                        set.add(d);
                    }
                    return true;
                }
            }
        }
        for (Disposable d2 : ds) {
            d2.dispose();
        }
        return false;
    }

    public boolean remove(Disposable d) {
        if (!delete(d)) {
            return false;
        }
        d.dispose();
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0021, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean delete(p005io.reactivex.disposables.Disposable r4) {
        /*
            r3 = this;
            java.lang.String r0 = "Disposable item is null"
            p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r0)
            boolean r0 = r3.disposed
            r1 = 0
            if (r0 == 0) goto L_0x000b
            return r1
        L_0x000b:
            monitor-enter(r3)
            boolean r0 = r3.disposed     // Catch:{ all -> 0x0022 }
            if (r0 == 0) goto L_0x0012
            monitor-exit(r3)     // Catch:{ all -> 0x0022 }
            return r1
        L_0x0012:
            java.util.List<io.reactivex.disposables.Disposable> r0 = r3.resources     // Catch:{ all -> 0x0022 }
            if (r0 == 0) goto L_0x0020
            boolean r2 = r0.remove(r4)     // Catch:{ all -> 0x0022 }
            if (r2 != 0) goto L_0x001d
            goto L_0x0020
        L_0x001d:
            monitor-exit(r3)     // Catch:{ all -> 0x0022 }
            r0 = 1
            return r0
        L_0x0020:
            monitor-exit(r3)     // Catch:{ all -> 0x0022 }
            return r1
        L_0x0022:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0022 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.disposables.ListCompositeDisposable.delete(io.reactivex.disposables.Disposable):boolean");
    }

    public void clear() {
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    List<Disposable> set = this.resources;
                    this.resources = null;
                    dispose(set);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispose(List<Disposable> set) {
        if (set != null) {
            List<Throwable> errors = null;
            for (Disposable o : set) {
                try {
                    o.dispose();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(ex);
                }
            }
            if (errors == null) {
                return;
            }
            if (errors.size() == 1) {
                throw ExceptionHelper.wrapOrThrow((Throwable) errors.get(0));
            }
            throw new CompositeException((Iterable<? extends Throwable>) errors);
        }
    }
}
