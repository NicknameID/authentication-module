package tech.mufeng.module.authentication.core.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import tech.mufeng.module.authentication.core.AuthenticationTokenService;
import tech.mufeng.module.authentication.core.model.TokenPersistent;
import tech.mufeng.module.authentication.core.model.TokenResultDTO;
import tech.mufeng.module.authentication.core.AuthenticationTokenRepository;
import tech.mufeng.module.authentication.core.exception.UnknownFormatTokenException;
import tech.mufeng.module.authentication.core.model.Token;

import java.io.Serializable;
import java.time.Duration;
import java.util.UUID;

/**
 * @author T.J
 * @date 2021/10/14 15:29
 */
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {
    private final HmacAlgorithm hmacAlgorithm;
    private final Duration retention;
    private final AuthenticationTokenRepository authenticationTokenRepository;

    public AuthenticationTokenServiceImpl(HmacAlgorithm hmacAlgorithm, Duration retention, AuthenticationTokenRepository authenticationTokenRepository) {
        this.hmacAlgorithm = hmacAlgorithm;
        this.retention = retention;
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    @Override
    public boolean match(String token) throws UnknownFormatTokenException {
        Token decodedToken = parseToken(token);
        String uid = decodedToken.getUid();
        String storageSign = decodedToken.getSign();
        TokenPersistent tokenPersistent = authenticationTokenRepository.getToken(uid);
        if (tokenPersistent == null) {
            return false;
        }
        String secret = tokenPersistent.getSecret();
        HMac mac = new HMac(hmacAlgorithm, StrUtil.bytes(secret));
        String sign = mac.digestHex(uid);
        return StrUtil.equals(sign, storageSign);
    }

    @Override
    public Token parseToken(String token) throws UnknownFormatTokenException {
        boolean isBase64 = Base64.isBase64(token);
        if (!isBase64) {
            throw new UnknownFormatTokenException();
        }
        String decodedTokenStr = Base64.decodeStr(token);
        try {
            return JSONUtil.toBean(decodedTokenStr, Token.class);
        }catch (JSONException e) {
            throw new UnknownFormatTokenException();
        }
    }

    @Override
    public TokenResultDTO grantToken(Serializable uid) {
        String userId = String.valueOf(uid);
        String secret = UUID.randomUUID().toString();
        HMac mac = new HMac(hmacAlgorithm, StrUtil.bytes(secret));
        String sign = mac.digestHex(userId);
        Token token = new Token(userId, sign);
        String tokenJsonStr = JSONUtil.toJsonStr(token);
        String tokenEncoded = Base64.encode(tokenJsonStr);
        // 放入缓存
        long expireAt = System.currentTimeMillis() + retention.toMillis();
        TokenPersistent tokenPersistent = new TokenPersistent(secret, expireAt);
        authenticationTokenRepository.putToken(userId, tokenPersistent);
        return new TokenResultDTO(tokenEncoded, retention);
    }

    @Override
    public TokenResultDTO refreshToken(String token) throws UnknownFormatTokenException {
        Token decodedToken = parseToken(token);
        String uid = decodedToken.getUid();
        cleanTokenByUid(uid);
        return grantToken(uid);
    }

    @Override
    public boolean cleanTokenByToken(String token) throws UnknownFormatTokenException {
        Token decodedToken = parseToken(token);
        return cleanTokenByUid(decodedToken.getUid());
    }

    @Override
    public boolean cleanTokenByUid(Serializable uid) {
        authenticationTokenRepository.deleteToken(String.valueOf(uid));
        return true;
    }
}
