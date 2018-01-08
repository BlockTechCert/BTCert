package org.bham.btcert.config;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author gkatzioura
 * @edit rxl635@bham.ac.uk
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12));
        return hashed;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
    
    public static void main(String[] args) {
		System.out.println(new CustomPasswordEncoder().encode("admin"));
	}

}
