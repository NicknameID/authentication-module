package tech.mufeng.module.authentication.core.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.mufeng.module.authentication.core.PasswordEncoderProvider;

/**
 * @author T.J
 * @date 2021/10/14 11:10
 */
public class DefaultPasswordEncoderProvider implements PasswordEncoderProvider {

    @Override
    public PasswordEncoder provide() {
        return new BCryptPasswordEncoder();
    }
}
