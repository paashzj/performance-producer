package com.github.shoothzj.pf.producer.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class MongoConfig {

    @Value("${MONGO_ADDR:localhost:27017}")
    public String mongoAddr;

}
