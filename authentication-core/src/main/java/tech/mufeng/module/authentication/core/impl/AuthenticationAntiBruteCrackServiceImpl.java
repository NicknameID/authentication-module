package tech.mufeng.module.authentication.core.impl;

import tech.mufeng.module.authentication.core.AuthenticationAntiBruteCrackService;
import tech.mufeng.module.authentication.core.AntiBruteCrackRepository;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author T.J
 * @date 2021/10/14 15:05
 */
public class AuthenticationAntiBruteCrackServiceImpl implements AuthenticationAntiBruteCrackService {
    private final int maxCrackTimes;
    private final Duration banDuration;
    private final AntiBruteCrackRepository repository;

    public AuthenticationAntiBruteCrackServiceImpl(Integer maxCrackTimes, Duration banDuration, AntiBruteCrackRepository repository) {
        if (maxCrackTimes < 0) {
            throw new IllegalArgumentException("maxCrackTimes is negative");
        }
        this.maxCrackTimes = maxCrackTimes;
        this.banDuration = banDuration;
        this.repository = repository;
    }

    @Override
    public synchronized boolean markCrackTimes(Serializable uid, int times) {
        repository.plusAndGetCrackTimes(uid, times, banDuration);
        return true;
    }

    @Override
    public synchronized boolean reset(Serializable uid) {
        return repository.deleteBan(uid);
    }

    @Override
    public synchronized boolean banned(Serializable uid) {
        return repository.hasBan(uid, maxCrackTimes);
    }
}
