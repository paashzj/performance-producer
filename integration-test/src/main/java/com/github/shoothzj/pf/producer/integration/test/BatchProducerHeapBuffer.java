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

package com.github.shoothzj.pf.producer.integration.test;

import com.github.shoothzj.javatool.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.TypedMessageBuilder;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hezhangjian
 */
@Slf4j
public class BatchProducerHeapBuffer {

    public static void main(String[] args) throws Exception {
        System.setProperty("pulsar.allocator.pooled", "false");
        String topic = "test";
        System.out.println(String.format("topic is %s", topic));
        PulsarClient pulsarClient = PulsarClient.builder()
                .serviceUrl("http://127.0.0.1:8080")
                .build();
        ProducerBuilder<String> producerBuilder = pulsarClient.newProducer(Schema.STRING).enableBatching(true);
        Producer<String> producer = producerBuilder.topic(topic).create();

        while (true) {
            final TypedMessageBuilder<String> stringTypedMessageBuilder = producer.newMessage();
            final HashMap<String, String> map = new HashMap<>();
            map.put("1", "2");
            final TypedMessageBuilder<String> value = stringTypedMessageBuilder.key("1").value("2").properties(map);
            final CompletableFuture<MessageId> completableFuture = value.sendAsync();
            completableFuture.thenAccept(new Consumer<MessageId>() {
                @Override
                public void accept(MessageId messageId) {
                    log.debug("msg id is {}", messageId);
                }
            }).exceptionally(new Function<Throwable, Void>() {
                @Override
                public Void apply(Throwable throwable) {
                    log.error("send error ", throwable.getCause());
                    return null;
                }
            });
            CommonUtil.sleep(TimeUnit.MILLISECONDS, 1);
        }
    }

}
