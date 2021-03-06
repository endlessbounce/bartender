package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 * A utility class to hash passwords and check passwords vs hashed values.
 * It uses a combination of hashing and unique
 * salt. The algorithm used is PBKDF2WithHmacSHA1 which,
 * although not the best for hashing password (vs. bcrypt) is
 * still considered robust. The hashed value has 256 bits.
 */
public class HashCoder {

    // Constants ----------------------------------------------------------------------------------
    private static final String FACTORY = "PBKDF2WithHmacSHA1";

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private Random random = new SecureRandom();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @return a 16 bytes random salt
     */
    public byte[] getNextSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password the password to be hashed
     * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
     * @return the hashed password with a pinch of salt
     */
    public Optional<byte[]> hash(char[] password, byte[] salt) {
        Optional<byte[]> hashKey = Optional.empty();
        /*get password-based specification for generation of a key*/
        PBEKeySpec spec = new PBEKeySpec(password, salt, Constant.ITERATIONS, Constant.KEY_LENGTH);

        /*destroy password*/
        Arrays.fill(password, Character.MIN_VALUE);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(FACTORY);
            hashKey = Optional.of(skf.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.catching(e);
        } finally {
            spec.clearPassword();
        }
        return hashKey;
    }

    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
     * Side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
        byte[] passwordHash = hash(password, salt).orElse(new byte[0]);

        /*destroy password*/
        Arrays.fill(password, Character.MIN_VALUE);

        return Arrays.equals(passwordHash, expectedHash);
    }
}