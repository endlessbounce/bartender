package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.exception.ServiceException;
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
public class Password {
    private Logger logger = LogManager.getLogger();
    private Random random = new SecureRandom();

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
        /*get password-based encryption key*/
        PBEKeySpec spec = new PBEKeySpec(password, salt, Constant.ITERATIONS, Constant.KEY_LENGTH);

        /*destroy password*/
        Arrays.fill(password, Character.MIN_VALUE);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
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
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
        boolean equals = true;
        Optional<byte[]> passwordHashOpt = hash(password, salt);
        byte [] passwordHash = passwordHashOpt.orElse(new byte[0]);

        /*destroy password*/
        Arrays.fill(password, Character.MIN_VALUE);

        if (passwordHash.length != expectedHash.length) {
            equals = false;
        } else {
            for (int i = 0; i < passwordHash.length; i++) {
                if (passwordHash[i] != expectedHash[i]) {
                    equals = false;
                }
            }
        }

        return equals;
    }

    /**
     * Generates a random password of a given length, using letters and digits.
     *
     * @param length the length of the password
     * @return a random password
     */
    public String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = random.nextInt(62);
            if (c <= 9) {
                sb.append(String.valueOf(c));
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }
}