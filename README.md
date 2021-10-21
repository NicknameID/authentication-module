# 用户认证代码模块

## 用户认证代码模块是什么？
用户认证代码模块是一个Java程序包，JavaWeb开发中在开发用户认证功能时能提供开箱即用的特性，提高开发效率。

## 为什么要编写用户认证模块的程序包？
在平时开发中，每次开始开发一个新的服务时发现都要写一些功能重复的代码逻辑，比如用户认证的功能。所以为了提高开发效率，减少重读代码的复制粘贴，将用户
认证相关的通用逻辑提取出来放到了该程序包中。之后的开发只需要在pom.xml或build.gradle中声明依赖就能直接下载。

## 用户认证代码模块提供的什么功能？
1. 开箱即用的认证管理器，所有认证相关功能只需要和一个类打交到
2. 完成了认证消息的持久化抽象，支持多种持久化实现，如：Redis、SQL数据库、内存等，并允许用户自己扩展
3. (后续还有功能开发中...)

## 如何使用

### 声明依赖
使用maven在pom.xml中什么依赖
```xml
<dependency>
  <groupId>tech.mufeng.module</groupId>
  <artifactId>authentication-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 配置文件
在`applicaiton.yml`中声明配置
```yaml
authentication-module:
  # 是否启用authentication-module
  enable: true
  # 通用配置
  common:
    # 请求头中Token的键名，只有当authentication-module.mvc.enable=true时才生效
    headerTokenSymbol: X-Token
    # 密码编解码算法，可选： BCrypt、SCrypt、CUSTOM
    passwordEncoderAlgorithm: BCrypt
    # Token签名算法，可选：HmacMD5、HmacSHA1、HmacSHA256、HmacSHA384、HmacSHA512、HmacSM3
    tokenHmacAlgorithm: HmacSHA1
    # token颁发有效时间（Duration格式）
    grantDuration: 15m
  # MVC拦截器配置
  mvc:
    # 是否启用MVC配置
    enable: true
    # 需要拦截的路径
    matchPathPatterns: ['/**']
    # 需要排除的路径
    excludePathPatterns: ['/auth/login']
  # 防爆破配置
  antiBruteCrack:
    # 尝试认证的最大可重试次数
    maxRetryTimes: 10
    # 禁用时长
    banDuration: 1d
  # 持久化配置
  persistent:
    # 持久化类型，可选：INMEMORY、REDIS、CUSTOM
    type: REDIS
    # 持久化键名前缀
    keyPrefix: 'app'

```

如果要开启MVC拦截器自动配置需要添加依赖，即`authentication-module.mvc.enable=true`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

如果后端存储为Redis类型需要添加依赖，即`authentication-module.persistent.type=REDIS`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### API使用
核心类`AuthenticationManager`

使用示例
```java
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
```

