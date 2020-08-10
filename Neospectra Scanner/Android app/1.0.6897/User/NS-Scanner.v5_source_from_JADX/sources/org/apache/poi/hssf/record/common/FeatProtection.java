package org.apache.poi.hssf.record.common;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class FeatProtection implements SharedFeature {
    public static long HAS_SELF_RELATIVE_SECURITY_FEATURE = 1;
    public static long NO_SELF_RELATIVE_SECURITY_FEATURE = 0;
    private int fSD;
    private int passwordVerifier;
    private byte[] securityDescriptor;
    private String title;

    public FeatProtection() {
        this.securityDescriptor = new byte[0];
    }

    public FeatProtection(RecordInputStream in) {
        this.fSD = in.readInt();
        this.passwordVerifier = in.readInt();
        this.title = StringUtil.readUnicodeString(in);
        this.securityDescriptor = in.readRemainder();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [FEATURE PROTECTION]\n");
        StringBuilder sb = new StringBuilder();
        sb.append("   Self Relative = ");
        sb.append(this.fSD);
        buffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("   Password Verifier = ");
        sb2.append(this.passwordVerifier);
        buffer.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("   Title = ");
        sb3.append(this.title);
        buffer.append(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("   Security Descriptor Size = ");
        sb4.append(this.securityDescriptor.length);
        buffer.append(sb4.toString());
        buffer.append(" [/FEATURE PROTECTION]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(this.fSD);
        out.writeInt(this.passwordVerifier);
        StringUtil.writeUnicodeString(out, this.title);
        out.write(this.securityDescriptor);
    }

    public int getDataSize() {
        return StringUtil.getEncodedSize(this.title) + 8 + this.securityDescriptor.length;
    }

    public int getPasswordVerifier() {
        return this.passwordVerifier;
    }

    public void setPasswordVerifier(int passwordVerifier2) {
        this.passwordVerifier = passwordVerifier2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int getFSD() {
        return this.fSD;
    }
}
