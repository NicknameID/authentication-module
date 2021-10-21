package tech.mufeng.module.starter;

import cn.hutool.crypto.digest.HmacAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tech.mufeng.module.starter.model.PasswordEncoderAlgorithmEnum;
import tech.mufeng.module.starter.model.TokenPersistentTypeEnum;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static tech.mufeng.module.starter.model.PasswordEncoderAlgorithmEnum.BCrypt;
import static tech.mufeng.module.starter.model.TokenPersistentTypeEnum.INMEMORY;

/**
 * @author T.J
 * @date 2021/10/20 14:19
 */
@ConfigurationProperties(prefix = "authentication-module")
public class AuthenticationConfigProperties {
    private boolean enable;
    private Common common;
    private MvcConfig mvc;
    private PersistentConfig persistent;
    private AntiBruteCrack antiBruteCrack;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PersistentConfig getPersistent() {
        return persistent;
    }

    public void setPersistent(PersistentConfig persistent) {
        this.persistent = persistent;
    }

    public AntiBruteCrack getAntiBruteCrack() {
        return antiBruteCrack;
    }

    public void setAntiBruteCrack(AntiBruteCrack antiBruteCrack) {
        this.antiBruteCrack = antiBruteCrack;
    }

    public MvcConfig getMvc() {
        return mvc;
    }

    public void setMvc(MvcConfig mvc) {
        this.mvc = mvc;
    }

    public static class Common {
        /**
         * 请求体token键名
         */
        private String headerTokenSymbol = "X-Token";
        /**
         * 密码编解码算法
         */
        private PasswordEncoderAlgorithmEnum passwordEncoderAlgorithm = BCrypt;
        /**
         * token签名算法
         */
        private HmacAlgorithm tokenHmacAlgorithm = HmacAlgorithm.HmacSHA1;
        /**
         * 认证颁发有效时长
         */
        private Duration grantDuration = Duration.ofMinutes(20);

        public String getHeaderTokenSymbol() {
            return headerTokenSymbol;
        }

        public void setHeaderTokenSymbol(String headerTokenSymbol) {
            this.headerTokenSymbol = headerTokenSymbol;
        }

        public PasswordEncoderAlgorithmEnum getPasswordEncoderAlgorithm() {
            return passwordEncoderAlgorithm;
        }

        public void setPasswordEncoderAlgorithm(PasswordEncoderAlgorithmEnum passwordEncoderAlgorithm) {
            this.passwordEncoderAlgorithm = passwordEncoderAlgorithm;
        }

        public HmacAlgorithm getTokenHmacAlgorithm() {
            return tokenHmacAlgorithm;
        }

        public void setTokenHmacAlgorithm(HmacAlgorithm tokenHmacAlgorithm) {
            this.tokenHmacAlgorithm = tokenHmacAlgorithm;
        }

        public Duration getGrantDuration() {
            return grantDuration;
        }

        public void setGrantDuration(Duration grantDuration) {
            this.grantDuration = grantDuration;
        }
    }

    public static class MvcConfig {
        /**
         * 是否开启mvc拦截器配置
         */
        private boolean enable;
        /**
         * 需要匹配的路径
         */
        private Set<String> matchPathPatterns = new HashSet<>();
        /**
         * 需要排除的路径
         */
        private Set<String> excludePathPatterns = new HashSet<>();

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public Set<String> getMatchPathPatterns() {
            return matchPathPatterns;
        }

        public void setMatchPathPatterns(Set<String> matchPathPatterns) {
            this.matchPathPatterns = matchPathPatterns;
        }

        public Set<String> getExcludePathPatterns() {
            return excludePathPatterns;
        }

        public void setExcludePathPatterns(Set<String> excludePathPatterns) {
            this.excludePathPatterns = excludePathPatterns;
        }
    }

    public static class PersistentConfig {
        /**
         * 持久化设施类型
         */
        private TokenPersistentTypeEnum type = INMEMORY;
        /**
         * 持久化键名前缀
         */
        private String keyPrefix = "";

        public TokenPersistentTypeEnum getType() {
            return type;
        }

        public void setType(TokenPersistentTypeEnum type) {
            this.type = type;
        }

        public String getKeyPrefix() {
            return keyPrefix;
        }

        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }
    }

    public static class AntiBruteCrack {
        /**
         * 最大重试次数
         */
        private Integer maxRetryTimes = 10;
        /**
         * 超过最大重试次数后，禁用账户的时长
         */
        private Duration banDuration = Duration.ofDays(1);

        public Integer getMaxRetryTimes() {
            return maxRetryTimes;
        }

        public void setMaxRetryTimes(Integer maxRetryTimes) {
            this.maxRetryTimes = maxRetryTimes;
        }

        public Duration getBanDuration() {
            return banDuration;
        }

        public void setBanDuration(Duration banDuration) {
            this.banDuration = banDuration;
        }
    }
}
