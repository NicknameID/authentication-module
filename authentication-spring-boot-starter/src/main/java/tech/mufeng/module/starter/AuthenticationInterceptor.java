package tech.mufeng.module.starter;

import org.springframework.web.servlet.HandlerInterceptor;
import tech.mufeng.module.authentication.core.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author T.J
 * @date 2021/10/19 14:53
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationManager authenticationManager;
    private final String headerTokeSymbol;

    public AuthenticationInterceptor(AuthenticationManager authenticationManager, String headerTokeSymbol) {
        this.authenticationManager = authenticationManager;
        this.headerTokeSymbol = headerTokeSymbol;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(headerTokeSymbol);
        authenticationManager.match(token);
        return true;
    }
}
