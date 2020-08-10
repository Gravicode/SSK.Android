package com.google.android.gms.internal;

abstract class zzcjl extends zzcjk {
    private boolean zzdtb;

    zzcjl(zzcim zzcim) {
        super(zzcim);
        this.zziwf.zzb(this);
    }

    public final void initialize() {
        if (this.zzdtb) {
            throw new IllegalStateException("Can't initialize twice");
        } else if (!zzaxz()) {
            this.zziwf.zzbak();
            this.zzdtb = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean isInitialized() {
        return this.zzdtb;
    }

    /* access modifiers changed from: protected */
    public abstract boolean zzaxz();

    /* access modifiers changed from: protected */
    public void zzayy() {
    }

    public final void zzazw() {
        if (this.zzdtb) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzayy();
        this.zziwf.zzbak();
        this.zzdtb = true;
    }

    /* access modifiers changed from: protected */
    public final void zzxf() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
