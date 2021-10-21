package tech.mufeng.module.starter.auto.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import tech.mufeng.module.authentication.core.*;
import tech.mufeng.module.authentication.core.impl.DefaultPasswordEncoderProvider;
import tech.mufeng.module.starter.AuthenticationConfigProperties;

/**
 * @author T.J
 * @date 2021/10/20 14:19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({AuthenticationTokenRepository.class, AntiBruteCrackRepository.class, AuthenticationUserService.class})
@AutoConfigureAfter(AuthenticationBackendAutoConfig.class)
public class AuthenticationAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "authentication-module.common.passwordEncoderAlgorithm", havingValue = "BCrypt", matchIfMissing = true)
    public PasswordEncoderProvider bCryptPasswordEncoderProvider() {
        return new DefaultPasswordEncoderProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "authentication-module.common.passwordEncoderAlgorithm", havingValue = "SCrypt")
    public PasswordEncoderProvider sCryptPasswordEncoderProvider() {
        return new PasswordEncoderProvider() {
            @Override
            public PasswordEncoder provide() {
                return new SCryptPasswordEncoder();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfigProperties properties,
                                                       AuthenticationUserService userService,
                                                       PasswordEncoderProvider passwordEncoderProvider,
                                                       AuthenticationTokenRepository authenticationTokenRepository,
                                                       AntiBruteCrackRepository antiBruteCrackRepository
    ) {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder();
        return builder
                .userService(userService)
                .passwordEncoderProvider(passwordEncoderProvider)
                .tokenRepository(authenticationTokenRepository)
                .antiBruteCrackRepository(antiBruteCrackRepository)
                .tokenHmacAlgorithm(properties.getCommon().getTokenHmacAlgorithm())
                .tokenValidDuration(properties.getCommon().getGrantDuration())
                .antiBruteCrackMaxTimes(properties.getAntiBruteCrack().getMaxRetryTimes())
                .antiBruteCrackBanDuration(properties.getAntiBruteCrack().getBanDuration())
                .build();
    }
}
