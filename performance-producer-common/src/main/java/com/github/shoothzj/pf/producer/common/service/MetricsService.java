package com.github.shoothzj.pf.producer.common.service;

import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.ProduceType;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hezhangjian
 */
@Slf4j
@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    private final Map<ProduceType, MetricFactory> map;

    public MetricsService(@Autowired MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.map = new ConcurrentHashMap<>();
    }

    public MetricFactory acquireMetricFactory(ProduceType produceType) {
        return map.computeIfAbsent(produceType, s -> new MetricFactory(meterRegistry, produceType));
    }

}
