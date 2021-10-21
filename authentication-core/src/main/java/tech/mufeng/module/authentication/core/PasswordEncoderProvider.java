package tech.mufeng.module.authentication.core;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器提供适配器
 * @author T.J
 * @date 2021/10/14 11:08
 */
public interface PasswordEncoderProvider {
    /**
     * 提供一个 PasswordEncoder 实例
     * @return PasswordEncoder 实例
     */
    PasswordEncoder provide();
}
