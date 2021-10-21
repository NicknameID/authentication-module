package tech.mufeng.module.authentication.core;

import org.springframework.security.crypto.password.PasswordEncoder;
import tech.mufeng.module.authentication.core.exception.BannedException;
import tech.mufeng.module.authentication.core.exception.IllegalTokenException;
import tech.mufeng.module.authentication.core.exception.MistakePasswordException;
import tech.mufeng.module.authentication.core.exception.UnknownFormatTokenException;
import tech.mufeng.module.authentication.core.model.Token;
import tech.mufeng.module.authentication.core.model.TokenResultDTO;
import tech.mufeng.module.authentication.core.model.UserInformation;

import java.io.Serializable;

/**
 * 认证管理器
 * @author T.J
 * @date 2021/10/14 11:16
 */
public class AuthenticationManager {
    private final AuthenticationUserService userService;
    private final AuthenticationAntiBruteCrackService antiBruteCrackService;
    private final AuthenticationTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationManager(AuthenticationUserService userService,
                                 AuthenticationAntiBruteCrackService antiBruteCrackService,
                                 AuthenticationTokenService tokenService,
                                 PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.antiBruteCrackService = antiBruteCrackService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 颁发认证
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    public TokenResultDTO grant(String username, String password) {
        UserInformation userInformation = userService.getUserInformation(username);
        Serializable uid = userInformation.getUid();
        boolean banned = antiBruteCrackService.banned(uid);
        if (banned) {
            throw new BannedException();
        }
        String encodedPassword = userInformation.getEncodedPassword();
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        if (!matches) {
            antiBruteCrackService.markCrackTimes(uid, 1);
            throw new MistakePasswordException();
        }
        antiBruteCrackService.reset(uid);
        return tokenService.grantToken(uid);
    }

    public void match(String token) {
        if (!tokenService.match(token)) {
            throw new IllegalTokenException();
        }
    }

    /**
     * 刷新凭证
     * @param token 老token
     * @return 新的token
     */
    public TokenResultDTO refresh(String token) {
        match(token);
        return tokenService.refreshToken(token);
    }

    /**
     * 吊销认证
     * @return 是否成功
     */
    public boolean revokeByUid(String uid) {
        return tokenService.cleanTokenByUid(uid);
    }

    /**
     * 吊销认证
     * @return 是否成功
     */
    public boolean revoke(String token) {
        return tokenService.cleanTokenByToken(token);
    }

    /**
     * 解析token中的userId
     * @param token token
     * @return 用户ID
     */
    public Serializable parseUid(String token) throws UnknownFormatTokenException{
        Token token1 = tokenService.parseToken(token);
        return token1.getUid();
    }
}
