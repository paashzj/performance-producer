package com.github.shoothzj.pf.producer.http.service;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.http.config.HttpConfig;
import com.github.shoothzj.pf.producer.http.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

/**
 * @author hezhangjian
 */
@Slf4j
public class HttpSendService extends AbstractProduceThread {

    private HttpConfig httpConfig;

    private CloseableHttpAsyncClient client;

    public HttpSendService(int index, int limit, HttpConfig httpConfig) {
        super(index, limit);
        this.httpConfig = httpConfig;
    }

    @Override
    public void init() throws Exception {
        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(5))
                .build();

        final AsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnPerRoute(1000).setMaxConnTotal(2000).build();

        client = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .setIOReactorConfig(ioReactorConfig)
                .build();

        client.start();
    }

    @Override
    protected void sendReq() throws Exception {
        HttpHost host = new HttpHost(httpConfig.httpHost, httpConfig.httpPort);
        SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest("POST", host, "/echo");
        simpleHttpRequest.setBody(HttpUtil.getHttpData(), ContentType.APPLICATION_JSON);
        client.execute(simpleHttpRequest, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse simpleHttpResponse) {
                log.info("http request success, response code is [{}]", simpleHttpResponse.getCode());
            }

            @Override
            public void failed(Exception e) {
                log.error("fucking error is ", e);
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
