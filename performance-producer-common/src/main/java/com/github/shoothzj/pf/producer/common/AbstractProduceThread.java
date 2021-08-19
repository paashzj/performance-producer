package com.github.shoothzj.pf.producer.common;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author hezhangjian
 */
@Slf4j
public abstract class AbstractProduceThread extends Thread {

    private RateLimiter rateLimiter;

    public AbstractProduceThread(int index, int limit) {
        setName("produce-" + index);
        rateLimiter = RateLimiter.create(limit);
    }

    public abstract void init() throws Exception;

    @Override
    public void run() {
        while (true) {
            if (rateLimiter.tryAcquire(2, TimeUnit.MILLISECONDS)) {
                try {
                    sendReq();
                } catch (Exception e) {
                    log.error("send req exception ", e);
                }
            }
        }
    }

    protected abstract void sendReq() throws Exception;

}
