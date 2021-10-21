package tech.mufeng.module.authentication.core;

import cn.hutool.crypto.digest.HmacAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.mufeng.module.authentication.core.impl.*;

import java.time.Duration;

/**
 * @author T.J
 * @date 2021/10/14 14:30
 */
public class AuthenticationManagerBuilder {
    private AuthenticationUserService userService;
    private PasswordEncoderProvider passwordEncoderProvider;
    private AuthenticationTokenRepository authenticationTokenRepository;
    private AntiBruteCrackRepository antiBruteCrackRepository;
    private HmacAlgorithm tokenHmacAlgorithm = HmacAlgorithm.valueOf("HmacSHA1");
    private Duration tokenValidDuration = Duration.ofMinutes(20);
    private Integer antiBruteCrackMaxTimes = 10;
    private Duration antiBruteCrackBanDuration = Duration.ofDays(1);

    public AuthenticationManagerBuilder passwordEncoderProvider(PasswordEncoderProvider passwordEncoderProvider) {
        this.passwordEncoderProvider = passwordEncoderProvider;
        return this;
    }

    public AuthenticationManagerBuilder userService(AuthenticationUserService userService) {
        this.userService = userService;
        return this;
    }

    public AuthenticationManagerBuilder tokenRepository(AuthenticationTokenRepository authenticationTokenRepository) {
        this.authenticationTokenRepository = authenticationTokenRepository;
        return this;
    }

    public AuthenticationManagerBuilder antiBruteCrackRepository(AntiBruteCrackRepository antiBruteCrackRepository) {
        this.antiBruteCrackRepository = antiBruteCrackRepository;
        return this;
    }

    public AuthenticationManagerBuilder tokenHmacAlgorithm(HmacAlgorithm algorithm) {
        this.tokenHmacAlgorithm = algorithm;
        return this;
    }

    public AuthenticationManagerBuilder tokenValidDuration(Duration tokenValidDuration) {
        this.tokenValidDuration = tokenValidDuration;
        return this;
    }

    public AuthenticationManagerBuilder antiBruteCrackMaxTimes(int antiBruteCrackMaxTimes) {
        this.antiBruteCrackMaxTimes = antiBruteCrackMaxTimes;
        return this;
    }

    public AuthenticationManagerBuilder antiBruteCrackBanDuration(Duration antiBruteCrackBanDuration) {
        this.antiBruteCrackBanDuration = antiBruteCrackBanDuration;
        return this;
    }

    public AuthenticationManager build() {
        if (userService == null) {
            throw new IllegalArgumentException("userService is required");
        }

        if (antiBruteCrackRepository == null) {
            antiBruteCrackRepository = new InMemoryAntiBruteCrackRepository();
        }
        AuthenticationAntiBruteCrackService antiBruteCrackService =
                new AuthenticationAntiBruteCrackServiceImpl(antiBruteCrackMaxTimes, antiBruteCrackBanDuration, antiBruteCrackRepository);

        if (authenticationTokenRepository == null) {
            authenticationTokenRepository = new InMemoryAuthenticationTokenRepository();
        }
        AuthenticationTokenService tokenService =
                new AuthenticationTokenServiceImpl(tokenHmacAlgorithm, tokenValidDuration, authenticationTokenRepository);

        if (passwordEncoderProvider == null) {
            passwordEncoderProvider = new DefaultPasswordEncoderProvider();
        }
        PasswordEncoder passwordEncoder = passwordEncoderProvider.provide();
        return new AuthenticationManager(userService, antiBruteCrackService, tokenService, passwordEncoder);
    }
}
