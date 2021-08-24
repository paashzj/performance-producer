package com.github.shoothzj.pf.producer.service;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.CommonConfig;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.config.PfConfig;
import com.github.shoothzj.pf.producer.http.config.HttpConfig;
import com.github.shoothzj.pf.producer.http.service.HttpSendService;
import com.github.shoothzj.pf.producer.kafka.config.KafkaConfig;
import com.github.shoothzj.pf.producer.kafka.service.KafkaSendService;
import com.github.shoothzj.pf.producer.module.ProduceType;
import com.github.shoothzj.pf.producer.pulsar.config.PulsarConfig;
import com.github.shoothzj.pf.producer.pulsar.service.PulsarSendService;
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
    private PulsarConfig pulsarConfig;

    private final List<AbstractProduceThread> threads = new ArrayList<>();

    @PostConstruct
    public void init() throws Exception {
        for (int i = 0; i < commonConfig.workNum; i++) {
            if (pfConfig.produceType.equals(ProduceType.HTTP)) {
                threads.add(new HttpSendService(i, threadConfig, httpConfig));
            } else if (pfConfig.produceType.equals(ProduceType.KAFKA)) {
                threads.add(new KafkaSendService(i, threadConfig, kafkaConfig));
            } else if (pfConfig.produceType.equals(ProduceType.PULSAR)) {
                threads.add(new PulsarSendService(i, threadConfig, pulsarConfig));
            }
        }
        for (AbstractProduceThread thread : threads) {
            thread.init();
            thread.start();
        }
    }

}