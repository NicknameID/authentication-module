package tech.mufeng.module.starter.auto.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.mufeng.module.authentication.core.AuthenticationManager;
import tech.mufeng.module.starter.AuthenticationConfigProperties;
import tech.mufeng.module.starter.AuthenticationInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author T.J
 * @date 2021/10/21 15:28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "authentication-module.mvc.enable", havingValue = "true")
@ConditionalOnClass({HttpServletRequest.class, HttpServletResponse.class, HandlerInterceptor.class})
@EnableConfigurationProperties(AuthenticationConfigProperties.class)
@ConditionalOnBean(AuthenticationManager.class)
@AutoConfigureAfter(AuthenticationAutoConfig.class)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class AuthenticationMvcAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationInterceptor authenticationInterceptor(AuthenticationConfigProperties properties,
                                                               AuthenticationManager authenticationManager) {
        String headerTokenSymbol = properties.getCommon().getHeaderTokenSymbol();
        return new AuthenticationInterceptor(authenticationManager, headerTokenSymbol);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfigurer mvcConfig(AuthenticationConfigProperties properties, AuthenticationInterceptor authenticationInterceptor) {
        return new MvcConfig(properties, authenticationInterceptor);
    }


    public static class MvcConfig implements WebMvcConfigurer {
        private final AuthenticationConfigProperties properties;
        private final AuthenticationInterceptor authenticationInterceptor;

        public MvcConfig(AuthenticationConfigProperties properties, AuthenticationInterceptor authenticationInterceptor) {
            this.properties = properties;
            this.authenticationInterceptor = authenticationInterceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            Set<String> matchPathPatterns = properties.getMvc().getMatchPathPatterns();
            Set<String> excludePathPatterns = properties.getMvc().getExcludePathPatterns();
            registry.addInterceptor(authenticationInterceptor)
                    .addPathPatterns(new ArrayList<>(matchPathPatterns))
                    .excludePathPatterns(new ArrayList<>(excludePathPatterns));
        }
    }
}
