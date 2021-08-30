package com.github.shoothzj.pf.producer.common.metrics;

import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.github.shoothzj.pf.producer.common.module.ProduceType;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hezhangjian
 */
@Slf4j
public class MetricFactory {

    private final MeterRegistry meterRegistry;

    private final ProduceType produceType;

    public MetricFactory(MeterRegistry meterRegistry, ProduceType produceType) {
        this.meterRegistry = meterRegistry;
        this.produceType = produceType;
    }

    public MetricBean newMetricBean(OperationType operationType, String... tags) {
        return new MetricBean(meterRegistry, produceType, operationType, tags);
    }

}
