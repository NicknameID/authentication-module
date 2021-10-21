package tech.mufeng.module.user.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.mufeng.module.authentication.core.exception.*;

/**
 * @author T.J
 * @date 2021/10/19 14:19
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BaseAuthenticationException.class)
    public String authOboutExceptionHandle(BaseAuthenticationException exception) {
        if (exception instanceof BannedException) {
            return "账户被禁用请联系管理员";
        }else if (exception instanceof IllegalTokenException) {
            return "请先登录";
        }else if (exception instanceof MistakePasswordException) {
            return "密码错误";
        }else if (exception instanceof UnknownFormatTokenException) {
            return "非法token";
        }else if (exception instanceof UserNotExistException) {
            return "用户不存在";
        }else {
            return "未知登录异常";
        }
    }
}
