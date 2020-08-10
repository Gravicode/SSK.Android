package org.apache.poi.poifs.crypt;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

public class EncryptionVerifier {
    private final byte[] salt = new byte[16];
    private final byte[] verifier = new byte[16];
    private final byte[] verifierHash;
    private final int verifierHashSize;

    public EncryptionVerifier(DocumentInputStream is, int encryptedLength) {
        if (is.readInt() != 16) {
            throw new RuntimeException("Salt size != 16 !?");
        }
        is.readFully(this.salt);
        is.readFully(this.verifier);
        this.verifierHashSize = is.readInt();
        this.verifierHash = new byte[encryptedLength];
        is.readFully(this.verifierHash);
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getVerifier() {
        return this.verifier;
    }

    public byte[] getVerifierHash() {
        return this.verifierHash;
    }
}
