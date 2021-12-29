package com.github.shoothzj.pf.producer.pulsar;

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

    @Value("${PULSAR_MEMORY_LIMIT_MB:50}")
    public int memoryLimitMb;

    @Value("${PULSAR_MAX_PENDING_MESSAGE:1000}")
    public int maxPendingMessage;

}
