package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class zzg<T> extends AbstractDataBuffer<T> {
    private boolean zzfwo = false;
    private ArrayList<Integer> zzfwp;

    protected zzg(DataHolder dataHolder) {
        super(dataHolder);
    }

    private final void zzakb() {
        synchronized (this) {
            if (!this.zzfwo) {
                int i = this.zzfqt.zzfwg;
                this.zzfwp = new ArrayList<>();
                if (i > 0) {
                    this.zzfwp.add(Integer.valueOf(0));
                    String zzaka = zzaka();
                    String zzd = this.zzfqt.zzd(zzaka, 0, this.zzfqt.zzbz(0));
                    for (int i2 = 1; i2 < i; i2++) {
                        int zzbz = this.zzfqt.zzbz(i2);
                        String zzd2 = this.zzfqt.zzd(zzaka, i2, zzbz);
                        if (zzd2 == null) {
                            StringBuilder sb = new StringBuilder(String.valueOf(zzaka).length() + 78);
                            sb.append("Missing value for markerColumn: ");
                            sb.append(zzaka);
                            sb.append(", at row: ");
                            sb.append(i2);
                            sb.append(", for window: ");
                            sb.append(zzbz);
                            throw new NullPointerException(sb.toString());
                        }
                        if (!zzd2.equals(zzd)) {
                            this.zzfwp.add(Integer.valueOf(i2));
                            zzd = zzd2;
                        }
                    }
                }
                this.zzfwo = true;
            }
        }
    }

    private final int zzcc(int i) {
        if (i >= 0 && i < this.zzfwp.size()) {
            return ((Integer) this.zzfwp.get(i)).intValue();
        }
        StringBuilder sb = new StringBuilder(53);
        sb.append("Position ");
        sb.append(i);
        sb.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(sb.toString());
    }

    public final T get(int i) {
        int i2;
        zzakb();
        int zzcc = zzcc(i);
        if (i < 0 || i == this.zzfwp.size()) {
            i2 = 0;
        } else {
            i2 = (i == this.zzfwp.size() - 1 ? this.zzfqt.zzfwg : ((Integer) this.zzfwp.get(i + 1)).intValue()) - ((Integer) this.zzfwp.get(i)).intValue();
            if (i2 == 1) {
                this.zzfqt.zzbz(zzcc(i));
            }
        }
        return zzl(zzcc, i2);
    }

    public int getCount() {
        zzakb();
        return this.zzfwp.size();
    }

    /* access modifiers changed from: protected */
    public abstract String zzaka();

    /* access modifiers changed from: protected */
    public abstract T zzl(int i, int i2);
}
