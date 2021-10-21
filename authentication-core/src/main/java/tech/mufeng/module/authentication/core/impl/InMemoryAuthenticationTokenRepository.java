package tech.mufeng.module.authentication.core.impl;

import tech.mufeng.module.authentication.core.AuthenticationTokenRepository;
import tech.mufeng.module.authentication.core.model.TokenPersistent;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author T.J
 * @date 2021/10/14 17:56
 */
public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {
    private final Map<String, TokenPersistent> tokenMap = new ConcurrentHashMap<>();

    @Override
    public boolean putToken(Serializable key, TokenPersistent tokenPersistent) {
        tokenMap.put(String.valueOf(key), tokenPersistent);;
        return true;
    }

    @Override
    public TokenPersistent getToken(Serializable key) {
        return tokenMap.get(String.valueOf(key));
    }

    @Override
    public boolean deleteToken(Serializable key) {
        tokenMap.remove(String.valueOf(key));
        return true;
    }
}
