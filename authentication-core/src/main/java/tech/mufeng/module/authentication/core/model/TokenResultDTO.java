package tech.mufeng.module.authentication.core.model;

import java.time.Duration;

/**
 * @author T.J
 * @date 2021/10/14 11:19
 */
public class TokenResultDTO {
    private String token;
    private Long expiresSeconds;
    private Long expireAt;

    public TokenResultDTO(String token, Duration expire) {
        this.token = token;
        this.expiresSeconds = expire.toSeconds();
        this.expireAt = System.currentTimeMillis() + expire.toMillis();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresSeconds() {
        return expiresSeconds;
    }

    public void setExpiresSeconds(Long expiresSeconds) {
        this.expiresSeconds = expiresSeconds;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }
}
