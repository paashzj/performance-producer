package com.github.shoothzj.pf.producer.pulsar;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricBean;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.github.shoothzj.pf.producer.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SizeUnit;

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

    private final MetricBean metricBean;

    public PulsarSendService(int index, MetricFactory metricFactory, ThreadConfig threadConfig,
                             PulsarConfig pulsarConfig) {
        super(index, metricFactory, threadConfig);
        this.producers = new ArrayList<>();
        this.pulsarConfig = pulsarConfig;
        this.random = new Random();
        this.metricBean = newMetricBean(OperationType.PRODUCE);
    }

    @Override
    public void init() throws Exception {
        PulsarClient client = PulsarClient.builder().memoryLimit(pulsarConfig.memoryLimitMb, SizeUnit.MEGA_BYTES)
                .serviceUrl(pulsarConfig.pulsarAddr).build();
        for (int i = 0; i < pulsarConfig.producerNum; i++) {
            Producer<byte[]> producer = client.newProducer().maxPendingMessages(pulsarConfig.maxPendingMessage)
                    .topic(pulsarConfig.topic).create();
            producers.add(producer);
        }
    }

    @Override
    protected void send() {
        long startTime = System.currentTimeMillis();
        try {
            CompletableFuture<MessageId> messageIdCompletableFuture = producers
                    .get(random.nextInt(pulsarConfig.producerNum))
                    .sendAsync(RandomUtil.getRandomBytes(pulsarConfig.messageByte));
            messageIdCompletableFuture.whenComplete((messageId, throwable) -> {
                if (throwable != null) {
                    metricBean.fail(System.currentTimeMillis() - startTime);
                    log.error("error is ", throwable);
                } else {
                    metricBean.success(System.currentTimeMillis() - startTime);
                    log.info("message id is [{}]", messageId);
                }
            });
        } catch (Exception e) {
            metricBean.fail(System.currentTimeMillis() - startTime);
            log.error("send req exception ", e);
        }
    }

}
