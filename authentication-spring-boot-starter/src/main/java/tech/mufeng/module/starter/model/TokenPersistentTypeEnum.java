package tech.mufeng.module.starter.model;

/**
 * @author T.J
 * @date 2021/10/20 14:23
 */
public enum TokenPersistentTypeEnum {
    /**
     * 内存式的持久化
     */
    INMEMORY,
    /**
     * redis作为持久化设施
     */
    REDIS,
    /**
     * 自定义持久化实现类
     */
    CUSTOM;
}
