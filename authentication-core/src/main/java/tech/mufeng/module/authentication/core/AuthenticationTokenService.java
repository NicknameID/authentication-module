package tech.mufeng.module.authentication.core;

import tech.mufeng.module.authentication.core.exception.UnknownFormatTokenException;
import tech.mufeng.module.authentication.core.model.Token;
import tech.mufeng.module.authentication.core.model.TokenResultDTO;

import java.io.Serializable;

/**
 * @author T.J
 * @date 2021/10/14 11:31
 */
public interface AuthenticationTokenService {
    /**
     * 解析token为对象
     * @param token token
     * @return Token对象
     */
    Token parseToken(String token) throws UnknownFormatTokenException;
    /**
     * 校验token是否有效
     * @param token 待检查的token
     * @return true：合法token；false：非法token
     */
    boolean match(String token) throws UnknownFormatTokenException;

    /**
     * 颁发新token
     * @param uid 用户ID
     * @return token结果
     */
    TokenResultDTO grantToken(Serializable uid);

    /**
     * 刷新token
     * @param token 待刷新的token
     * @return token结果
     */
    TokenResultDTO refreshToken(String token) throws UnknownFormatTokenException;

    /**
     * 清除指定token
     * @param token 待清除token
     * @return 是否清除成功
     */
    boolean cleanTokenByToken(String token) throws UnknownFormatTokenException;

    /**
     * 清除指定用户的token
     * @param uid 用户ID
     * @return 是否清除成功
     */
    boolean cleanTokenByUid(Serializable uid);
}
