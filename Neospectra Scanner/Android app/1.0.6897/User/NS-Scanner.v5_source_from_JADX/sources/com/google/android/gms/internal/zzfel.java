package com.google.android.gms.internal;

import com.google.android.gms.internal.zzfek;
import com.google.android.gms.internal.zzfel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class zzfel<MessageType extends zzfek<MessageType, BuilderType>, BuilderType extends zzfel<MessageType, BuilderType>> implements zzfhf {
    protected static <T> void zza(Iterable<T> iterable, List<? super T> list) {
        zzffz.checkNotNull(iterable);
        if (iterable instanceof zzfgl) {
            List zzcyl = ((zzfgl) iterable).zzcyl();
            zzfgl zzfgl = (zzfgl) list;
            int size = list.size();
            for (Object next : zzcyl) {
                if (next == null) {
                    int size2 = zzfgl.size() - size;
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Element at index ");
                    sb.append(size2);
                    sb.append(" is null.");
                    String sb2 = sb.toString();
                    for (int size3 = zzfgl.size() - 1; size3 >= size; size3--) {
                        zzfgl.remove(size3);
                    }
                    throw new NullPointerException(sb2);
                } else if (!(next instanceof zzfes)) {
                    zzfgl.add((String) next);
                }
            }
        } else if (iterable instanceof zzfhl) {
            list.addAll((Collection) iterable);
        } else {
            zzb(iterable, list);
        }
    }

    private static <T> void zzb(Iterable<T> iterable, List<? super T> list) {
        if ((list instanceof ArrayList) && (iterable instanceof Collection)) {
            ((ArrayList) list).ensureCapacity(list.size() + ((Collection) iterable).size());
        }
        int size = list.size();
        for (Object next : iterable) {
            if (next == null) {
                int size2 = list.size() - size;
                StringBuilder sb = new StringBuilder(37);
                sb.append("Element at index ");
                sb.append(size2);
                sb.append(" is null.");
                String sb2 = sb.toString();
                for (int size3 = list.size() - 1; size3 >= size; size3--) {
                    list.remove(size3);
                }
                throw new NullPointerException(sb2);
            }
            list.add(next);
        }
    }

    /* access modifiers changed from: protected */
    public abstract BuilderType zza(MessageType messagetype);

    /* renamed from: zza */
    public abstract BuilderType zzb(zzffb zzffb, zzffm zzffm) throws IOException;

    /* renamed from: zzcvh */
    public abstract BuilderType clone();

    public final /* synthetic */ zzfhf zzd(zzfhe zzfhe) {
        if (zzcxq().getClass().isInstance(zzfhe)) {
            return zza((zzfek) zzfhe);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
