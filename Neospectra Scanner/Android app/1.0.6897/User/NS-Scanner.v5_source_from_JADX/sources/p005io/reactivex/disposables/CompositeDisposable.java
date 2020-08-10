package p005io.reactivex.disposables;

import java.util.ArrayList;
import java.util.List;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableContainer;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.internal.util.OpenHashSet;

/* renamed from: io.reactivex.disposables.CompositeDisposable */
public final class CompositeDisposable implements Disposable, DisposableContainer {
    volatile boolean disposed;
    OpenHashSet<Disposable> resources;

    public CompositeDisposable() {
    }

    public CompositeDisposable(@NonNull Disposable... resources2) {
        ObjectHelper.requireNonNull(resources2, "resources is null");
        this.resources = new OpenHashSet<>(resources2.length + 1);
        for (Disposable d : resources2) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public CompositeDisposable(@NonNull Iterable<? extends Disposable> resources2) {
        ObjectHelper.requireNonNull(resources2, "resources is null");
        this.resources = new OpenHashSet<>();
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
                    OpenHashSet<Disposable> set = this.resources;
                    this.resources = null;
                    dispose(set);
                }
            }
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public boolean add(@NonNull Disposable d) {
        ObjectHelper.requireNonNull(d, "d is null");
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    OpenHashSet<Disposable> set = this.resources;
                    if (set == null) {
                        set = new OpenHashSet<>();
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

    public boolean addAll(@NonNull Disposable... ds) {
        ObjectHelper.requireNonNull(ds, "ds is null");
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    OpenHashSet<Disposable> set = this.resources;
                    if (set == null) {
                        set = new OpenHashSet<>(ds.length + 1);
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

    public boolean remove(@NonNull Disposable d) {
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
    public boolean delete(@p005io.reactivex.annotations.NonNull p005io.reactivex.disposables.Disposable r4) {
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
            io.reactivex.internal.util.OpenHashSet<io.reactivex.disposables.Disposable> r0 = r3.resources     // Catch:{ all -> 0x0022 }
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
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.disposables.CompositeDisposable.delete(io.reactivex.disposables.Disposable):boolean");
    }

    public void clear() {
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    OpenHashSet<Disposable> set = this.resources;
                    this.resources = null;
                    dispose(set);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0017, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int size() {
        /*
            r2 = this;
            boolean r0 = r2.disposed
            r1 = 0
            if (r0 == 0) goto L_0x0006
            return r1
        L_0x0006:
            monitor-enter(r2)
            boolean r0 = r2.disposed     // Catch:{ all -> 0x0018 }
            if (r0 == 0) goto L_0x000d
            monitor-exit(r2)     // Catch:{ all -> 0x0018 }
            return r1
        L_0x000d:
            io.reactivex.internal.util.OpenHashSet<io.reactivex.disposables.Disposable> r0 = r2.resources     // Catch:{ all -> 0x0018 }
            if (r0 == 0) goto L_0x0016
            int r1 = r0.size()     // Catch:{ all -> 0x0018 }
        L_0x0016:
            monitor-exit(r2)     // Catch:{ all -> 0x0018 }
            return r1
        L_0x0018:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0018 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.disposables.CompositeDisposable.size():int");
    }

    /* access modifiers changed from: 0000 */
    public void dispose(OpenHashSet<Disposable> set) {
        Object[] array;
        if (set != null) {
            List list = null;
            for (Object o : set.keys()) {
                if (o instanceof Disposable) {
                    try {
                        ((Disposable) o).dispose();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (list == null) {
                            list = new ArrayList();
                        }
                        list.add(ex);
                    }
                }
            }
            if (list == null) {
                return;
            }
            if (list.size() == 1) {
                throw ExceptionHelper.wrapOrThrow((Throwable) list.get(0));
            }
            throw new CompositeException((Iterable<? extends Throwable>) list);
        }
    }
}
