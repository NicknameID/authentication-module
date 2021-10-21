package tech.mufeng.module.user.auth;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import tech.mufeng.module.authentication.core.AuthenticationManager;
import tech.mufeng.module.authentication.core.model.TokenResultDTO;

import javax.annotation.Resource;

/**
 * @author T.J
 * @date 2021/10/18 17:47
 */
@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Resource
    private AuthenticationManager authenticationManager;

    @Data
    public static class LoginParam {
        private String username;
        private String password;
    }

    @PostMapping("/login")
    public TokenResultDTO login(@RequestBody LoginParam param) {
        return authenticationManager.grant(param.getUsername(), param.getPassword());
    }

    @GetMapping("/refresh")
    public TokenResultDTO refresh(@RequestHeader("X-Token") String token) {
        return authenticationManager.refresh(token);
    }

    @GetMapping("/logout")
    public Boolean logout(@RequestHeader("X-Token") String token) {
        return authenticationManager.revoke(token);
    }

    @GetMapping("/echo")
    public String echo() {
        return "OK";
    }
}
