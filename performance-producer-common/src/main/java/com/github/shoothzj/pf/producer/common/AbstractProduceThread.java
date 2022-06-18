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

package com.github.shoothzj.pf.producer.common;

import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricBean;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

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
