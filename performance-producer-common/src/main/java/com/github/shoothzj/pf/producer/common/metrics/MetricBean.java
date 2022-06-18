/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.shoothzj.pf.producer.common.metrics;

import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.github.shoothzj.pf.producer.common.module.ProduceType;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

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

    public MetricBean(MeterRegistry meterRegistry, ProduceType produceType, OperationType operationType,
                      String... tags) {
        this.produceType = produceType;
        this.operationType = operationType;
        this.counter = meterRegistry.counter(acquireMetricName(COUNT_NAME_FORMAT), tags);
        this.successCounter = meterRegistry.counter(acquireMetricName(SUCCESS_COUNT_NAME_FORMAT), tags);
        this.failCounter = meterRegistry.counter(acquireMetricName(FAIL_COUNT_NAME_FORMAT), tags);
        this.successSummary = meterRegistry.summary(acquireMetricName(SUCCESS_LATENCY_SUMMARY_NAME_FORMAT), tags);
        this.failSummary = meterRegistry.summary(acquireMetricName(FAIL_LATENCY_SUMMARY_NAME_FORMAT), tags);
        this.successTimer = Timer.builder(acquireMetricName(SUCCESS_LATENCY_TIMER_NAME_FORMAT))
                .publishPercentiles(0.5, 0.75, 0.9, 0.95, 0.99, 0.999)
                .publishPercentileHistogram(true)
                .tags(tags).register(meterRegistry);
        this.failTimer = Timer.builder(acquireMetricName(FAIL_LATENCY_TIMER_NAME_FORMAT))
                .publishPercentiles(0.5, 0.75, 0.9, 0.95, 0.99, 0.999)
                .publishPercentileHistogram(true)
                .tags(tags).register(meterRegistry);
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
