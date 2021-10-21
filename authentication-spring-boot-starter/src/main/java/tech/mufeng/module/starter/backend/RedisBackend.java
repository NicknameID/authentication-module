package tech.mufeng.module.starter.backend;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import tech.mufeng.module.authentication.core.AntiBruteCrackRepository;
import tech.mufeng.module.authentication.core.AuthenticationTokenRepository;
import tech.mufeng.module.authentication.core.model.TokenPersistent;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author T.J
 * @date 2021/10/20 15:06
 */
public class RedisBackend implements AuthenticationTokenRepository, AntiBruteCrackRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String keyPrefix;

    public RedisBackend(RedisTemplate<String, Object> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public int plusAndGetCrackTimes(Serializable key, int times, Duration retention) {
        String opsKey = keyPrefix + "ANTI_BRUTE_CRACK_TIMES:" + key;
        BoundValueOperations<String, Object> crackTimes = redisTemplate.boundValueOps(opsKey);
        Long count = crackTimes.increment(times);
        crackTimes.expire(retention);
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    @Override
    public boolean deleteBan(Serializable key) {
        String opsKey = keyPrefix + "ANTI_BRUTE_CRACK_TIMES:" + key;
        Boolean success = redisTemplate.delete(opsKey);
        return success != null && success;
    }

    @Override
    public boolean hasBan(Serializable key, int maxCrackTimes) {
        String opsKey = keyPrefix + "ANTI_BRUTE_CRACK_TIMES:" + key;
        BoundValueOperations<String, Object> crackTimes = redisTemplate.boundValueOps(opsKey);
        Object count = crackTimes.get();
        if (count == null) {
            return false;
        }
        return Integer.parseInt(count.toString()) > maxCrackTimes;
    }

    @Override
    public boolean putToken(Serializable key, TokenPersistent tokenPersistent) {
        String opsKey = keyPrefix + "TOKEN:" + key;
        Long expireAt = tokenPersistent.getExpireAt();
        long retentionMills = expireAt - System.currentTimeMillis();
        if (retentionMills <= 0) {
            return false;
        }
        BoundValueOperations<String, Object> token = redisTemplate.boundValueOps(opsKey);
        token.set(tokenPersistent, retentionMills * 2, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public TokenPersistent getToken(Serializable key) {
        String opsKey = keyPrefix + "TOKEN:" + key;
        Object tokenPersistent = redisTemplate.opsForValue().get(opsKey);
        if (tokenPersistent == null) {
            return null;
        }
        return (TokenPersistent) tokenPersistent;
    }

    @Override
    public boolean deleteToken(Serializable key) {
        String opsKey = keyPrefix + "TOKEN:" + key;
        Boolean success = redisTemplate.delete(opsKey);
        return success != null && success;
    }
}
