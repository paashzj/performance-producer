package com.github.shoothzj.pf.producer.pulsar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class PulsarConfig {

    @Value("${PULSAR_ADDR:localhost:8080}")
    public String pulsarAddr;

    @Value("${PULSAR_PRODUCER_NUM_PER_THREAD:1}")
    public int producerNum;

    @Value("${PULSAR_TOPIC:topic}")
    public String topic;

    @Value("${PULSAR_MESSAGE_BYTE:1024}")
    public int messageByte;

}
