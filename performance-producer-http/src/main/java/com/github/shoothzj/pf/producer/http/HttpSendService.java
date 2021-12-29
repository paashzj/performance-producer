package com.github.shoothzj.pf.producer.http;

import com.github.shoothzj.pf.producer.common.AbstractProduceThread;
import com.github.shoothzj.pf.producer.common.config.ThreadConfig;
import com.github.shoothzj.pf.producer.common.metrics.MetricBean;
import com.github.shoothzj.pf.producer.common.metrics.MetricFactory;
import com.github.shoothzj.pf.producer.common.module.OperationType;
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

    private final MetricBean metricBean;

    public HttpSendService(int index, MetricFactory metricFactory, ThreadConfig threadConfig, HttpConfig httpConfig) {
        super(index, metricFactory, threadConfig);
        this.httpConfig = httpConfig;
        this.metricBean = newMetricBean(OperationType.PRODUCE);
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
    protected void send() {
        long startTime = System.currentTimeMillis();
        try {
            HttpHost host = new HttpHost(httpConfig.httpHost, httpConfig.httpPort);
            SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest("POST", host, "/echo");
            simpleHttpRequest.setBody(HttpUtil.getHttpData(), ContentType.APPLICATION_JSON);
            client.execute(simpleHttpRequest, new FutureCallback<SimpleHttpResponse>() {
                @Override
                public void completed(SimpleHttpResponse simpleHttpResponse) {
                    if (simpleHttpResponse.getCode() >= 200 && simpleHttpResponse.getCode() > 200) {
                        metricBean.success(System.currentTimeMillis() - startTime);
                    } else {
                        metricBean.fail(System.currentTimeMillis() - startTime);
                    }
                    log.info("http request success, response code is [{}]", simpleHttpResponse.getCode());
                }

                @Override
                public void failed(Exception e) {
                    metricBean.fail(System.currentTimeMillis() - startTime);
                    log.error("send error is ", e);
                }

                @Override
                public void cancelled() {

                }
            });
        } catch (Exception e) {
            metricBean.fail(System.currentTimeMillis() - startTime);
            log.error("send req exception ", e);
        }
    }
}
