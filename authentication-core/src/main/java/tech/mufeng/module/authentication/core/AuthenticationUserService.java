package tech.mufeng.module.authentication.core;

import tech.mufeng.module.authentication.core.exception.UserNotExistException;
import tech.mufeng.module.authentication.core.model.UserInformation;

import java.io.Serializable;

/**
 * @author T.J
 * @date 2021/10/14 11:22
 */
public interface AuthenticationUserService {
    /**
     * 获取用户信息
     * @param uid 用户ID，可以是用户的数字型ID，也可以直接是用户的用户名
     * @return 用户信息
     * @throws UserNotExistException 没有找到用户异常
     */
    UserInformation getUserInformation(Serializable uid) throws UserNotExistException;
}
