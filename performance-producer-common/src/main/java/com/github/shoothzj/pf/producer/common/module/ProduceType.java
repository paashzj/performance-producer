package com.github.shoothzj.pf.producer.common.module;

/**
 * @author hezhangjian
 */
public enum ProduceType {

    // protocol
    HTTP,
    // middleware
    KAFKA,
    MQTT,
    PULSAR,
    ROCKETMQ,
    // relation Db
    POSTGRE,
    MYSQL,
    // No sql
    ES,
    REDIS,
    MONGO,
    // ts db
    INFLUX,
}
