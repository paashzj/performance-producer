package com.github.shoothzj.pf.producer.service;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.CommonConfig;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.service.MetricsService;
import com.github.shoothzj.pf.producer.config.PfConfig;
import com.github.shoothzj.pf.producer.http.HttpConfig;
import com.github.shoothzj.pf.producer.http.HttpSendService;
import com.github.shoothzj.pf.producer.kafka.KafkaConfig;
import com.github.shoothzj.pf.producer.kafka.KafkaSendService;
import com.github.shoothzj.pf.producer.common.module.ProduceType;
import com.github.shoothzj.pf.producer.mqtt.MqttConfig;
import com.github.shoothzj.pf.producer.mqtt.MqttSendService;
import com.github.shoothzj.pf.producer.pulsar.PulsarConfig;
import com.github.shoothzj.pf.producer.pulsar.PulsarSendService;
import com.github.shoothzj.pf.producer.rocketmq.RocketMqConfig;
import com.github.shoothzj.pf.producer.rocketmq.RocketMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hezhangjian
 */
@Slf4j
@Service
public class BootService {

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private ThreadConfig threadConfig;

    @Autowired
    private PfConfig pfConfig;

    @Autowired
    private HttpConfig httpConfig;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private MqttConfig mqttConfig;

    @Autowired
    private PulsarConfig pulsarConfig;

    @Autowired
    private RocketMqConfig rocketMqConfig;

    @Autowired
    private MetricsService metricsService;

    private final List<AbstractProduceThread> threads = new ArrayList<>();

    @PostConstruct
    public void init() throws Exception {
        final MetricFactory metricFactory = metricsService.acquireMetricFactory(pfConfig.produceType);
        for (int i = 0; i < commonConfig.workNum; i++) {
            if (pfConfig.produceType.equals(ProduceType.HTTP)) {
                threads.add(new HttpSendService(i, metricFactory, threadConfig, httpConfig));
            } else if (pfConfig.produceType.equals(ProduceType.KAFKA)) {
                threads.add(new KafkaSendService(i, metricFactory, threadConfig, kafkaConfig));
            } else if (pfConfig.produceType.equals(ProduceType.MQTT)) {
                threads.add(new MqttSendService(i, metricFactory, threadConfig, mqttConfig));
            } else if (pfConfig.produceType.equals(ProduceType.PULSAR)) {
                threads.add(new PulsarSendService(i, metricFactory, threadConfig, pulsarConfig));
            } else if (pfConfig.produceType.equals(ProduceType.ROCKETMQ)) {
                threads.add(new RocketMqService(i, metricFactory, threadConfig, rocketMqConfig));
            }
        }
        for (AbstractProduceThread thread : threads) {
            thread.init();
            thread.start();
        }
    }

}
