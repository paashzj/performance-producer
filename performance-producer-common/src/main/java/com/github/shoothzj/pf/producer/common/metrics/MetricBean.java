package com.github.shoothzj.pf.producer.common.metrics;

import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.github.shoothzj.pf.producer.common.module.ProduceType;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author hezhangjian
 */
@Slf4j
public class MetricBean {

    private static final String COUNT_NAME_FORMAT = "%s_%s_count";

    private static final String SUCCESS_COUNT_NAME_FORMAT = "%s_%s_success_count";

    private static final String FAIL_COUNT_NAME_FORMAT = "%s_%s_fail_count";

    private static final String SUCCESS_LATENCY_SUMMARY_NAME_FORMAT = "%s_%s_success_latency_summary";

    private static final String FAIL_LATENCY_SUMMARY_NAME_FORMAT = "%s_%s_fail_latency_summary";

    private static final String SUCCESS_LATENCY_TIMER_NAME_FORMAT = "%s_%s_success_latency_timer";

    private static final String FAIL_LATENCY_TIMER_NAME_FORMAT = "%s_%s_fail_latency_timer";

    private final ProduceType produceType;

    private final OperationType operationType;

    private final Counter counter;

    private final Counter successCounter;

    private final Counter failCounter;

    private final DistributionSummary successSummary;

    private final DistributionSummary failSummary;

    private final Timer successTimer;

    private final Timer failTimer;

    public MetricBean(MeterRegistry meterRegistry, ProduceType produceType, OperationType operationType, String... tags) {
        this.produceType = produceType;
        this.operationType = operationType;
        this.counter = meterRegistry.counter(acquireMetricName(COUNT_NAME_FORMAT), tags);
        this.successCounter = meterRegistry.counter(acquireMetricName(SUCCESS_COUNT_NAME_FORMAT), tags);
        this.failCounter = meterRegistry.counter(acquireMetricName(FAIL_COUNT_NAME_FORMAT), tags);
        this.successSummary = meterRegistry.summary(acquireMetricName(SUCCESS_LATENCY_SUMMARY_NAME_FORMAT), tags);
        this.failSummary = meterRegistry.summary(acquireMetricName(FAIL_LATENCY_SUMMARY_NAME_FORMAT), tags);
        this.successTimer = meterRegistry.timer(acquireMetricName(SUCCESS_LATENCY_TIMER_NAME_FORMAT), tags);
        this.failTimer = meterRegistry.timer(acquireMetricName(FAIL_LATENCY_TIMER_NAME_FORMAT), tags);
    }

    private String acquireMetricName(String format) {
        return String.format(format, produceType, operationType);
    }

    public void success(long cost) {
        counter.increment();
        successCounter.increment();
        successSummary.record(cost);
        successTimer.record(cost, TimeUnit.MILLISECONDS);
    }

    public void fail(long cost) {
        counter.increment();
        failCounter.increment();
        failSummary.record(cost);
        failTimer.record(cost, TimeUnit.MILLISECONDS);
    }

}
