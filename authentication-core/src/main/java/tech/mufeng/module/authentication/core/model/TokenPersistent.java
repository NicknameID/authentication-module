package tech.mufeng.module.authentication.core.model;

import java.io.Serializable;

/**
 * @author T.J
 * @date 2021/10/14 16:05
 */
public class TokenPersistent implements Serializable {
    private static final long serialVersionUID = 4375927301465188699L;

    /**
     * 随机签名密钥
     */
    private String secret;
    /**
     * 过期时间戳
     */
    private long expireAt;

    public TokenPersistent() {
    }

    public TokenPersistent(String secret, long expireAt) {
        this.secret = secret;
        this.expireAt = expireAt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }
}
