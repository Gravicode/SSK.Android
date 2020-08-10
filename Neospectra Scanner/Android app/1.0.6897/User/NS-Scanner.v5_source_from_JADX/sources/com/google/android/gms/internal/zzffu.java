package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffu;
import com.google.android.gms.internal.zzffu.zza;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class zzffu<MessageType extends zzffu<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zzfek<MessageType, BuilderType> {
    private static Map<Object, zzffu<?, ?>> zzpgt = new ConcurrentHashMap();
    protected zzfio zzpgr = zzfio.zzczu();
    protected int zzpgs = -1;

    public static abstract class zza<MessageType extends zzffu<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zzfel<MessageType, BuilderType> {
        private final MessageType zzpgu;
        protected MessageType zzpgv;
        private boolean zzpgw = false;

        protected zza(MessageType messagetype) {
            this.zzpgu = messagetype;
            this.zzpgv = (zzffu) messagetype.zza(zzg.zzphi, (Object) null, (Object) null);
        }

        private static void zza(MessageType messagetype, MessageType messagetype2) {
            zzf zzf = zzf.zzphb;
            messagetype.zza(zzg.zzphd, (Object) zzf, (Object) messagetype2);
            messagetype.zzpgr = zzf.zza(messagetype.zzpgr, messagetype2.zzpgr);
        }

        /* access modifiers changed from: private */
        /* renamed from: zzd */
        public final BuilderType zzb(zzffb zzffb, zzffm zzffm) throws IOException {
            zzcxr();
            try {
                this.zzpgv.zza(zzg.zzphg, (Object) zzffb, (Object) zzffm);
                return this;
            } catch (RuntimeException e) {
                if (e.getCause() instanceof IOException) {
                    throw ((IOException) e.getCause());
                }
                throw e;
            }
        }

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            zza zza = (zza) ((zzffu) this.zzpgu).zza(zzg.zzphj, (Object) null, (Object) null);
            if (!this.zzpgw) {
                MessageType messagetype = this.zzpgv;
                messagetype.zza(zzg.zzphh, (Object) null, (Object) null);
                messagetype.zzpgr.zzbiy();
                this.zzpgw = true;
            }
            zza.zza((MessageType) (zzffu) this.zzpgv);
            return zza;
        }

        public final boolean isInitialized() {
            return zzffu.zza(this.zzpgv, false);
        }

        public final /* synthetic */ zzfel zza(zzffb zzffb, zzffm zzffm) throws IOException {
            return (zza) zzb(zzffb, zzffm);
        }

        public final BuilderType zza(MessageType messagetype) {
            zzcxr();
            zza(this.zzpgv, messagetype);
            return this;
        }

        public final /* synthetic */ zzfel zzcvh() {
            return (zza) clone();
        }

        public final /* synthetic */ zzfhe zzcxq() {
            return this.zzpgu;
        }

        /* access modifiers changed from: protected */
        public final void zzcxr() {
            if (this.zzpgw) {
                MessageType messagetype = (zzffu) this.zzpgv.zza(zzg.zzphi, (Object) null, (Object) null);
                zza(messagetype, this.zzpgv);
                this.zzpgv = messagetype;
                this.zzpgw = false;
            }
        }

        public final MessageType zzcxs() {
            if (this.zzpgw) {
                return this.zzpgv;
            }
            MessageType messagetype = this.zzpgv;
            messagetype.zza(zzg.zzphh, (Object) null, (Object) null);
            messagetype.zzpgr.zzbiy();
            this.zzpgw = true;
            return this.zzpgv;
        }

        public final MessageType zzcxt() {
            boolean z = true;
            if (!this.zzpgw) {
                MessageType messagetype = this.zzpgv;
                messagetype.zza(zzg.zzphh, (Object) null, (Object) null);
                messagetype.zzpgr.zzbiy();
                this.zzpgw = true;
            }
            MessageType messagetype2 = (zzffu) this.zzpgv;
            boolean booleanValue = Boolean.TRUE.booleanValue();
            byte byteValue = ((Byte) messagetype2.zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
            if (byteValue != 1) {
                if (byteValue == 0) {
                    z = false;
                } else {
                    if (messagetype2.zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) == null) {
                        z = false;
                    }
                    if (booleanValue) {
                        messagetype2.zza(zzg.zzphf, (Object) z ? messagetype2 : null, (Object) null);
                    }
                }
            }
            if (z) {
                return messagetype2;
            }
            throw new zzfim(messagetype2);
        }

        public final /* synthetic */ zzfhe zzcxu() {
            if (this.zzpgw) {
                return this.zzpgv;
            }
            MessageType messagetype = this.zzpgv;
            messagetype.zza(zzg.zzphh, (Object) null, (Object) null);
            messagetype.zzpgr.zzbiy();
            this.zzpgw = true;
            return this.zzpgv;
        }

        public final /* synthetic */ zzfhe zzcxv() {
            boolean z = true;
            if (!this.zzpgw) {
                MessageType messagetype = this.zzpgv;
                messagetype.zza(zzg.zzphh, (Object) null, (Object) null);
                messagetype.zzpgr.zzbiy();
                this.zzpgw = true;
            }
            zzffu zzffu = (zzffu) this.zzpgv;
            boolean booleanValue = Boolean.TRUE.booleanValue();
            byte byteValue = ((Byte) zzffu.zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
            if (byteValue != 1) {
                if (byteValue == 0) {
                    z = false;
                } else {
                    if (zzffu.zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) == null) {
                        z = false;
                    }
                    if (booleanValue) {
                        zzffu.zza(zzg.zzphf, (Object) z ? zzffu : null, (Object) null);
                    }
                }
            }
            if (z) {
                return zzffu;
            }
            throw new zzfim(zzffu);
        }
    }

    public static class zzb<T extends zzffu<T, ?>> extends zzfen<T> {
        private T zzpgu;

        public zzb(T t) {
            this.zzpgu = t;
        }

        public final /* synthetic */ Object zze(zzffb zzffb, zzffm zzffm) throws zzfge {
            return zzffu.zza(this.zzpgu, zzffb, zzffm);
        }
    }

    static class zzc implements zzh {
        static final zzc zzpgx = new zzc();
        private static zzffv zzpgy = new zzffv();

        private zzc() {
        }

        public final double zza(boolean z, double d, boolean z2, double d2) {
            if (z == z2 && d == d2) {
                return d;
            }
            throw zzpgy;
        }

        public final int zza(boolean z, int i, boolean z2, int i2) {
            if (z == z2 && i == i2) {
                return i;
            }
            throw zzpgy;
        }

        public final long zza(boolean z, long j, boolean z2, long j2) {
            if (z == z2 && j == j2) {
                return j;
            }
            throw zzpgy;
        }

        public final zzfes zza(boolean z, zzfes zzfes, boolean z2, zzfes zzfes2) {
            if (z == z2 && zzfes.equals(zzfes2)) {
                return zzfes;
            }
            throw zzpgy;
        }

        public final zzfgc zza(zzfgc zzfgc, zzfgc zzfgc2) {
            if (zzfgc.equals(zzfgc2)) {
                return zzfgc;
            }
            throw zzpgy;
        }

        public final <T> zzfgd<T> zza(zzfgd<T> zzfgd, zzfgd<T> zzfgd2) {
            if (zzfgd.equals(zzfgd2)) {
                return zzfgd;
            }
            throw zzpgy;
        }

        public final <K, V> zzfgy<K, V> zza(zzfgy<K, V> zzfgy, zzfgy<K, V> zzfgy2) {
            if (zzfgy.equals(zzfgy2)) {
                return zzfgy;
            }
            throw zzpgy;
        }

        public final <T extends zzfhe> T zza(T t, T t2) {
            if (t == null && t2 == null) {
                return null;
            }
            if (t == null || t2 == null) {
                throw zzpgy;
            }
            T t3 = (zzffu) t;
            if (t3 != t2 && ((zzffu) t3.zza(zzg.zzphk, (Object) null, (Object) null)).getClass().isInstance(t2)) {
                zzffu zzffu = (zzffu) t2;
                t3.zza(zzg.zzphd, (Object) this, (Object) zzffu);
                t3.zzpgr = zza(t3.zzpgr, zzffu.zzpgr);
            }
            return t;
        }

        public final zzfio zza(zzfio zzfio, zzfio zzfio2) {
            if (zzfio.equals(zzfio2)) {
                return zzfio;
            }
            throw zzpgy;
        }

        public final Object zza(boolean z, Object obj, Object obj2) {
            if (z && obj.equals(obj2)) {
                return obj;
            }
            throw zzpgy;
        }

        public final String zza(boolean z, String str, boolean z2, String str2) {
            if (z == z2 && str.equals(str2)) {
                return str;
            }
            throw zzpgy;
        }

        public final boolean zza(boolean z, boolean z2, boolean z3, boolean z4) {
            if (z == z3 && z2 == z4) {
                return z2;
            }
            throw zzpgy;
        }

        public final Object zzb(boolean z, Object obj, Object obj2) {
            if (z && obj.equals(obj2)) {
                return obj;
            }
            throw zzpgy;
        }

        public final Object zzc(boolean z, Object obj, Object obj2) {
            if (z && obj.equals(obj2)) {
                return obj;
            }
            throw zzpgy;
        }

        public final Object zzd(boolean z, Object obj, Object obj2) {
            if (z && obj.equals(obj2)) {
                return obj;
            }
            throw zzpgy;
        }

        public final void zzdf(boolean z) {
            if (z) {
                throw zzpgy;
            }
        }

        public final Object zze(boolean z, Object obj, Object obj2) {
            if (z && obj.equals(obj2)) {
                return obj;
            }
            throw zzpgy;
        }

        public final Object zzf(boolean z, Object obj, Object obj2) {
            if (z && obj.equals(obj2)) {
                return obj;
            }
            throw zzpgy;
        }

        public final Object zzg(boolean z, Object obj, Object obj2) {
            if (z) {
                zzffu zzffu = (zzffu) obj;
                zzfhe zzfhe = (zzfhe) obj2;
                boolean z2 = true;
                if (zzffu != zzfhe) {
                    if (!((zzffu) zzffu.zza(zzg.zzphk, (Object) null, (Object) null)).getClass().isInstance(zzfhe)) {
                        z2 = false;
                    } else {
                        zzffu zzffu2 = (zzffu) zzfhe;
                        zzffu.zza(zzg.zzphd, (Object) this, (Object) zzffu2);
                        zzffu.zzpgr = zza(zzffu.zzpgr, zzffu2.zzpgr);
                    }
                }
                if (z2) {
                    return obj;
                }
            }
            throw zzpgy;
        }
    }

    public static abstract class zzd<MessageType extends zzd<MessageType, BuilderType>, BuilderType> extends zzffu<MessageType, BuilderType> implements zzfhg {
        protected zzffq<Object> zzpgz = zzffq.zzcxf();
    }

    static class zze implements zzh {
        int zzpha = 0;

        zze() {
        }

        public final double zza(boolean z, double d, boolean z2, double d2) {
            this.zzpha = (this.zzpha * 53) + zzffz.zzde(Double.doubleToLongBits(d));
            return d;
        }

        public final int zza(boolean z, int i, boolean z2, int i2) {
            this.zzpha = (this.zzpha * 53) + i;
            return i;
        }

        public final long zza(boolean z, long j, boolean z2, long j2) {
            this.zzpha = (this.zzpha * 53) + zzffz.zzde(j);
            return j;
        }

        public final zzfes zza(boolean z, zzfes zzfes, boolean z2, zzfes zzfes2) {
            this.zzpha = (this.zzpha * 53) + zzfes.hashCode();
            return zzfes;
        }

        public final zzfgc zza(zzfgc zzfgc, zzfgc zzfgc2) {
            this.zzpha = (this.zzpha * 53) + zzfgc.hashCode();
            return zzfgc;
        }

        public final <T> zzfgd<T> zza(zzfgd<T> zzfgd, zzfgd<T> zzfgd2) {
            this.zzpha = (this.zzpha * 53) + zzfgd.hashCode();
            return zzfgd;
        }

        public final <K, V> zzfgy<K, V> zza(zzfgy<K, V> zzfgy, zzfgy<K, V> zzfgy2) {
            this.zzpha = (this.zzpha * 53) + zzfgy.hashCode();
            return zzfgy;
        }

        public final <T extends zzfhe> T zza(T t, T t2) {
            int i;
            if (t == null) {
                i = 37;
            } else if (t instanceof zzffu) {
                zzffu zzffu = (zzffu) t;
                if (zzffu.zzpfa == 0) {
                    int i2 = this.zzpha;
                    this.zzpha = 0;
                    zzffu.zza(zzg.zzphd, (Object) this, (Object) zzffu);
                    zzffu.zzpgr = zza(zzffu.zzpgr, zzffu.zzpgr);
                    zzffu.zzpfa = this.zzpha;
                    this.zzpha = i2;
                }
                i = zzffu.zzpfa;
            } else {
                i = t.hashCode();
            }
            this.zzpha = (this.zzpha * 53) + i;
            return t;
        }

        public final zzfio zza(zzfio zzfio, zzfio zzfio2) {
            this.zzpha = (this.zzpha * 53) + zzfio.hashCode();
            return zzfio;
        }

        public final Object zza(boolean z, Object obj, Object obj2) {
            this.zzpha = (this.zzpha * 53) + zzffz.zzdg(((Boolean) obj).booleanValue());
            return obj;
        }

        public final String zza(boolean z, String str, boolean z2, String str2) {
            this.zzpha = (this.zzpha * 53) + str.hashCode();
            return str;
        }

        public final boolean zza(boolean z, boolean z2, boolean z3, boolean z4) {
            this.zzpha = (this.zzpha * 53) + zzffz.zzdg(z2);
            return z2;
        }

        public final Object zzb(boolean z, Object obj, Object obj2) {
            this.zzpha = (this.zzpha * 53) + ((Integer) obj).intValue();
            return obj;
        }

        public final Object zzc(boolean z, Object obj, Object obj2) {
            this.zzpha = (this.zzpha * 53) + zzffz.zzde(Double.doubleToLongBits(((Double) obj).doubleValue()));
            return obj;
        }

        public final Object zzd(boolean z, Object obj, Object obj2) {
            this.zzpha = (this.zzpha * 53) + zzffz.zzde(((Long) obj).longValue());
            return obj;
        }

        public final void zzdf(boolean z) {
            if (z) {
                throw new IllegalStateException();
            }
        }

        public final Object zze(boolean z, Object obj, Object obj2) {
            this.zzpha = (this.zzpha * 53) + obj.hashCode();
            return obj;
        }

        public final Object zzf(boolean z, Object obj, Object obj2) {
            this.zzpha = (this.zzpha * 53) + obj.hashCode();
            return obj;
        }

        public final Object zzg(boolean z, Object obj, Object obj2) {
            return zza((T) (zzfhe) obj, (T) (zzfhe) obj2);
        }
    }

    public static class zzf implements zzh {
        public static final zzf zzphb = new zzf();

        private zzf() {
        }

        public final double zza(boolean z, double d, boolean z2, double d2) {
            return z2 ? d2 : d;
        }

        public final int zza(boolean z, int i, boolean z2, int i2) {
            return z2 ? i2 : i;
        }

        public final long zza(boolean z, long j, boolean z2, long j2) {
            return z2 ? j2 : j;
        }

        public final zzfes zza(boolean z, zzfes zzfes, boolean z2, zzfes zzfes2) {
            return z2 ? zzfes2 : zzfes;
        }

        public final zzfgc zza(zzfgc zzfgc, zzfgc zzfgc2) {
            int size = zzfgc.size();
            int size2 = zzfgc2.size();
            if (size > 0 && size2 > 0) {
                if (!zzfgc.zzcvi()) {
                    zzfgc = zzfgc.zzlu(size2 + size);
                }
                zzfgc.addAll(zzfgc2);
            }
            return size > 0 ? zzfgc : zzfgc2;
        }

        public final <T> zzfgd<T> zza(zzfgd<T> zzfgd, zzfgd<T> zzfgd2) {
            int size = zzfgd.size();
            int size2 = zzfgd2.size();
            if (size > 0 && size2 > 0) {
                if (!zzfgd.zzcvi()) {
                    zzfgd = zzfgd.zzly(size2 + size);
                }
                zzfgd.addAll(zzfgd2);
            }
            return size > 0 ? zzfgd : zzfgd2;
        }

        public final <K, V> zzfgy<K, V> zza(zzfgy<K, V> zzfgy, zzfgy<K, V> zzfgy2) {
            if (!zzfgy2.isEmpty()) {
                if (!zzfgy.isMutable()) {
                    zzfgy = zzfgy.zzcyq();
                }
                zzfgy.zza(zzfgy2);
            }
            return zzfgy;
        }

        public final <T extends zzfhe> T zza(T t, T t2) {
            return (t == null || t2 == null) ? t != null ? t : t2 : t.zzcxp().zzd(t2).zzcxv();
        }

        public final zzfio zza(zzfio zzfio, zzfio zzfio2) {
            return zzfio2 == zzfio.zzczu() ? zzfio : zzfio.zzb(zzfio, zzfio2);
        }

        public final Object zza(boolean z, Object obj, Object obj2) {
            return obj2;
        }

        public final String zza(boolean z, String str, boolean z2, String str2) {
            return z2 ? str2 : str;
        }

        public final boolean zza(boolean z, boolean z2, boolean z3, boolean z4) {
            return z3 ? z4 : z2;
        }

        public final Object zzb(boolean z, Object obj, Object obj2) {
            return obj2;
        }

        public final Object zzc(boolean z, Object obj, Object obj2) {
            return obj2;
        }

        public final Object zzd(boolean z, Object obj, Object obj2) {
            return obj2;
        }

        public final void zzdf(boolean z) {
        }

        public final Object zze(boolean z, Object obj, Object obj2) {
            return obj2;
        }

        public final Object zzf(boolean z, Object obj, Object obj2) {
            return obj2;
        }

        public final Object zzg(boolean z, Object obj, Object obj2) {
            return z ? zza((T) (zzfhe) obj, (T) (zzfhe) obj2) : obj2;
        }
    }

    /* 'enum' access flag removed */
    public static final class zzg {
        public static final int zzphc = 1;
        public static final int zzphd = 2;
        public static final int zzphe = 3;
        public static final int zzphf = 4;
        public static final int zzphg = 5;
        public static final int zzphh = 6;
        public static final int zzphi = 7;
        public static final int zzphj = 8;
        public static final int zzphk = 9;
        public static final int zzphl = 10;
        private static final /* synthetic */ int[] zzphm = {zzphc, zzphd, zzphe, zzphf, zzphg, zzphh, zzphi, zzphj, zzphk, zzphl};
        public static final int zzphn = 1;
        public static final int zzpho = 2;
        private static final /* synthetic */ int[] zzphp = {zzphn, zzpho};

        /* renamed from: values$50KLMJ33DTMIUPRFDTJMOP9FE1P6UT3FC9QMCBQ7CLN6ASJ1EHIM8JB5EDPM2PR59HKN8P949LIN8Q3FCHA6UIBEEPNMMP9R0 */
        public static int[] m13x126d66cb() {
            return (int[]) zzphm.clone();
        }
    }

    public interface zzh {
        double zza(boolean z, double d, boolean z2, double d2);

        int zza(boolean z, int i, boolean z2, int i2);

        long zza(boolean z, long j, boolean z2, long j2);

        zzfes zza(boolean z, zzfes zzfes, boolean z2, zzfes zzfes2);

        zzfgc zza(zzfgc zzfgc, zzfgc zzfgc2);

        <T> zzfgd<T> zza(zzfgd<T> zzfgd, zzfgd<T> zzfgd2);

        <K, V> zzfgy<K, V> zza(zzfgy<K, V> zzfgy, zzfgy<K, V> zzfgy2);

        <T extends zzfhe> T zza(T t, T t2);

        zzfio zza(zzfio zzfio, zzfio zzfio2);

        Object zza(boolean z, Object obj, Object obj2);

        String zza(boolean z, String str, boolean z2, String str2);

        boolean zza(boolean z, boolean z2, boolean z3, boolean z4);

        Object zzb(boolean z, Object obj, Object obj2);

        Object zzc(boolean z, Object obj, Object obj2);

        Object zzd(boolean z, Object obj, Object obj2);

        void zzdf(boolean z);

        Object zze(boolean z, Object obj, Object obj2);

        Object zzf(boolean z, Object obj, Object obj2);

        Object zzg(boolean z, Object obj, Object obj2);
    }

    protected static <T extends zzffu<T, ?>> T zza(T t, zzfes zzfes) throws zzfge {
        boolean z;
        T zza2 = zza(t, zzfes, zzffm.zzcxb());
        boolean z2 = false;
        if (zza2 != null) {
            boolean booleanValue = Boolean.TRUE.booleanValue();
            byte byteValue = ((Byte) zza2.zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
            if (byteValue == 1) {
                z = true;
            } else if (byteValue == 0) {
                z = false;
            } else {
                z = zza2.zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) != null;
                if (booleanValue) {
                    zza2.zza(zzg.zzphf, (Object) z ? zza2 : null, (Object) null);
                }
            }
            if (!z) {
                throw new zzfim(zza2).zzczt().zzi(zza2);
            }
        }
        if (zza2 != null) {
            boolean booleanValue2 = Boolean.TRUE.booleanValue();
            byte byteValue2 = ((Byte) zza2.zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
            if (byteValue2 == 1) {
                z2 = true;
            } else if (byteValue2 != 0) {
                if (zza2.zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) != null) {
                    z2 = true;
                }
                if (booleanValue2) {
                    zza2.zza(zzg.zzphf, (Object) z2 ? zza2 : null, (Object) null);
                }
            }
            if (!z2) {
                throw new zzfim(zza2).zzczt().zzi(zza2);
            }
        }
        return zza2;
    }

    private static <T extends zzffu<T, ?>> T zza(T t, zzfes zzfes, zzffm zzffm) throws zzfge {
        T zza2;
        try {
            zzffb zzcvm = zzfes.zzcvm();
            zza2 = zza(t, zzcvm, zzffm);
            zzcvm.zzkp(0);
            return zza2;
        } catch (zzfge e) {
            throw e.zzi(zza2);
        } catch (zzfge e2) {
            throw e2;
        }
    }

    static <T extends zzffu<T, ?>> T zza(T t, zzffb zzffb, zzffm zzffm) throws zzfge {
        T t2 = (zzffu) t.zza(zzg.zzphi, (Object) null, (Object) null);
        try {
            t2.zza(zzg.zzphg, (Object) zzffb, (Object) zzffm);
            t2.zza(zzg.zzphh, (Object) null, (Object) null);
            t2.zzpgr.zzbiy();
            return t2;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof zzfge) {
                throw ((zzfge) e.getCause());
            }
            throw e;
        }
    }

    protected static <T extends zzffu<T, ?>> T zza(T t, byte[] bArr) throws zzfge {
        T zza2 = zza(t, bArr, zzffm.zzcxb());
        if (zza2 != null) {
            boolean booleanValue = Boolean.TRUE.booleanValue();
            byte byteValue = ((Byte) zza2.zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
            boolean z = false;
            if (byteValue == 1) {
                z = true;
            } else if (byteValue != 0) {
                if (zza2.zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) != null) {
                    z = true;
                }
                if (booleanValue) {
                    zza2.zza(zzg.zzphf, (Object) z ? zza2 : null, (Object) null);
                }
            }
            if (!z) {
                throw new zzfim(zza2).zzczt().zzi(zza2);
            }
        }
        return zza2;
    }

    private static <T extends zzffu<T, ?>> T zza(T t, byte[] bArr, zzffm zzffm) throws zzfge {
        T zza2;
        try {
            zzffb zzbb = zzffb.zzbb(bArr);
            zza2 = zza(t, zzbb, zzffm);
            zzbb.zzkp(0);
            return zza2;
        } catch (zzfge e) {
            throw e.zzi(zza2);
        } catch (zzfge e2) {
            throw e2;
        }
    }

    static Object zza(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
            }
        }
    }

    protected static final <T extends zzffu<T, ?>> boolean zza(T t, boolean z) {
        byte byteValue = ((Byte) t.zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        return t.zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) != null;
    }

    protected static zzfgc zzcxn() {
        return zzffy.zzcxz();
    }

    protected static <E> zzfgd<E> zzcxo() {
        return zzfho.zzcza();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!((zzffu) zza(zzg.zzphk, (Object) null, (Object) null)).getClass().isInstance(obj)) {
            return false;
        }
        try {
            zzc zzc2 = zzc.zzpgx;
            zzffu zzffu = (zzffu) obj;
            zza(zzg.zzphd, (Object) zzc2, (Object) zzffu);
            this.zzpgr = zzc2.zza(this.zzpgr, zzffu.zzpgr);
            return true;
        } catch (zzffv e) {
            return false;
        }
    }

    public int hashCode() {
        if (this.zzpfa != 0) {
            return this.zzpfa;
        }
        zze zze2 = new zze();
        zza(zzg.zzphd, (Object) zze2, (Object) this);
        this.zzpgr = zze2.zza(this.zzpgr, this.zzpgr);
        this.zzpfa = zze2.zzpha;
        return this.zzpfa;
    }

    public final boolean isInitialized() {
        boolean booleanValue = Boolean.TRUE.booleanValue();
        byte byteValue = ((Byte) zza(zzg.zzphe, (Object) null, (Object) null)).byteValue();
        boolean z = true;
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        if (zza(zzg.zzphc, (Object) Boolean.FALSE, (Object) null) == null) {
            z = false;
        }
        if (booleanValue) {
            zza(zzg.zzphf, (Object) z ? this : null, (Object) null);
        }
        return z;
    }

    public String toString() {
        return zzfhh.zza(this, super.toString());
    }

    /* access modifiers changed from: protected */
    public abstract Object zza(int i, Object obj, Object obj2);

    public void zza(zzffg zzffg) throws IOException {
        zzfhn.zzcyz().zzl(getClass()).zza(this, zzffi.zzb(zzffg));
    }

    /* access modifiers changed from: protected */
    public final boolean zza(int i, zzffb zzffb) throws IOException {
        if ((i & 7) == 4) {
            return false;
        }
        if (this.zzpgr == zzfio.zzczu()) {
            this.zzpgr = zzfio.zzczv();
        }
        return this.zzpgr.zzb(i, zzffb);
    }

    public final zzfhk<MessageType> zzcxm() {
        return (zzfhk) zza(zzg.zzphl, (Object) null, (Object) null);
    }

    public final /* synthetic */ zzfhf zzcxp() {
        zza zza2 = (zza) zza(zzg.zzphj, (Object) null, (Object) null);
        zza2.zza(this);
        return zza2;
    }

    public final /* synthetic */ zzfhe zzcxq() {
        return (zzffu) zza(zzg.zzphk, (Object) null, (Object) null);
    }

    public int zzho() {
        if (this.zzpgs == -1) {
            this.zzpgs = zzfhn.zzcyz().zzl(getClass()).zzcp(this);
        }
        return this.zzpgs;
    }
}
