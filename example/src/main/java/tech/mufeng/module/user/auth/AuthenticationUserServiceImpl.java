package tech.mufeng.module.user.auth;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import tech.mufeng.module.authentication.core.AuthenticationUserService;
import tech.mufeng.module.authentication.core.exception.UserNotExistException;
import tech.mufeng.module.authentication.core.model.UserInformation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author T.J
 * @date 2021/10/15 17:06
 */
@Service
public class AuthenticationUserServiceImpl implements AuthenticationUserService, InitializingBean {
    private Map<String, UserInformation> usersInfoMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 原始密码为：123456
         */
        UserInformation userInformation = new UserInformation(
                1,
                "zhangsan",
                "$2a$10$tzgqxfOlqthId6uD06h1d.MKG7nRhehhB0oXEWceds67kxxKmv462"
        );
        usersInfoMap.put("zhangsan", userInformation);
    }

    @Override
    public UserInformation getUserInformation(Serializable uid) throws UserNotExistException {
        UserInformation userInformation = usersInfoMap.get(String.valueOf(uid));
        if (userInformation == null) {
            throw new UserNotExistException();
        }
        return userInformation;
    }
}
