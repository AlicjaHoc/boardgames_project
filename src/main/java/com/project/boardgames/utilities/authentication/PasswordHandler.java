package com.project.boardgames.utilities.authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;

public class PasswordHandler {

    private final String salt;

    public PasswordHandler(String salt) {
        this.salt = salt;
    }

    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10, new SecureRandom(salt.getBytes())));
    }

    public boolean matches(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }
}