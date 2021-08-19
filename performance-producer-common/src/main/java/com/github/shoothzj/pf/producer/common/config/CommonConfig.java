package com.github.shoothzj.pf.producer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class CommonConfig {

    @Value("${WORK_NUM:1}")
    public int workNum;

    @Value("${PRODUCE_RATE:1000}")
    public int produceRate;

}
