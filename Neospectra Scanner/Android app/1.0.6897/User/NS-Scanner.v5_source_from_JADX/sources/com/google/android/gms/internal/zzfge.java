package com.google.android.gms.internal;

import java.io.IOException;

public class zzfge extends IOException {
    private zzfhe zzphw = null;

    public zzfge(String str) {
        super(str);
    }

    static zzfge zzcya() {
        return new zzfge("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    static zzfge zzcyb() {
        return new zzfge("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    static zzfge zzcyc() {
        return new zzfge("CodedInputStream encountered a malformed varint.");
    }

    static zzfge zzcyd() {
        return new zzfge("Protocol message contained an invalid tag (zero).");
    }

    static zzfge zzcye() {
        return new zzfge("Protocol message end-group tag did not match expected tag.");
    }

    static zzfgf zzcyf() {
        return new zzfgf("Protocol message tag had invalid wire type.");
    }

    static zzfge zzcyg() {
        return new zzfge("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    static zzfge zzcyh() {
        return new zzfge("Protocol message was too large.  May be malicious.  Use CodedInputStream.setSizeLimit() to increase the size limit.");
    }

    static zzfge zzcyi() {
        return new zzfge("Protocol message had invalid UTF-8.");
    }

    public final zzfge zzi(zzfhe zzfhe) {
        this.zzphw = zzfhe;
        return this;
    }
}
