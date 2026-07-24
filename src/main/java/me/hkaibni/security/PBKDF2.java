package me.hkaibni.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class PBKDF2 {
    private static int ITERATIONS = 10000;
    private static int KEYLENGTH = 256;

    public static void main(String[] args) throws Exception {
        String password = "mySecurePassword";
        byte[] salt = generateSalt(); // Generate a random salt
        int iterations = 10000;
        int keyLength = 256;
        // Hash the password using PBKDF2
//        byte[] hashedPassword = hashPassword(password, salt);

        // Convert the hashed password to a string for storage
     //   String hashedPasswordString = bytesToHex(hashPassword(password, salt));
//        System.out.println("Hashed Password: " + hashedPasswordString);
    }

    public static byte[] getSalt(){
        return generateSalt();
    }

    public static String hash(String password,byte [] salt) throws Exception {

        byte[] hashedPassword = hashPassword(password, salt);
        return bytesToHex(hashedPassword);
    }


    private static byte[] hashPassword(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEYLENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
    }
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 16 bytes for the salt
        random.nextBytes(salt);
        return salt;
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean validatePassword(
            String password,
            byte[] salt,
            String storedHash
    ) throws Exception {

        byte[] hashToCheck = hashPassword(password, salt);

        return bytesToHex(hashToCheck).equals(storedHash);
    }

}