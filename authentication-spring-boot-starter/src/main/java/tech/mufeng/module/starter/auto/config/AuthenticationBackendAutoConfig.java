package tech.mufeng.module.starter.auto.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import tech.mufeng.module.authentication.core.AntiBruteCrackRepository;
import tech.mufeng.module.authentication.core.AuthenticationTokenRepository;
import tech.mufeng.module.authentication.core.impl.InMemoryAntiBruteCrackRepository;
import tech.mufeng.module.authentication.core.impl.InMemoryAuthenticationTokenRepository;
import tech.mufeng.module.starter.AuthenticationConfigProperties;
import tech.mufeng.module.starter.backend.RedisBackend;

/**
 * @author T.J
 * @date 2021/10/20 18:39
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "authentication-module.enable", havingValue = "true")
@EnableConfigurationProperties(AuthenticationConfigProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class AuthenticationBackendAutoConfig {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "authentication-module.persistent.type", havingValue = "INMEMORY", matchIfMissing = true)
    public static class InMemoryBackendConfig {
        @Bean
        @ConditionalOnMissingBean
        public AuthenticationTokenRepository inMemoryTokenRepository() {
            return new InMemoryAuthenticationTokenRepository();
        }

        @Bean
        @ConditionalOnMissingBean
        public AntiBruteCrackRepository inMemoryAntiBruteCrackRepository() {
            return new InMemoryAntiBruteCrackRepository();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "authentication-module.persistent.type", havingValue = "REDIS")
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean(RedisBackend.class)
    public static class RedisBackendConfig {
        @Bean
        @SuppressWarnings("unchecked")
        public RedisBackend redisBackend(AuthenticationConfigProperties properties, RedisTemplate redisTemplate) {
            String keyPrefix = properties.getPersistent().getKeyPrefix();
            return new RedisBackend(redisTemplate, keyPrefix);
        }
    }
}
