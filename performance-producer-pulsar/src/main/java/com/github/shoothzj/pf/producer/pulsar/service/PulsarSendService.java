package com.github.shoothzj.pf.producer.pulsar.service;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.util.RandomUtil;
import com.github.shoothzj.pf.producer.pulsar.config.PulsarConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * @author hezhangjian
 */
@Slf4j
public class PulsarSendService extends AbstractProduceThread {

    private PulsarConfig pulsarConfig;

    private List<Producer<byte[]>> producers;

    private final Random random;

    public PulsarSendService(int index, ThreadConfig threadConfig, PulsarConfig pulsarConfig) {
        super(index, threadConfig);
        this.producers = new ArrayList<>();
        this.pulsarConfig = pulsarConfig;
        this.random = new Random();
    }

    @Override
    public void init() throws Exception {
        PulsarClient client = PulsarClient.builder().serviceUrl(pulsarConfig.pulsarAddr).build();
        for (int i = 0; i < pulsarConfig.producerNum; i++) {
            Producer<byte[]> producer = client.newProducer().topic(pulsarConfig.topic).create();
            producers.add(producer);
        }
    }

    @Override
    protected void sendReq() throws Exception {
        CompletableFuture<MessageId> messageIdCompletableFuture = producers.get(random.nextInt(pulsarConfig.producerNum)).sendAsync(RandomUtil.getRandomBytes(pulsarConfig.messageByte));
        messageIdCompletableFuture.whenComplete((messageId, throwable) -> {
            if (throwable != null) {
                log.error("error is ", throwable);
            } else {
                log.info("message id is [{}]", messageId);
            }
        });
    }

}
