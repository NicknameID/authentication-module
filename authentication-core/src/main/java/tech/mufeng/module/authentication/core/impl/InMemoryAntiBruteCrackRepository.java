package tech.mufeng.module.authentication.core.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import tech.mufeng.module.authentication.core.AntiBruteCrackRepository;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author T.J
 * @date 2021/10/14 18:20
 */
public class InMemoryAntiBruteCrackRepository implements AntiBruteCrackRepository {
    private final TimedCache<String, Integer> crackTimesMap = CacheUtil.newTimedCache(30000);

    public InMemoryAntiBruteCrackRepository() {
        crackTimesMap.schedulePrune(10000);
    }

    @Override
    public int plusAndGetCrackTimes(Serializable key, int times, Duration retention) {
        Integer crackTimes = crackTimesMap.get(String.valueOf(key));
        if (crackTimes == null) {
            crackTimes = 0;
        }
        crackTimes += times;
        crackTimesMap.put(String.valueOf(key), crackTimes, retention.toMillis());
        return crackTimes;
    }

    @Override
    public boolean deleteBan(Serializable key) {
        String k = String.valueOf(key);
        crackTimesMap.remove(k);
        return true;
    }

    @Override
    public boolean hasBan(Serializable key, int maxCrackTimes) {
        String k = String.valueOf(key);
        Integer times = crackTimesMap.get(k);
        if (times == null) {
            return false;
        }
        return times > maxCrackTimes;
    }
}
