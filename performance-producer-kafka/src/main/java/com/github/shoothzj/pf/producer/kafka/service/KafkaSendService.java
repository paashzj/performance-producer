package com.github.shoothzj.pf.producer.kafka.service;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.kafka.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * @author hezhangjian
 */
@Slf4j
public class KafkaSendService extends AbstractProduceThread {

    private KafkaConfig kafkaConfig;

    private List<KafkaProducer<String, String>> producers;

    private final Random random;

    public KafkaSendService(int index, int limit, KafkaConfig kafkaConfig) {
        super(index, limit);
        this.kafkaConfig = kafkaConfig;
        this.producers = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public void init() throws Exception {
        for (int i = 0; i < kafkaConfig.producerNum; i++) {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.kafkaAddr);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producers.add(new KafkaProducer<>(props));
        }
    }

    @Override
    protected void sendReq() throws Exception {
        ProducerRecord<String, String> record = getRecord(kafkaConfig.topic, kafkaConfig.messageByte);
        producers.get(random.nextInt(kafkaConfig.producerNum)).send(record, (recordMetadata, e) -> {
            if (e != null) {
                log.error("exception is ", e);
            } else {
                log.debug("send record to [{}]", record.topic());
            }
        });
    }

    private ProducerRecord<String, String> getRecord(String topic, int messageByte) {
        StringBuilder messageBuilder = new StringBuilder(messageByte);
        for (int i = 0; i < messageByte; i++) {
            messageBuilder.append('a' + random.nextInt(26));
        }
        return new ProducerRecord<>(topic, 0, "key", messageBuilder.toString());
    }


}
