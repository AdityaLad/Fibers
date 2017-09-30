package com.fiber.rest.util;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;

@Service
public class RestHelper {

    @Suspendable
    public String callPost() {
        String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?></Request>http://posttestserver.com/post.php</Request>";
        HttpRequestBase httpRequestBase = new HttpPost("http://posttestserver.com/post.php");
        httpRequestBase.setHeader("Content-Type", "application/xml");
        try {
            ((HttpPost) httpRequestBase).setEntity(new StringEntity(requestBody));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        ((HttpPost) httpRequestBase).setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                .setSocketTimeout(10000).build());
        CloseableHttpClient httpClient = FiberHttpClientBuilder.create().build();
        String response = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequestBase);
            response = EntityUtils.toString(new BufferedHttpEntity(httpResponse.getEntity()), "UTF-8");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Suspendable
    public String callGet() {
        HttpRequestBase httpRequestBase = new HttpGet("https://stackoverflow.com/robots.txt");
        httpRequestBase.setHeader("Content-Type", "text/plain");

        ((HttpGet) httpRequestBase).setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                .setSocketTimeout(10000).build());
        CloseableHttpClient httpClient = FiberHttpClientBuilder.create().build();
        String response = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequestBase);
            response = EntityUtils.toString(new BufferedHttpEntity(httpResponse.getEntity()), "UTF-8");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
