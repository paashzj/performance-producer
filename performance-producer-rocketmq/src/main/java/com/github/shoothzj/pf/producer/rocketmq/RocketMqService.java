package com.github.shoothzj.pf.producer.rocketmq;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricBean;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.github.shoothzj.pf.producer.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class RocketMqService extends AbstractProduceThread {

    private final RocketMqConfig config;

    private final Random random;

    private final List<DefaultMQProducer> producers;

    private final MetricBean metricBean;

    public RocketMqService(int index, MetricFactory metricFactory, ThreadConfig config, RocketMqConfig rocketMqConfig) {
        super(index, metricFactory, config);
        this.config = rocketMqConfig;
        this.producers = new ArrayList<>();
        this.random = new Random();
        this.metricBean = newMetricBean(OperationType.PRODUCE);

    }

    @Override
    public void init() throws Exception {
        for (int i = 0; i < config.producerNum; i++) {
            DefaultMQProducer producer = new
                    DefaultMQProducer(config.groupName + "_" + i);
            // Specify name server addresses.
            producer.setNamesrvAddr(config.rocketMqAddr);
            //Launch the instance.
            producer.start();
            producers.add(producer);
        }
    }

    @Override
    protected void send() {

        long startTime = System.currentTimeMillis();
        int index = random.nextInt(config.producerNum);
        String topic = config.topic + "_" + index;
        DefaultMQProducer producer = producers.get(index);
        try {
            Message msg = new Message(topic, RandomUtil.getRandomBytes(config.messageByte));
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    metricBean.success(System.currentTimeMillis() - startTime);
                    log.info("msg send to {} success. msgId {} queueOffset {}",
                            topic, sendResult.getMsgId(), sendResult.getQueueOffset());
                }

                @Override
                public void onException(Throwable e) {
                    metricBean.fail(System.currentTimeMillis() - startTime);
                    log.error("{} send msg callback failed", topic, e);
                }
            });
        } catch (MQClientException | RemotingException | InterruptedException e) {
            log.error("send msg to {} failed", topic, e);
        }
    }
}
