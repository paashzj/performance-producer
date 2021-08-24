package com.github.shoothzj.pf.producer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class ThreadConfig {

    @Value("${PRODUCE_RATE:1000}")
    public int produceRate;

    @Value("${PRODUCE_MINUTE:30}")
    public int produceMinute;

}
