package com.github.shoothzj.pf.producer.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class KafkaConfig {

    @Value("${KAFKA_ADDR:localhost:9092}")
    public String kafkaAddr;

    @Value("${KAFKA_PRODUCER_NUM_PER_THREAD:1}")
    public int producerNum;

    @Value("${KAFKA_TOPIC:topic}")
    public String topic;

    @Value("${KAFKA_MESSAGE_BYTE:1024}")
    public int messageByte;

}
