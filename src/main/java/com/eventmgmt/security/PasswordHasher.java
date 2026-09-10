package com.eventmgmt.security;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    private static final int ITERATIONS = 65_536;
    private static final int KEY_BITS = 256;
    private PasswordHasher() {}

    public static String hash(String password) {
        byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + encode(password, salt);
    }

    public static boolean matches(String password, String stored) {
        if (stored == null || !stored.contains(":")) return false;
        String[] parts = stored.split(":", 2);
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        return java.security.MessageDigest.isEqual(
                parts[1].getBytes(java.nio.charset.StandardCharsets.UTF_8),
                encode(password, salt).getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private static String encode(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_BITS);
            return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded());
        } catch (GeneralSecurityException e) { throw new IllegalStateException("Password hashing unavailable", e); }
    }
}
