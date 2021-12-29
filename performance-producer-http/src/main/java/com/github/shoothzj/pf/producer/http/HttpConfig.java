package com.github.shoothzj.pf.producer.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author hezhangjian
 */
@Configuration
@Service
public class HttpConfig {

    @Value("${HTTP_HOST:localhost}")
    public String httpHost;

    @Value("${HTTP_PORT:8080}")
    public int httpPort;

}
