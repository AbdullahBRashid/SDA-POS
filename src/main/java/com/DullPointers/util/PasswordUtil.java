package com.DullPointers.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean check(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}
