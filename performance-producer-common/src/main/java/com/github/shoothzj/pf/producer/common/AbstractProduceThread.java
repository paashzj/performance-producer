package com.github.shoothzj.pf.producer.common;

import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author hezhangjian
 */
@Slf4j
public abstract class AbstractProduceThread extends Thread {

    private final RateLimiter rateLimiter;

    private final long endTime;

    public AbstractProduceThread(int index, ThreadConfig config) {
        setName("produce-" + index);
        rateLimiter = RateLimiter.create(config.produceRate);
        endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(config.produceMinute);
    }

    public abstract void init() throws Exception;

    @Override
    public void run() {
        while (true) {
            if (System.currentTimeMillis() - endTime > 0) {
                break;
            }
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
