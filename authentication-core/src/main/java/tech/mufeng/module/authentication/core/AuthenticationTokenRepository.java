package tech.mufeng.module.authentication.core;

import tech.mufeng.module.authentication.core.model.TokenPersistent;

import java.io.Serializable;

/**
 * token存储接口
 * @author T.J
 * @date 2021/10/14 17:33
 */
public interface AuthenticationTokenRepository {
    /**
     * 保存token
     * @param key 键名
     * @param tokenPersistent 数据体
     * @return 是否成功
     */
    boolean putToken(Serializable key, TokenPersistent tokenPersistent);

    /**
     * 获取token
     * @param key 键名
     * @return 数据体
     */
    TokenPersistent getToken(Serializable key);

    /**
     * 删除token
     * @param key 键名
     * @return 是否成功
     */
    boolean deleteToken(Serializable key);
}
