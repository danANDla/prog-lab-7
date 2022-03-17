package utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordManager {

    public PasswordManager() {
    }

    public String shacode(String password){
        return Hashing.sha1().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
