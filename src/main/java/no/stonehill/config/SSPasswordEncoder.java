package no.stonehill.config;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SSPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return Crypt.crypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(Crypt.crypt(rawPassword.toString(), encodedPassword));
    }
}
