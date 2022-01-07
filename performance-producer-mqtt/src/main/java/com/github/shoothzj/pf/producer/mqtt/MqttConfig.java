package com.github.shoothzj.pf.producer.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class MqttConfig {

    @Value("${MQTT_HOST:localhost}")
    public String host;

    @Value("${MQTT_PORT:1883}")
    public int port;

    @Value("${CLIENT_ID:clientId}")
    public String clientId;

    @Value("${MQTT_TOPIC:topic}")
    public String topic;

    @Value("${MQTT_USERNAME:username}")
    public String username;

    @Value("${MQTT_PASSWORD:password}")
    public String password;

    @Value("${MQTT_PRODUCER_NUM_PER_THREAD:1}")
    public int producerNum;

    @Value("${MQTT_MESSAGE_BYTE:1024}")
    public int messageByte;

}
