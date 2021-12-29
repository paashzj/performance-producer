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
public class BatchProducer {

    public static void main(String[] args) throws Exception {
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
