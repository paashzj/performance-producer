package com.github.shoothzj.pf.producer.config;

import com.github.shoothzj.pf.producer.common.module.ProduceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class PfConfig {

    @Value("${PRODUCE_TYPE}")
    public ProduceType produceType;

}
