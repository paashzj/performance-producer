package com.github.shoothzj.pf.producer.mqtt;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricBean;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.OperationType;
import com.github.shoothzj.pf.producer.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class MqttSendService extends AbstractProduceThread {

    private final MqttConfig mqttConfig;

    private final List<MqttClient> mqttClients;

    private final Random random;

    private final MetricBean metricBean;

    public MqttSendService(int index, MetricFactory metricFactory, ThreadConfig config, MqttConfig mqttConfig) {
        super(index, metricFactory, config);
        this.mqttConfig = mqttConfig;
        this.mqttClients = new ArrayList<>();
        this.random = new Random();
        this.metricBean = newMetricBean(OperationType.PRODUCE);
    }

    @Override
    public void init() throws Exception {
        for (int i = 0; i < mqttConfig.producerNum; i++) {
            MqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d", mqttConfig.host, mqttConfig.port),
                    mqttConfig.clientId);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName(mqttConfig.username);
            mqttConnectOptions.setPassword(mqttConfig.password.toCharArray());
            mqttClient.connect(mqttConnectOptions);
            mqttClients.add(mqttClient);
        }
    }

    @Override
    protected void send() {
        long startTime = System.currentTimeMillis();
        try {
            MqttMessage mqttMessage = getMqttMessage(mqttConfig.messageByte);
            mqttClients.get(random.nextInt(mqttConfig.producerNum)).publish(mqttConfig.topic,
                    mqttMessage);
        } catch (Exception e) {
            metricBean.fail(System.currentTimeMillis() - startTime);
            log.error("send req exception ", e);
        }
    }

    private MqttMessage getMqttMessage(int messageByte) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(0);
        mqttMessage.setPayload(RandomUtil.getRandomBytes(messageByte));
        return mqttMessage;
    }

}
