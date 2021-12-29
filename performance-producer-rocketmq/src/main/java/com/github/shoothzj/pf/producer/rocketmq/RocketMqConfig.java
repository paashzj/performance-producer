package com.github.shoothzj.pf.producer.rocketmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class RocketMqConfig {

    @Value("${ROCKETMQ_ADDR:localhost:9876}")
    public String rocketMqAddr;

    @Value("${ROCKETMQ_PRODUCER_NUM_PER_THREAD:1}")
    public int producerNum;

    @Value("${ROCKETMQ_GROUP_NAME:groupName}")
    public String groupName;

    @Value("${ROCKETMQ_TOPIC:topic}")
    public String topic;

    @Value("${ROCKETMQ_MESSAGE_BYTE:1024}")
    public int messageByte;
}
