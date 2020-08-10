package org.apache.poi.hssf.record.crypto;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutputStream;

public final class Biff8EncryptionKey {
    private static final int KEY_DIGEST_LENGTH = 5;
    private static final int PASSWORD_HASH_NUMBER_OF_BYTES_USED = 5;
    private static final ThreadLocal<String> _userPasswordTLS = new ThreadLocal<>();
    private final byte[] _keyDigest;

    public static Biff8EncryptionKey create(byte[] docId) {
        return new Biff8EncryptionKey(createKeyDigest(Decryptor.DEFAULT_PASSWORD, docId));
    }

    public static Biff8EncryptionKey create(String password, byte[] docIdData) {
        return new Biff8EncryptionKey(createKeyDigest(password, docIdData));
    }

    Biff8EncryptionKey(byte[] keyDigest) {
        if (keyDigest.length != 5) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected 5 byte key digest, but got ");
            sb.append(HexDump.toHex(keyDigest));
            throw new IllegalArgumentException(sb.toString());
        }
        this._keyDigest = keyDigest;
    }

    static byte[] createKeyDigest(String password, byte[] docIdData) {
        check16Bytes(docIdData, "docId");
        int nChars = Math.min(password.length(), 16);
        byte[] passwordData = new byte[(nChars * 2)];
        for (int i = 0; i < nChars; i++) {
            char ch = password.charAt(i);
            passwordData[(i * 2) + 0] = (byte) ((ch << 0) & 255);
            passwordData[(i * 2) + 1] = (byte) ((ch << 8) & 255);
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(passwordData);
            byte[] passwordHash = md5.digest();
            md5.reset();
            for (int i2 = 0; i2 < 16; i2++) {
                md5.update(passwordHash, 0, 5);
                md5.update(docIdData, 0, docIdData.length);
            }
            byte[] result = new byte[5];
            System.arraycopy(md5.digest(), 0, result, 0, 5);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validate(byte[] saltData, byte[] saltHash) {
        check16Bytes(saltData, "saltData");
        check16Bytes(saltHash, "saltHash");
        RC4 rc4 = createRC4(0);
        byte[] saltDataPrime = (byte[]) saltData.clone();
        rc4.encrypt(saltDataPrime);
        byte[] saltHashPrime = (byte[]) saltHash.clone();
        rc4.encrypt(saltHashPrime);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(saltDataPrime);
            return Arrays.equals(saltHashPrime, md5.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] xor(byte[] a, byte[] b) {
        byte[] c = new byte[a.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = (byte) (a[i] ^ b[i]);
        }
        return c;
    }

    private static void check16Bytes(byte[] data, String argName) {
        if (data.length != 16) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected 16 byte ");
            sb.append(argName);
            sb.append(", but got ");
            sb.append(HexDump.toHex(data));
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public RC4 createRC4(int keyBlockNo) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(this._keyDigest);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
            new LittleEndianOutputStream(baos).writeInt(keyBlockNo);
            md5.update(baos.toByteArray());
            return new RC4(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCurrentUserPassword(String password) {
        _userPasswordTLS.set(password);
    }

    public static String getCurrentUserPassword() {
        return (String) _userPasswordTLS.get();
    }
}
