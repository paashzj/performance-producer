package com.github.shoothzj.pf.producer.common.module;

/**
 * @author hezhangjian
 */
public enum ProduceType {

    // protocol
    HTTP,
    // middleware
    KAFKA,
    PULSAR,
    ROCKETMQ,
    // relation Db
    POSTGRE,
    Mysql,
    // No sql
    ElasticSearch,
    Redis,
    Mongo,
    // ts db
    Influx,
}
