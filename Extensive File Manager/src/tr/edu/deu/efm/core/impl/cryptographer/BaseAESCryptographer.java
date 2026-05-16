package tr.edu.deu.efm.core.impl.cryptographer;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class BaseAESCryptographer {

    protected static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    protected static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    protected static final int KEY_LENGTH = 256;
    protected static final int ITERATIONS = 65536;
    protected static final int SALT_LENGTH = 16;
    protected static final int IV_LENGTH = 16;

    protected SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        spec.clearPassword(); 
        return new SecretKeySpec(keyBytes, "AES");
    }
}
