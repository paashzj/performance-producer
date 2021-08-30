package com.github.shoothzj.pf.producer.common;

import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricBean;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.OperationType;
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

    protected MetricFactory metricFactory;

    public AbstractProduceThread(int index, MetricFactory metricFactory, ThreadConfig config) {
        setName("produce-" + index);
        this.metricFactory = metricFactory;
        this.rateLimiter = RateLimiter.create(config.produceRate);
        this.endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(config.produceMinute);
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
                    send();
                } catch (Throwable e) {
                    log.error("unexpected exception ", e);
                }
            }
        }
    }

    protected abstract void send();

    protected MetricBean newMetricBean(OperationType operationType) {
        return metricFactory.newMetricBean(operationType);
    }

    protected MetricBean newMetricBean(OperationType operationType, String... tags) {
        return metricFactory.newMetricBean(operationType, tags);
    }

}
