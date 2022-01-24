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
