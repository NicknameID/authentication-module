package tech.mufeng.module.starter.model;

/**
 * @author T.J
 * @date 2021/10/20 14:32
 */
public enum PasswordEncoderAlgorithmEnum {
    /**
     * BCryptPasswordEncoder
     */
    BCrypt,
    /**
     * SCryptPasswordEncoder
     */
    SCrypt,
    /**
     * 自定义 PasswordEncoderProvider
     */
    CUSTOM
    ;
}
