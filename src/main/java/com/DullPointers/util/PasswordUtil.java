package com.DullPointers.util;

import org.mindrot.jbcrypt.BCrypt;

public interface PasswordUtil {
    static String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    static boolean check(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}