package tech.mufeng.module.authentication.core.exception;

/**
 * @author T.J
 * @date 2021/10/19 14:21
 */
public class BaseAuthenticationException extends RuntimeException {
    public BaseAuthenticationException() {
        super();
    }

    public BaseAuthenticationException(Throwable e) {
        super(e);
    }
}
