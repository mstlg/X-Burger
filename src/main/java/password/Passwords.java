package password;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * @author Cameron Grout
 *
 * A utility class to hash login.passwords and check login.passwords vs hashed values. Based on code from
 * <a href="http://stackoverflow.com/a/18143616">this StackOverflow post</a>, modified to be
 * inline with OWASP recommendations for algorithm and salt length. Overloads and base64 helpers
 * have been added for ease-of-use.
 *
 * Sourced from UoA PGCertificate course
 */
public class Passwords {
    private static final Random RANDOM = new SecureRandom();
    private static final int DEFAULT_ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 512;
    private static final int DEFAULT_SALT_LENGTH = 32;

    /**
     * static utility class
     */
    private Passwords() { }

    /**
     *Returns a random number of iterations to be used when hashing a password.
     */
    public static int getNextNumIterations() {
        return (int) (RANDOM.nextDouble() * 90_000 + 10000);
    }

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @param saltLength Size of salt in bytes
     *
     * @return A saltLength bytes random salt
     */
    public static byte[] getNextSalt(int saltLength) {
        byte[] salt = new byte[saltLength];
        RANDOM.nextBytes(salt);
        return salt;
    }
    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password   the password to be hashed
     * @param salt       a 16 bytes salt, ideally obtained with the getNextSalt method
     * @param iterations the number of iterations to use in the hashing process
     *
     * @return the hashed password with a pinch of salt
     */
    public static byte[] hash(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);

        // Blank out the provided password array - prevents accidental leakage of this information
        Arrays.fill(password, Character.MIN_VALUE);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param iterations   the number of iterations to use in the hashing process
     * @param expectedHash the expected hashed value of the password
     *
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public static boolean isExpectedPassword(char[] password, byte[] salt, int iterations, byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt, iterations);

        // Blank out the provided password array - prevents accidental leakage of this information
        Arrays.fill(password, Character.MIN_VALUE);

        if (pwdHash.length != expectedHash.length) {
            return false;
        }

        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Decodes a base64 encoded string into its byte array equivalent
     *
     * @param b64 A base64 encoded String
     *
     * @return A byte array representing the data encoded in the b64 argument string
     * @throws IllegalArgumentException Thrown if the argument b64 is not a valid base64 string
     */
    public static byte[] base64Decode(String b64) throws IllegalArgumentException {
        return Base64.getDecoder().decode(b64);
    }

    /**
     * Convert a byte array into a base64 string representation for ease of printing and storage
     *
     * @param array Byte data to encode in base64
     *
     * @return A base64 encoded String representing the data contained in the array
     */
    public static String base64Encode(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }
}
